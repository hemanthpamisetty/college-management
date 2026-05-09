package com.college.management.service;

import com.college.management.dto.EventDTO;
import com.college.management.entity.Event;
import com.college.management.entity.User;
import com.college.management.exception.ResourceNotFoundException;
import com.college.management.repository.EventRepository;
import com.college.management.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public EventDTO createEvent(EventDTO dto) {
        Event event = new Event();
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setEventDate(dto.getEventDate());
        event.setLocation(dto.getLocation());
        event.setCategory(dto.getCategory());
        event.setImageUrl(dto.getImageUrl());
        event.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);

        if (dto.getCreatedById() != null) {
            User creator = userRepository.findById(dto.getCreatedById()).orElse(null);
            event.setCreatedBy(creator);
        }

        Event saved = eventRepository.save(event);
        log.info("Event created: {}", dto.getTitle());
        return toDTO(saved);
    }

    public List<EventDTO> getAllActiveEvents() {
        return eventRepository.findByIsActiveTrueOrderByEventDateAsc().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<EventDTO> getUpcomingEvents() {
        return eventRepository.findByEventDateAfterAndIsActiveTrue(LocalDate.now()).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public EventDTO getById(Long id) {
        return toDTO(eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id)));
    }

    @Transactional
    public EventDTO updateEvent(Long id, EventDTO dto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));

        if (dto.getTitle() != null) event.setTitle(dto.getTitle());
        if (dto.getDescription() != null) event.setDescription(dto.getDescription());
        if (dto.getEventDate() != null) event.setEventDate(dto.getEventDate());
        if (dto.getLocation() != null) event.setLocation(dto.getLocation());
        if (dto.getCategory() != null) event.setCategory(dto.getCategory());
        if (dto.getImageUrl() != null) event.setImageUrl(dto.getImageUrl());
        if (dto.getIsActive() != null) event.setIsActive(dto.getIsActive());

        return toDTO(eventRepository.save(event));
    }

    @Transactional
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
    }

    private EventDTO toDTO(Event e) {
        EventDTO dto = new EventDTO();
        dto.setId(e.getId());
        dto.setTitle(e.getTitle());
        dto.setDescription(e.getDescription());
        dto.setEventDate(e.getEventDate());
        dto.setLocation(e.getLocation());
        dto.setCategory(e.getCategory());
        dto.setImageUrl(e.getImageUrl());
        dto.setIsActive(e.getIsActive());
        dto.setCreatedAt(e.getCreatedAt());
        if (e.getCreatedBy() != null) {
            dto.setCreatedById(e.getCreatedBy().getId());
            dto.setCreatedByName(e.getCreatedBy().getName());
        }
        return dto;
    }
}
