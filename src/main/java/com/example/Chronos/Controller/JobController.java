package com.example.Chronos.Controller;

import com.example.Chronos.Service.JobService;
import com.example.Chronos.dtos.JobRequestDto;
import com.example.Chronos.dtos.JobResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




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

}
