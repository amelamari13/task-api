package com.amel.taskapi.repository;

import com.amel.taskapi.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaskRepository extends JpaRepository<Task, Long>{

}
