package com.example.Chronos.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.time.LocalDateTime;

@Data
public class JobRequestDto {

    @NotBlank(message = "Job name is required")
    private String name;

    @NotBlank(message = "Job type is required")
    private String type;

    @NotNull(message = "Payload is required")
    private Object payload;

    @NotBlank(message = "Schedule type is required")
    private String scheduleType;

    private LocalDateTime runAt;

    private String cronExpression;

    @Min(value = 0, message = "Max retries must be a non-negative integer")
    private int maxRetries;


}
