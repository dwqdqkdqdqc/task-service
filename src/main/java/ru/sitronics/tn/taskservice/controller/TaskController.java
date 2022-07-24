package ru.sitronics.tn.taskservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.sitronics.tn.taskservice.dto.*;
import ru.sitronics.tn.taskservice.service.TaskService;

import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/task", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private static final String NEW_TASK_LOG = "New task was created id: {}";
    private static final String TASK_CLAIMED_LOG = "Task was claimed id: {}";
    private static final String TASK_UNCLAIMED_LOG = "Task was unclaimed id: {}";
    private static final String TASK_REASSIGNED_LOG = "Task was reassigned id: {}";
    private static final String TASK_COMPLETED_LOG = "Task was completed id: {}";

    private final TaskService taskService;

    @InitBinder    /* Converts empty strings into null when a form is submitted */
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/types")
    public ResponseEntity<Map<String, String>> getTaskTypes() {
        return ResponseEntity.ok(taskService.getTaskTypes());
    }

    @GetMapping("/statuses")
    public ResponseEntity<Map<String, String>> getTaskStatuses() {
        return ResponseEntity.ok(taskService.getTaskStatuses());
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskOutDto> createTask(@Valid @RequestBody TaskInDto taskInDto) {
        final TaskOutDto response = taskService.createTask(taskInDto);
        logger.info(NEW_TASK_LOG, response.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Returns a list of tasks and sorted/filtered based on the query parameters")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskPageDto> getTasks(@RequestParam(value = "filter", required = false) String filter,
                                                @RequestParam(value = "page", required = false) Integer page,
                                                @RequestParam(value = "size", required = false) Integer size,
                                                @RequestParam(value = "sort", required = false) String sort,
                                                @RequestParam(value = "fields", required = false) String fields) {
        return ResponseEntity.ok(taskService.getTasks(filter, page, size, sort, fields));
    }

    @PostMapping("/{taskId}/claim")
    public ResponseEntity<Void> claimTask(@PathVariable UUID taskId,
                                          @RequestParam String userId) {
        taskService.claimTask(taskId, userId);
        logger.info(TASK_CLAIMED_LOG, taskId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{taskId}/unclaim")
    public ResponseEntity<Void> unclaimTask(@PathVariable UUID taskId) {
        taskService.unclaimTask(taskId);
        logger.info(TASK_UNCLAIMED_LOG, taskId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{taskId}/reassign-by-current-user")
    public ResponseEntity<Void> reassignByCurrentUser(@PathVariable UUID taskId,
                                                      @RequestParam String currentUserId,
                                                      @RequestParam String newUserId) {
        taskService.reassignByCurrentUser(taskId, currentUserId, newUserId);
        logger.info(TASK_REASSIGNED_LOG, taskId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{taskId}/complete")
    public ResponseEntity<Void> completeTask(@PathVariable UUID taskId, @RequestBody CompleteTaskDto completeTaskDto) {
        taskService.completeTask(taskId, completeTaskDto);
        logger.info(TASK_COMPLETED_LOG, taskId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/count")
    public ResponseEntity<TaskCountDto> getTaskCountDto(@RequestParam String assignee,
                                                        @RequestParam boolean readByAssignee){
        return ResponseEntity.ok(taskService.countByAssigneeAndReadByAssignee(assignee,readByAssignee));
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskOutDto> updateTask(@PathVariable UUID taskId,
                                                 @RequestBody TaskInDto taskInDto){
        return ResponseEntity.ok(taskService.updateTask(taskId,taskInDto));
    }

    @PatchMapping("/update-by-process-engine-id/{processEngineTaskId}")
    public ResponseEntity<TaskOutDto> updateByProcessEngineTaskId(@PathVariable String processEngineTaskId,
                                                 @RequestBody TaskInDto taskInDto){
        return ResponseEntity.ok(taskService.updateByProcessEngineTaskId(processEngineTaskId,taskInDto));
    }
}
