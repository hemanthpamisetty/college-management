package com.college.management.repository;

import com.college.management.entity.Event;
import com.college.management.entity.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByEventDateAfterAndIsActiveTrue(LocalDate date);
    List<Event> findByCategoryAndIsActiveTrue(EventCategory category);
    List<Event> findByIsActiveTrueOrderByEventDateAsc();
    List<Event> findByEventDateBetweenAndIsActiveTrue(LocalDate start, LocalDate end);
}
