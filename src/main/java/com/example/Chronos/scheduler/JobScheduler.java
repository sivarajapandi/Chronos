package com.example.Chronos.scheduler;

import com.example.Chronos.Entities.Job;
import com.example.Chronos.Repository.JobRepository;
import com.example.Chronos.Service.JobService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class JobScheduler {
    private final JobRepository jobRepository;

    private final JobService jobService;

    public JobScheduler(JobRepository jobRepository, JobService jobService) {
        this.jobRepository = jobRepository;
        this.jobService = jobService;
    }

    @Scheduled(fixedRate = 5000)
    public void pollJobs() {

        //find the jobs with pending status and also the jobs that are with retry counts

        List<Job> jobs =
                jobRepository.findDueJobs(LocalDateTime.now());

        //will it be executed at the right time ? as we are iterating and Executing
        for (Job job : jobs) {
            jobService.executeJob(job);
        }



    }



}
