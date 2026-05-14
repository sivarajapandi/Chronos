package com.example.Chronos.handler;

public interface JobHandler {
    //identify the job type
    String getJobType();

    //validate payload before saving job
    void validate(String payload);

    //Execute the job logic
    void execute(String payload);
}
