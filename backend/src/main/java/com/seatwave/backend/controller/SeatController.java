package com.seatwave.backend.controller;

import com.seatwave.backend.dto.SeatBulkCreateRequest;
import com.seatwave.backend.dto.SeatResponse;
import com.seatwave.backend.service.SeatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venues/{venueId}/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @PostMapping("/bulk")
    public ResponseEntity<List<SeatResponse>> bulkCreate(
            @PathVariable Long venueId,
            @Valid @RequestBody SeatBulkCreateRequest request) {
        return ResponseEntity.ok(seatService.bulkCreate(venueId, request));
    }

    @GetMapping
    public ResponseEntity<List<SeatResponse>> getByVenue(@PathVariable Long venueId) {
        return ResponseEntity.ok(seatService.getByVenue(venueId));
    }
}