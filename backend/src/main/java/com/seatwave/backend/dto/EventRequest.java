package com.seatwave.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class EventRequest {
    @NotNull
    private Long venueId;

    @NotBlank
    private String name;

    @NotBlank
    private String category;

    private String description;

    @NotNull
    @Future
    private LocalDateTime eventTime;

    @NotNull
    @Positive
    private BigDecimal basePrice;
}