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

        List<Job> jobs = jobRepository.findDueJobs(LocalDateTime.now());

        for (Job job : jobs) {
            jobService.processJob(job);
        }

    }



}
