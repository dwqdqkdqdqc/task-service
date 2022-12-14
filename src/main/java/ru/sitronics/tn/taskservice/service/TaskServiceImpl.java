package ru.sitronics.tn.taskservice.service;

import io.github.perplexhub.rsql.RSQLJPASupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.sitronics.tn.taskservice.dto.*;
import ru.sitronics.tn.taskservice.exception.BpmsException;
import ru.sitronics.tn.taskservice.exception.IllegalActionException;
import ru.sitronics.tn.taskservice.exception.ResourceNotFoundException;
import ru.sitronics.tn.taskservice.model.Task;
import ru.sitronics.tn.taskservice.model.TaskStatusDict;
import ru.sitronics.tn.taskservice.model.TaskTypeDict;
import ru.sitronics.tn.taskservice.repository.TaskRepository;
import ru.sitronics.tn.taskservice.repository.TaskStatusDictRepository;
import ru.sitronics.tn.taskservice.repository.TaskTypeDictRepository;
import ru.sitronics.tn.taskservice.validation.BpmValidation;
import ru.sitronics.tn.taskservice.util.CustomRestClient;
import ru.sitronics.tn.taskservice.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static ru.sitronics.tn.taskservice.model.TaskStatusEnum.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    @Value("${rsql.defaultSort}")
    private String defaultSort;

    @Value("${rsql.defaultPageSize}")
    private Integer defaultPageSize;

    private final CustomRestClient customRestClient;
    private final TaskRepository taskRepository;
    private final TaskTypeDictRepository taskTypeDictRepository;
    private final TaskStatusDictRepository taskStatusDictRepository;
    private final BpmValidation bpmValidation;

    @Override
    public Map<String, String> getTaskTypes() {
        Map<String, String> m = new HashMap<>();
        List<TaskTypeDict> taskTypes = taskTypeDictRepository.findAll();
        taskTypes.forEach(taskTypeDict -> m.put(taskTypeDict.getCode(), taskTypeDict.getFullValue()));
        return m;
    }

    @Override
    public Map<String, String> getTaskStatuses() {
        Map<String, String> m = new HashMap<>();
        List<TaskStatusDict> taskStatuses = taskStatusDictRepository.findAll();
        taskStatuses.forEach(taskStatusDict -> m.put(taskStatusDict.getCode(), taskStatusDict.getFullValue()));
        return m;
    }

    @Override
    public TaskOutDto createTask(TaskInDto taskInDto) {
        Task task = ObjectUtils.convertObject(taskInDto, new Task());
        return ObjectUtils.convertObject(taskRepository.save(task), new TaskOutDto());
    }

    @Override
    @SuppressWarnings("unchecked")
    public TaskPageDto getTasks(String filter, Integer page, Integer size, String sort, String fields) {

        //pages start from 1 for user
        if (page == null || page < 1) {
            log.warn("Invalid page value (page = {}). Set default page value = 0", page);
            page = 0; //default page
        } else --page;

        if (sort == null || sort.isBlank()) {
            log.warn("Invalid sort value (sort = {}). Set default sort value = {}", sort, defaultSort);
            sort = defaultSort;
        }
        if (size == null || size <= 0) {
            log.warn("Invalid size value (size = {}). Set default size  = {}", size, defaultPageSize);
            size = defaultPageSize;
        }

        TaskPageDto taskPageDto = new TaskPageDto();
        taskPageDto.setSort(sort);
        taskPageDto.setPage(page + 1);
        taskPageDto.setElementsOnPage(size);

        Page<Task> taskPage;

        if (filter == null || filter.isBlank()) {
            taskPage = taskRepository.findAll(RSQLJPASupport.toSort(sort), PageRequest.of(page, size));
        } else {
            taskPageDto.setFilter(filter);
            Specification<?> specification = RSQLJPASupport.toSpecification(filter).and(RSQLJPASupport.toSort(sort));
            taskPage = taskRepository.findAll((Specification<Task>) specification, PageRequest.of(page, size));
        }
        taskPageDto.setTotalAmount(taskPage.getTotalElements());
        taskPageDto.setPages(taskPage.getTotalPages());
        taskPageDto.setEntity(taskPage.stream()
                .map(task -> {
                    TaskOutDto taskOutDto = new TaskOutDto();
                    if (fields == null) {
                        ObjectUtils.convertObject(task, taskOutDto);
                    } else {
                        ObjectUtils.convertObject(task, taskOutDto, fields);
                    }
                    return taskOutDto;
                })
                .collect(Collectors.toList()));
        return taskPageDto;
    }

    @Override
    @Transactional
    public void claimTask(UUID taskId, String userId) {
        Task task = getTask(taskId);
        checkIfStatusValid(task);
        if (!StringUtils.hasText(task.getAssignee())) {
            task.setAssignee(userId);
            task.setStatus(IN_PROGRESS.toString());
            taskRepository.save(task);
        } else {
            throw new IllegalActionException(String.format("Task with id %s is already assigned", taskId));
        }
    }

    @Override
    @Transactional
    public void unclaimTask(UUID taskId) {
        Task task = getTask(taskId);
        checkIfStatusValid(task);
        task.setAssignee(null);
        task.setStatus(PENDING.toString());
        task.setReadByAssignee(false);
        taskRepository.save(task);
    }

    @Override
    @Transactional
    public void reassignByCurrentUser(UUID taskId, String currentUserId, String newUserId) {
        Task task = getTask(taskId);
        checkIfStatusValid(task);
        if (Objects.equals(task.getAssignee(), currentUserId) && StringUtils.hasText(currentUserId)) {
            task.setAssignee(newUserId);
            task.setReadByAssignee(false);
            taskRepository.save(task);
        } else {
            throw new IllegalActionException(String.format("Task %s can be reassigned only by current assignee", taskId));
        }
    }

    @Override
    public TaskCountDto countByAssigneeAndReadByAssignee(String assignee, boolean readByAssignee){
        TaskCountDto taskCountDto = new TaskCountDto();
        taskCountDto.setCount(taskRepository.countByAssigneeAndReadByAssignee( assignee, readByAssignee));
        return taskCountDto;
    }

    @Override
    @Transactional
    //TODO UserId validation?
    public void completeTask(UUID taskId, CompleteTaskDto completeTaskDto) {
        Task task = getTask(taskId);
        checkIfStatusValid(task);
        if (!task.getValidationProcessKey().isBlank()) {
            bpmValidation.validateTaskCompletion(task, completeTaskDto, task.getValidationProcessKey());
        }
        String endPointUri = String.format("/task/%s/complete", task.getProcessEngineTaskId());
        ResponseEntity<Void> response = customRestClient.postJson(endPointUri, completeTaskDto, Void.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            task.setStatus(COMPLETED.toString());
            taskRepository.save(task);
        } else {
            throw new BpmsException(
                String.format("Couldn't complete task %s in process engine application. Application responded with code: %s",
                        task.getId(),
                        response.getStatusCodeValue()));
        }
    }

    @Override
    public TaskOutDto updateTask(UUID taskId, TaskInDto taskInDto){
        Task task = getTask(taskId);
        checkIfStatusValid(task);
        return updateTaskReturnDto(taskInDto, task);
    }

    @Override
    public TaskOutDto updateByProcessEngineTaskId(String processEngineTaskId, TaskInDto taskInDto) {
        Task task = taskRepository.findByProcessEngineTaskId(processEngineTaskId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Task with processEngineTaskId %s is not found", processEngineTaskId)));
        checkIfStatusValid(task);
        return updateTaskReturnDto(taskInDto, task);
    }

    private TaskOutDto updateTaskReturnDto(TaskInDto taskInDto, Task task) {
        Task partialUpdateTask = new Task();
        ObjectUtils.convertObject(taskInDto, partialUpdateTask);
        Task updatedTask =  ObjectUtils.partialUpdate(task, partialUpdateTask);
        return ObjectUtils.convertObject(taskRepository.save(updatedTask), new TaskOutDto());
    }

    private Task getTask(UUID taskId) {
        return taskRepository.findById(taskId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Task with id %s is not found", taskId)));
    }

    private void checkIfStatusValid(Task task) {
        if (task.getStatus().equals(COMPLETED.toString())) {
            throw new IllegalActionException("Task is completed");
        }
        if (task.getStatus().equals(CLOSED.toString())) {
            throw new IllegalActionException("Task is closed");
        }
    }
}
