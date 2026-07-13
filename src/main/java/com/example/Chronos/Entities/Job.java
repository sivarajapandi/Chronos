package com.example.Chronos.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Job {


    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String type;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private String scheduleType;

    private LocalDateTime runAt;

    //what is cronExpression and why it is used for the
    private String cronExpression;

    private int maxRetries;

    private int retryCount =0;

    private String status = "PENDING";

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "job")
    private List<JobExecution> executions;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


}
