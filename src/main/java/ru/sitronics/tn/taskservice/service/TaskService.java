package ru.sitronics.tn.taskservice.service;

import ru.sitronics.tn.taskservice.dto.TaskDto;

import java.util.List;
import java.util.Map;

public interface TaskService {
    Map<String, String> getTaskTypes();
    Map<String, String> getTaskStatuses();
    TaskDto createTask(TaskDto taskDto);
    List<TaskDto> getTasks();
    void claimTask(String taskId, String userId);
    void unclaimTask(String taskId);
    void reassignByCurrentUser(String taskId, String currentUserId, String newUserId);
    void completeTask(String taskId);
}
