package com.example.Chronos.handler;

import org.springframework.stereotype.Component;

@Component
public class FailureHandler implements JobHandler{

    @Override
    public String getJobType(){
        return "failureHandler";
    }

    @Override
    public void validate(String payload){

    }
    @Override
    public void execute(String payload){
        throw new RuntimeException("Job Execution Failed for Reason: ");
    }

    

}
