package com.seatwave.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class SeatBulkCreateRequest {
    @NotEmpty
    private List<String> rows;          // e.g. ["A", "B", "C"]

    @NotNull
    @Positive
    private Integer seatsPerRow;         // e.g. 10 -> seats 1 through 10 in each row

    @NotBlank
    private String seatType;             // e.g. "REGULAR", "PREMIUM", "VIP"
}