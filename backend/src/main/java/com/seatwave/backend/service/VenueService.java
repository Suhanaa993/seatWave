package com.seatwave.backend.service;

import com.seatwave.backend.dto.VenueRequest;
import com.seatwave.backend.dto.VenueResponse;
import com.seatwave.backend.entity.Venue;
import com.seatwave.backend.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VenueService {

    private final VenueRepository venueRepository;

    public VenueResponse create(VenueRequest request) {
        Venue venue = Venue.builder()
                .name(request.getName())
                .address(request.getAddress())
                .city(request.getCity())
                .totalCapacity(request.getTotalCapacity())
                .build();

        Venue saved = venueRepository.save(venue);
        return toResponse(saved);
    }

    public VenueResponse getById(Long id) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venue not found"));
        return toResponse(venue);
    }

    public Page<VenueResponse> getAll(String city, Pageable pageable) {
        Page<Venue> venues = (city != null && !city.isBlank())
                ? venueRepository.findByCityIgnoreCase(city, pageable)
                : venueRepository.findAll(pageable);

        return venues.map(this::toResponse);
    }

    public VenueResponse update(Long id, VenueRequest request) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venue not found"));

        venue.setName(request.getName());
        venue.setAddress(request.getAddress());
        venue.setCity(request.getCity());
        venue.setTotalCapacity(request.getTotalCapacity());

        return toResponse(venueRepository.save(venue));
    }

    public void delete(Long id) {
        if (!venueRepository.existsById(id)) {
            throw new RuntimeException("Venue not found");
        }
        venueRepository.deleteById(id);
    }

    private VenueResponse toResponse(Venue venue) {
        return VenueResponse.builder()
                .id(venue.getId())
                .name(venue.getName())
                .address(venue.getAddress())
                .city(venue.getCity())
                .totalCapacity(venue.getTotalCapacity())
                .build();
    }
}