package com.seatwave.backend.service;

import com.seatwave.backend.dto.EventRequest;
import com.seatwave.backend.dto.EventResponse;
import com.seatwave.backend.entity.Event;
import com.seatwave.backend.entity.Venue;
import com.seatwave.backend.repository.EventRepository;
import com.seatwave.backend.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;

    public EventResponse create(EventRequest request) {
        Venue venue = venueRepository.findById(request.getVenueId())
                .orElseThrow(() -> new RuntimeException("Venue not found"));

        Event event = Event.builder()
                .venue(venue)
                .name(request.getName())
                .category(request.getCategory())
                .description(request.getDescription())
                .eventTime(request.getEventTime())
                .basePrice(request.getBasePrice())
                .build();

        return toResponse(eventRepository.save(event));
    }

    public EventResponse getById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return toResponse(event);
    }

    public Page<EventResponse> search(String city, String category, Pageable pageable) {
        Page<Event> events;
        if (city != null && !city.isBlank()) {
            events = eventRepository.findByVenue_CityIgnoreCase(city, pageable);
        } else if (category != null && !category.isBlank()) {
            events = eventRepository.findByCategoryIgnoreCase(category, pageable);
        } else {
            events = eventRepository.findAll(pageable);
        }
        return events.map(this::toResponse);
    }

    public void delete(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new RuntimeException("Event not found");
        }
        eventRepository.deleteById(id);
    }

    private EventResponse toResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .category(event.getCategory())
                .description(event.getDescription())
                .eventTime(event.getEventTime())
                .basePrice(event.getBasePrice())
                .venueName(event.getVenue().getName())
                .venueCity(event.getVenue().getCity())
                .build();
    }
}