package com.college.management.controller;

import com.college.management.entity.Timetable;
import com.college.management.entity.User;
import com.college.management.repository.UserRepository;
import com.college.management.service.TimetableService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/timetable")
public class TimetableController {

    private final TimetableService timetableService;
    private final UserRepository userRepository;

    public TimetableController(TimetableService timetableService, UserRepository userRepository) {
        this.timetableService = timetableService;
        this.userRepository = userRepository;
    }

    // Get timetable with optional filters
    @GetMapping
    public ResponseEntity<List<Timetable>> getTimetable(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String section,
            @RequestParam(required = false) Integer semester) {
        List<Timetable> timetables = timetableService.getTimetableFiltered(department, section, semester);
        return ResponseEntity.ok(timetables);
    }

    // Get timetable for the logged-in student's department/section/semester
    @GetMapping("/my")
    public ResponseEntity<List<Timetable>> getMyTimetable(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getDepartment() == null) {
            // If student has no department set, return empty
            return ResponseEntity.ok(List.of());
        }

        List<Timetable> timetables = timetableService.getTimetableFiltered(
                user.getDepartment(), user.getSection(), user.getSemester());
        return ResponseEntity.ok(timetables);
    }

    // Get current user info (for frontend to know role/department)
    @GetMapping("/userinfo")
    public ResponseEntity<UserInfoResponse> getUserInfo(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(new UserInfoResponse(
                user.getName(), user.getEmail(), user.getRole().name(),
                user.getDepartment(), user.getSection(), user.getSemester()));
    }

    // Add new timetable entry (Admin/Faculty only)
    @PostMapping
    public ResponseEntity<Timetable> addTimetable(@RequestBody Timetable timetable) {
        Timetable saved = timetableService.addTimetableEntry(timetable);
        return ResponseEntity.ok(saved);
    }

    // Update timetable entry
    @PutMapping("/{id}")
    public ResponseEntity<Timetable> updateTimetable(@PathVariable Long id, @RequestBody Timetable timetable) {
        Timetable updated = timetableService.updateTimetable(id, timetable);
        return ResponseEntity.ok(updated);
    }

    // Delete timetable entry
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimetable(@PathVariable Long id) {
        timetableService.deleteTimetable(id);
        return ResponseEntity.noContent().build();
    }

    // Simple inner class for user info response
    public static class UserInfoResponse {
        private String name;
        private String email;
        private String role;
        private String department;
        private String section;
        private Integer semester;

        public UserInfoResponse(String name, String email, String role, String department, String section, Integer semester) {
            this.name = name;
            this.email = email;
            this.role = role;
            this.department = department;
            this.section = section;
            this.semester = semester;
        }

        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
        public String getDepartment() { return department; }
        public String getSection() { return section; }
        public Integer getSemester() { return semester; }
    }
}
