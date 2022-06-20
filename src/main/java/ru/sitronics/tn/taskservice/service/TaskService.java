package ru.sitronics.tn.taskservice.service;

import ru.sitronics.tn.taskservice.dto.TaskCountDto;
import ru.sitronics.tn.taskservice.dto.TaskDto;
import ru.sitronics.tn.taskservice.dto.TaskPageDto;

import java.util.Map;

public interface TaskService {
    Map<String, String> getTaskTypes();
    Map<String, String> getTaskStatuses();
    TaskDto createTask(TaskDto taskDto);
    TaskPageDto getTasks(String filter, Integer page, Integer size, String sort, String fields);
    void claimTask(String taskId, String userId);
    void unclaimTask(String taskId);
    void reassignByCurrentUser(String taskId, String currentUserId, String newUserId);
    TaskCountDto countByAssigneeAndReadByAssignee(String assignee, boolean readByAssignee);
    void completeTask(String taskId);
}
