package com.example.Chronos.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
public class JobResponseDto {
    private UUID jobId;

    private String status;

    private LocalDateTime runAt;




}
