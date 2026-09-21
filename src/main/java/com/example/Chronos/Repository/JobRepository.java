package com.example.Chronos.Repository;

import com.example.Chronos.Entities.Job;
import com.example.Chronos.dtos.JobResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {


    @Query("SELECT j FROM Job j WHERE j.status in ('PENDING','RETRY_PENDING') AND j.runAt <= :now")
    List<Job> findDueJobs(LocalDateTime now);

}
