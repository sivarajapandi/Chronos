package com.example.Chronos.Service;

import com.example.Chronos.Entities.Job;
import com.example.Chronos.Entities.JobExecution;
import com.example.Chronos.Repository.JobExecutionRepository;
import com.example.Chronos.Repository.JobRepository;
import com.example.Chronos.dtos.JobRequestDto;
import com.example.Chronos.dtos.JobResponseDto;
import com.example.Chronos.handler.JobHandler;
import com.example.Chronos.handler.JobHandlerRegistry;
import com.example.Chronos.worker.JobWorker;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.time.LocalDateTime;
import java.util.logging.Logger;

@Service
public class JobService {


    private final JobRepository jobRepository;


    private final JobHandlerRegistry handlerRegistry;


    private final ObjectMapper objectMapper;

    //why do we need this jobExeuction as a variable here, because do we need to create new object every time while executing the job
    //private final JobExecution jobExecution;

    private final JobExecutionRepository jobExecutionRepository;



    public JobService(JobRepository jobRepository, JobHandlerRegistry handlerRegistry, ObjectMapper objectMapper,JobExecutionRepository jobExecutionRepository) {
        this.jobRepository = jobRepository;
        this.handlerRegistry = handlerRegistry;
        this.objectMapper = objectMapper;
        this.jobExecutionRepository = jobExecutionRepository;
    }

    public JobResponseDto createJob(JobRequestDto request) {

        JobHandler handler = handlerRegistry.getHandler(request.getType());

        //why cant a handler do this -- is this comes under the SOLID that all the Handler related tasks should be at the Handler level itself
        String payloadJson;  //json to string and string to json -serialization and deserilaization

        try{
            payloadJson = objectMapper.writeValueAsString(request.getPayload());
            System.out.println(payloadJson);
        }catch (Exception e){
            throw new RuntimeException("Failed to serialize payload", e);
        }

        //why cant we directly pass the json in the handler service
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
        job.setRetryCount(0); //this is by default
        job.setStatus("PENDING");

        Job savedJob =jobRepository.save(job);

        return JobResponseDto.builder()
                .jobId(savedJob.getId())
                .status(savedJob.getStatus())
                .runAt(savedJob.getRunAt())
                .build();


    }

    //while executing the job we need to make an entry in the JOB Execution entity
    public void executeJob(Job job){
        //mark the job as running
        job.setStatus("RUNNING");
        jobRepository.save(job);

       JobExecution execution=new JobExecution();

       execution.setJob(job);
       execution.setStatus("RUNNING");
       execution.setStatrtTime(LocalDateTime.now());

       jobExecutionRepository.save(execution);

        try{
            // 2. Get handler based on type
            JobHandler jobHandler = handlerRegistry.getHandler(job.getType());
            // 3. Execute job //if it has any Exception we need to pass it to the worker node
            jobHandler.execute(job.getPayload());


            //are success and failure is determined by the service itself or who to determine
            // 4. Mark SUCCESS
            job.setStatus("SUCCESS");
            execution.setStatus("SUCCESS");
            execution.setCompletedAt(LocalDateTime.now());


        }catch(Exception e) {
            execution.setMessage(e.getMessage());
            if(job.getRetryCount() < job.getMaxRetries()) {
                job.setRetryCount(job.getRetryCount()+1);
                job.setStatus("RETRY_PENDING");
                job.setRunAt(LocalDateTime.now().plusMinutes(1)); //retry after 1 minute
                execution.setStatus("FAILED");
                execution.setCompletedAt(LocalDateTime.now());
            } else if(job.getRetryCount() == job.getMaxRetries()) {
                job.setStatus("FAILED");
                execution.setStatus("FAILED");
                execution.setCompletedAt(LocalDateTime.now());
            }
            System.out.println("Failed to execute job " + job.getName());
        }
        // 6. Save final state
        jobRepository.save(job);
        jobExecutionRepository.save(execution);



    }
}
