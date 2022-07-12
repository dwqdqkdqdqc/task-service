package ru.sitronics.tn.taskservice.service;

import ru.sitronics.tn.taskservice.dto.*;

import java.util.Map;
import java.util.UUID;

public interface TaskService {
    Map<String, String> getTaskTypes();
    Map<String, String> getTaskStatuses();
    TaskOutDto createTask(TaskInDto taskInDto);
    TaskPageDto getTasks(String filter, Integer page, Integer size, String sort, String fields);
    void claimTask(UUID taskId, String userId);
    void unclaimTask(UUID taskId);
    void reassignByCurrentUser(UUID taskId, String currentUserId, String newUserId);
    TaskCountDto countByAssigneeAndReadByAssignee(String assignee, boolean readByAssignee);
    void completeTask(UUID taskId, CompleteTaskDto completeTaskDto);
    TaskOutDto updateTask(UUID taskId,TaskInDto taskDto);
}
