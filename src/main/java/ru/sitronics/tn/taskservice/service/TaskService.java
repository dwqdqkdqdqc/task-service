package ru.sitronics.tn.taskservice.service;

import ru.sitronics.tn.taskservice.dto.TaskCountDto;
import ru.sitronics.tn.taskservice.dto.TaskInDto;
import ru.sitronics.tn.taskservice.dto.TaskOutDto;
import ru.sitronics.tn.taskservice.dto.TaskPageDto;

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
    void completeTask(UUID taskId);
    TaskOutDto updateTask(UUID taskId,TaskInDto taskDto);
}
