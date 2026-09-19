package com.seatwave.backend.repository;

import com.seatwave.backend.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByCategoryIgnoreCase(String category, Pageable pageable);
    Page<Event> findByVenue_CityIgnoreCase(String city, Pageable pageable);
}