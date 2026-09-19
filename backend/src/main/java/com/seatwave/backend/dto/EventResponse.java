package com.seatwave.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class EventResponse {
    private Long id;
    private String name;
    private String category;
    private String description;
    private LocalDateTime eventTime;
    private BigDecimal basePrice;
    private String venueName;
    private String venueCity;
}