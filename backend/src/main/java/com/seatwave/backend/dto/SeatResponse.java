package com.seatwave.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SeatResponse {
    private Long id;
    private String seatRow;
    private Integer seatNumber;
    private String seatType;
}