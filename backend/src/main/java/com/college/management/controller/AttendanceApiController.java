package com.college.management.controller;

import com.college.management.dto.ApiResponse;
import com.college.management.dto.AttendanceDTO;
import com.college.management.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/attendance")
public class AttendanceApiController {

    private final AttendanceService attendanceService;

    public AttendanceApiController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AttendanceDTO>> markAttendance(@Valid @RequestBody AttendanceDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Attendance marked", attendanceService.markAttendance(dto)));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<AttendanceDTO>>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.getByStudentId(studentId)));
    }

    @GetMapping("/student/{studentId}/range")
    public ResponseEntity<ApiResponse<List<AttendanceDTO>>> getByStudentAndRange(
            @PathVariable Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.getByStudentAndDateRange(studentId, start, end)));
    }

    @GetMapping("/department")
    public ResponseEntity<ApiResponse<List<AttendanceDTO>>> getByDeptAndSem(
            @RequestParam String department, @RequestParam Integer semester) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.getByDepartmentAndSemester(department, semester)));
    }

    @GetMapping("/percentage")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPercentage(
            @RequestParam Long studentId, @RequestParam String subjectCode) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.getAttendancePercentage(studentId, subjectCode)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AttendanceDTO>> update(@PathVariable Long id, @RequestBody AttendanceDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Attendance updated", attendanceService.updateAttendance(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        attendanceService.deleteAttendance(id);
        return ResponseEntity.ok(ApiResponse.success("Attendance deleted", null));
    }
}
