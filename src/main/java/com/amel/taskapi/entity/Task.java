package com.amel.taskapi.entity;

import jakarta.persistence.*;
//Java Persistence API
// Le fait de faire en sorte que les données survivent au-delà de l'objet Java qui existe en mémoire
// Communiquer entre objet Java et base de données SQL
// JPA = la norme/les règles, c'est une spécification : utilise les annotations
// Hibernate implémente JPA
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Peut être null avant insertion contrairement à long qui a une valeur par défaut à 0

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private Integer storyPoints;

    public Task(){

    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public TaskStatus getStatus(){
        return status;
    }

    public void setStatus(TaskStatus status){
        this.status = status;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public Integer getStoryPoints() { return storyPoints; }

    public void setStoryPoints(Integer storyPoints) { this.storyPoints = storyPoints; }

    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }
}
