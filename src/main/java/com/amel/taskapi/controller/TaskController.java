package com.amel.taskapi.controller;

import com.amel.taskapi.dto.TaskRequest;
import com.amel.taskapi.entity.Task;
import com.amel.taskapi.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> findAll(){
        return taskService.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Task create(@Valid @RequestBody TaskRequest taskRequest){
        return taskService.create(taskRequest);
    }

    @GetMapping("/{id}")
    public Task findById(@PathVariable Long id){
        return taskService.findById(id);
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable Long id, @Valid @RequestBody TaskRequest taskRequest){
        return taskService.update(id, taskRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        taskService.delete(id);
    }
}
