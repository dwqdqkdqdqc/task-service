package ru.sitronics.tn.taskservice.service;

import ru.sitronics.tn.taskservice.dto.TaskInDto;
import ru.sitronics.tn.taskservice.dto.TaskPageDto;

import java.util.Map;

public interface TaskService {
    Map<String, String> getTaskTypes();
    Map<String, String> getTaskStatuses();
    TaskInDto createTask(TaskInDto taskInDto);
    TaskPageDto getTasks(String filter, Integer page, Integer size, String sort, String fields);
    void claimTask(String taskId, String userId);
    void unclaimTask(String taskId);
    void reassignByCurrentUser(String taskId, String currentUserId, String newUserId);
    void completeTask(String taskId);
}
