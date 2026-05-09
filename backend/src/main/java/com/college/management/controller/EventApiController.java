package com.college.management.controller;

import com.college.management.dto.ApiResponse;
import com.college.management.dto.EventDTO;
import com.college.management.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/events")
public class EventApiController {

    private final EventService eventService;

    public EventApiController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EventDTO>> create(@Valid @RequestBody EventDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Event created", eventService.createEvent(dto)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EventDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(eventService.getAllActiveEvents()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(eventService.getById(id)));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponse<List<EventDTO>>> getUpcoming() {
        return ResponseEntity.ok(ApiResponse.success(eventService.getUpcomingEvents()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EventDTO>> update(@PathVariable Long id, @RequestBody EventDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Event updated", eventService.updateEvent(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok(ApiResponse.success("Event deleted", null));
    }
}
