package com.seatwave.backend.service;

import com.seatwave.backend.dto.SeatBulkCreateRequest;
import com.seatwave.backend.dto.SeatResponse;
import com.seatwave.backend.entity.Seat;
import com.seatwave.backend.entity.Venue;
import com.seatwave.backend.repository.SeatRepository;
import com.seatwave.backend.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final VenueRepository venueRepository;

    public List<SeatResponse> bulkCreate(Long venueId, SeatBulkCreateRequest request) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue not found"));

        List<Seat> seatsToCreate = new ArrayList<>();

        for (String row : request.getRows()) {
            for (int seatNum = 1; seatNum <= request.getSeatsPerRow(); seatNum++) {
                seatsToCreate.add(
                        Seat.builder()
                                .venue(venue)
                                .seatRow(row)
                                .seatNumber(seatNum)
                                .seatType(request.getSeatType())
                                .build()
                );
            }
        }

        List<Seat> saved = seatRepository.saveAll(seatsToCreate);

        return saved.stream()
                .map(this::toResponse)
                .toList();
    }

    public List<SeatResponse> getByVenue(Long venueId) {
        return seatRepository.findByVenueId(venueId).stream()
                .map(this::toResponse)
                .toList();
    }

    private SeatResponse toResponse(Seat seat) {
        return SeatResponse.builder()
                .id(seat.getId())
                .seatRow(seat.getSeatRow())
                .seatNumber(seat.getSeatNumber())
                .seatType(seat.getSeatType())
                .build();
    }
}