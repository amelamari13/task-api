package com.amel.taskapi.service;

import com.amel.taskapi.dto.TaskRequest;
import com.amel.taskapi.entity.Task;
import com.amel.taskapi.entity.TaskStatus;
import com.amel.taskapi.exception.TaskNotFoundException;
import com.amel.taskapi.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    public List<Task> findAll(){
        return taskRepository.findAll();
    }

    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task create(TaskRequest taskRequest){
        Task task = new Task();

        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setStatus(taskRequest.getStatus());

        task.setCreatedAt(LocalDateTime.now());

        task = this.taskRepository.save(task);

        return task;
    }

    public Task update(Long id, TaskRequest taskRequest){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setStatus(taskRequest.getStatus());

        return taskRepository.save(task);
    }

    public void delete(Long id){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        taskRepository.delete(task);
    }
}

