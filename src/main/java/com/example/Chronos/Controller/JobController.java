package com.example.Chronos.Controller;

import com.example.Chronos.Service.JobService;
import com.example.Chronos.dtos.JobExecutionResponseDto;
import com.example.Chronos.dtos.JobRequestDto;
import com.example.Chronos.dtos.JobResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private final JobService jobService;

    public JobController(JobService jobservice){
        this.jobService = jobservice;
    }

    //Contoller for creating the jobs
    // This endpoint accepts a JobRequestDto, validates it, and creates a new job using the Jobservice.
    @PostMapping
    public ResponseEntity<JobResponseDto> createJob(@Valid @RequestBody JobRequestDto request) {
        JobResponseDto response = jobService.createJob(request);
        return ResponseEntity.ok(response);


    }

    @GetMapping
    public ResponseEntity<List<JobResponseDto>> getAllJobs(){
        List<JobResponseDto> allJobs=jobService.getAllJobs();
        return ResponseEntity.ok(allJobs);

        
    }
    @GetMapping("/{id}")
    public ResponseEntity<JobResponseDto> getJobById(@Valid @PathVariable UUID id){

        JobResponseDto job=jobService.getJobById(id);

        return ResponseEntity.ok(job);

    }

    @GetMapping("/{id}/executions")
    public ResponseEntity<List<JobExecutionResponseDto>> getJobExecutions(@Valid @PathVariable UUID id){
        List<JobExecutionResponseDto> jobExecutionList=jobService.getAllJobExecutionsForId(id);

        return ResponseEntity.ok(jobExecutionList);



    }

}
