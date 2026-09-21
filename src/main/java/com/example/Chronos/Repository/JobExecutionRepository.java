package com.example.Chronos.Repository;

import com.example.Chronos.Entities.JobExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface JobExecutionRepository extends JpaRepository<JobExecution, UUID> {

    
    List<JobExecution> findByJob_Id(UUID jobId);
    
}
