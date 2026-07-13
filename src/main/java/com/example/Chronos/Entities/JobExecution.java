package com.example.Chronos.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class JobExecution {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    private LocalDateTime statrtTime;

    private LocalDateTime CompletedAt;

    private String status;

    @Column(columnDefinition = "TEXT")
    private String message;

}
