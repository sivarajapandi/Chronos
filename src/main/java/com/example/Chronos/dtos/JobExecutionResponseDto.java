package com.example.Chronos.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class JobExecutionResponseDto {

    public UUID jobExecutionId;

    public LocalDateTime startTime;

    public LocalDateTime endTime;

    public String status;

    public String message;

}
