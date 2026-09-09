package com.amel.taskapi.service;

import com.amel.taskapi.dto.TaskRequest;
import com.amel.taskapi.entity.Task;
import com.amel.taskapi.exception.InvalidStoryPointsException;
import com.amel.taskapi.exception.TaskNotFoundException;
import com.amel.taskapi.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class TaskService {
    private final Set<Integer> fibonacciStoryPoints = Set.of(1,2,3,5,8,13,21);

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

    public void validateStoryPoints(TaskRequest taskRequest){
        Integer storyPoints = taskRequest.getStoryPoints();
        if(storyPoints != null && !fibonacciStoryPoints.contains(storyPoints)){
            throw new InvalidStoryPointsException(storyPoints);
        }
    }

    public Task create(TaskRequest taskRequest){
        validateStoryPoints(taskRequest);

        Task task = new Task();

        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setStatus(taskRequest.getStatus());

        task.setCreatedAt(LocalDateTime.now());

        task.setStoryPoints(taskRequest.getStoryPoints());

        task = this.taskRepository.save(task);

        return task;
    }

    public Task update(Long id, TaskRequest taskRequest){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        validateStoryPoints(taskRequest);

        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setStatus(taskRequest.getStatus());

        task.setStoryPoints(taskRequest.getStoryPoints());

        return taskRepository.save(task);
    }

    public void delete(Long id){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        taskRepository.delete(task);
    }
}

