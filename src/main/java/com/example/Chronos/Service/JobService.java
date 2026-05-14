package com.example.Chronos.Service;

import com.example.Chronos.Entities.Job;
import com.example.Chronos.Repository.JobRepository;
import com.example.Chronos.dtos.JobRequestDto;
import com.example.Chronos.dtos.JobResponseDto;
import com.example.Chronos.handler.JobHandler;
import com.example.Chronos.handler.JobHandlerRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.time.LocalDateTime;

@Service
public class JobService {


    private final JobRepository jobRepository;


    private final JobHandlerRegistry handlerRegistry;


    private final ObjectMapper objectMapper;

    public JobService(JobRepository jobRepository, JobHandlerRegistry handlerRegistry, ObjectMapper objectMapper) {
        this.jobRepository = jobRepository;
        this.handlerRegistry = handlerRegistry;
        this.objectMapper = objectMapper;
    }



    public JobResponseDto createJob(JobRequestDto request) {

        JobHandler handler = handlerRegistry.getHandler(request.getType());

        String payloadJson;

        try{
            payloadJson = objectMapper.writeValueAsString(request.getPayload());
        }catch (Exception e){
            throw new RuntimeException("Failed to serialize payload", e);
        }


        handler.validate(payloadJson);

        // 4. Validate schedule
        if ("ONE_TIME".equalsIgnoreCase(request.getScheduleType())) {

            if (request.getRunAt() == null) {
                throw new IllegalArgumentException("runAt is required for ONE_TIME jobs");
            }

            if (request.getRunAt().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("runAt cannot be in the past");
            }

        } else if ("RECURRING".equalsIgnoreCase(request.getScheduleType())) {

            if (request.getCronExpression() == null) {
                throw new IllegalArgumentException("cronExpression is required for RECURRING jobs");
            }

        } else {
            throw new IllegalArgumentException("Invalid schedule type");
        }

        Job job=new Job();

        job.setName(request.getName());
        job.setType(request.getType());
        job.setPayload(payloadJson);
        job.setScheduleType(request.getScheduleType());
        job.setRunAt(request.getRunAt());
        job.setCronExpression(request.getCronExpression());
        job.setMaxRetries(request.getMaxRetries());
        job.setRetryCount(0);
        job.setStatus("PENDING");

        Job savedJob =jobRepository.save(job);

        return JobResponseDto.builder()
                .jobId(savedJob.getId())
                .status(savedJob.getStatus())
                .runAt(savedJob.getRunAt())
                .build();


    }
    public void processJob(Job job){
        //mark the job as running
        job.setStatus("RUNNING");
        jobRepository.save(job);


        try{
            // 2. Get handler based on type
            JobHandler jobHandler = handlerRegistry.getHandler(job.getType());

            // 3. Execute job
            jobHandler.execute(job.getPayload());

            // 4. Mark SUCCESS
            job.setStatus("SUCCESS");
        }catch(Exception e) {
            // 5. Handle failure
            job.setStatus("FAILED");

            // (optional log)
            System.out.println("Job failed: " + job.getId() + " Reason: " + e.getMessage());
        }
        // 6. Save final state
        jobRepository.save(job);

    }

    public void executeJob(Job job) {

    }
}
