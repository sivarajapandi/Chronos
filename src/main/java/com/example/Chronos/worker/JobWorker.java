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

    //it is simply delegating the job to the service, who should interact with the Handler then
    //either the JobWorker or jobService 
    public void executeJob(Job job) {
        jobservice.executeJob(job);
    }

    


}
