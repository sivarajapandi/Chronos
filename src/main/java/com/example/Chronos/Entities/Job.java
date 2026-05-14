package com.example.Chronos.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
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

    private String cronExpression;

    private int maxRetries;

    private int retryCount =0;

    private String status = "PENDING";

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

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
