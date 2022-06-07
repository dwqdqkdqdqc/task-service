package ru.sitronics.tn.taskservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.sitronics.tn.taskservice.dto.TaskDto;
import ru.sitronics.tn.taskservice.exception.BpmEngineException;
import ru.sitronics.tn.taskservice.exception.IllegalActionException;
import ru.sitronics.tn.taskservice.exception.ResourceNotFoundException;
import ru.sitronics.tn.taskservice.model.Task;
import ru.sitronics.tn.taskservice.model.TaskStatus;
import ru.sitronics.tn.taskservice.model.TaskType;
import ru.sitronics.tn.taskservice.repository.TaskRepository;
import ru.sitronics.tn.taskservice.util.CustomRestClient;
import ru.sitronics.tn.taskservice.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static ru.sitronics.tn.taskservice.model.TaskStatus.*;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final CustomRestClient customRestClient;
    private final TaskRepository taskRepository;

    @Override
    public Map<TaskType, String> getTaskTypes() {
        Map<TaskType, String> m = new EnumMap<>(TaskType.class);
        Arrays.asList(TaskType.values()).forEach(el -> m.put(el, el.getDisplayValue()));
        return m;
    }

    @Override
    public Map<TaskStatus, String> getTaskStatuses() {
        Map<TaskStatus, String> m = new EnumMap<>(TaskStatus.class);
        Arrays.asList(TaskStatus.values()).forEach(el -> m.put(el, el.getDisplayValue()));
        return m;
    }

    @Override
    public TaskDto createTask(TaskDto taskDto) {
        Task task = ObjectUtils.convertObject(taskDto, new Task());
        return ObjectUtils.convertObject(taskRepository.save(task), new TaskDto());
    }

    @Override
    public List<TaskDto> getTasks() {
        return taskRepository.findAll().stream()
                .map(task -> {
                    TaskDto taskDto = new TaskDto();
                    BeanUtils.copyProperties(task, taskDto);
                    return taskDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void claimTask(String taskId, String userId) {
        Task task = taskRepository.findById(Long.valueOf(taskId)).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Task with id %s is not found", taskId)));
        if (!StringUtils.hasText(task.getAssignee())) {
            task.setAssignee(userId);
            task.setStatus(IN_PROGRESS);
            taskRepository.save(task);
        } else {
            throw new IllegalActionException(String.format("Task with id %s is already assigned", taskId));
        }
    }

    @Override
    @Transactional
    public void unclaimTask(String taskId) {
        Task task = taskRepository.findById(Long.valueOf(taskId)).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Task with id %s is not found", taskId)));
        task.setAssignee(null);
        task.setStatus(PENDING);
        taskRepository.save(task);
    }

    @Override
    @Transactional
    public void reassignByCurrentUser(String taskId, String currentUserId, String newUserId) {
        Task task = taskRepository.findById(Long.valueOf(taskId)).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Task with id %s is not found", taskId)));
        if (Objects.equals(task.getAssignee(), currentUserId) && StringUtils.hasText(currentUserId)) {
            task.setAssignee(newUserId);
            taskRepository.save(task);
        } else {
            throw new IllegalActionException(String.format("Task %s can be reassigned only by current assignee", taskId));
        }
    }

    @Override
    @Transactional
    //TODO UserId validation?
    public void completeTask(String taskId) {
        Task task = taskRepository.findById(Long.valueOf(taskId)).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Task with id %s is not found", taskId)));
        String enpointUri = String.format("/task/%s/complete", task.getProcessEngineTaskId());
        ResponseEntity<Void> response = customRestClient.postJson(enpointUri, "{}", Void.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            task.setStatus(COMPLETED);
            taskRepository.save(task);
        } else {
            throw new BpmEngineException(
                String.format("Couldn't complete task %s in process engine application. Application responded with code: %s",
                        task.getId(),
                        response.getStatusCodeValue()));
        }
    }
}
