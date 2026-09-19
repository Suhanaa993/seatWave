package com.seatwave.backend.repository;

import com.seatwave.backend.entity.EventSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventSeatRepository extends JpaRepository<EventSeat, Long> {
    List<EventSeat> findByEventId(Long eventId);
    Optional<EventSeat> findByIdAndEventId(Long id, Long eventId);
}