package com.example.Chronos.worker;

import com.example.Chronos.Entities.Job;
import com.example.Chronos.Service.JobService;
import org.springframework.stereotype.Component;

@Component
public class JobWorker {
    private final JobService jobservice;

    public JobWorker(JobService jobservice) {
        this.jobservice = jobservice;
    }

    public void executeJob(Job job) {
        jobservice.executeJob(job);
    }


}
