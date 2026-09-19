package com.seatwave.backend.repository;

import com.seatwave.backend.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VenueRepository extends JpaRepository<Venue, Long> {
    Page<Venue> findByCityIgnoreCase(String city, Pageable pageable);
}