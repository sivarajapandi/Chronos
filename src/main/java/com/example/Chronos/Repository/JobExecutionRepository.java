package com.example.Chronos.Repository;

import com.example.Chronos.Entities.JobExecution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobExecutionRepository extends JpaRepository<JobExecution, UUID> {
    
}
