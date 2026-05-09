package com.college.management.service;

import com.college.management.dto.AttendanceDTO;
import com.college.management.entity.Attendance;
import com.college.management.entity.AttendanceStatus;
import com.college.management.entity.User;
import com.college.management.exception.ResourceNotFoundException;
import com.college.management.repository.AttendanceRepository;
import com.college.management.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    private static final Logger log = LoggerFactory.getLogger(AttendanceService.class);
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    public AttendanceService(AttendanceRepository attendanceRepository, UserRepository userRepository) {
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AttendanceDTO markAttendance(AttendanceDTO dto) {
        User student = userRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + dto.getStudentId()));

        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setSubjectName(dto.getSubjectName());
        attendance.setSubjectCode(dto.getSubjectCode());
        attendance.setDate(dto.getDate());
        attendance.setStatus(dto.getStatus());
        attendance.setDepartment(dto.getDepartment());
        attendance.setSection(dto.getSection());
        attendance.setSemester(dto.getSemester());

        if (dto.getMarkedById() != null) {
            User markedBy = userRepository.findById(dto.getMarkedById()).orElse(null);
            attendance.setMarkedBy(markedBy);
        }

        Attendance saved = attendanceRepository.save(attendance);
        log.info("Attendance marked for student {} on {}", student.getName(), dto.getDate());
        return toDTO(saved);
    }

    public List<AttendanceDTO> getByStudentId(Long studentId) {
        return attendanceRepository.findByStudentId(studentId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<AttendanceDTO> getByStudentAndDateRange(Long studentId, LocalDate start, LocalDate end) {
        return attendanceRepository.findByStudentIdAndDateBetween(studentId, start, end).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<AttendanceDTO> getByDepartmentAndSemester(String department, Integer semester) {
        return attendanceRepository.findByDepartmentAndSemester(department, semester).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Map<String, Object> getAttendancePercentage(Long studentId, String subjectCode) {
        long total = attendanceRepository.countByStudentIdAndSubjectCode(studentId, subjectCode);
        long present = attendanceRepository.countByStudentIdAndSubjectCodeAndStatus(studentId, subjectCode, AttendanceStatus.PRESENT);
        double percentage = total > 0 ? (double) present / total * 100.0 : 0.0;

        Map<String, Object> result = new HashMap<>();
        result.put("studentId", studentId);
        result.put("subjectCode", subjectCode);
        result.put("totalClasses", total);
        result.put("present", present);
        result.put("absent", total - present);
        result.put("percentage", Math.round(percentage * 100.0) / 100.0);
        return result;
    }

    @Transactional
    public AttendanceDTO updateAttendance(Long id, AttendanceDTO dto) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found with id: " + id));

        if (dto.getStatus() != null) attendance.setStatus(dto.getStatus());
        if (dto.getSubjectName() != null) attendance.setSubjectName(dto.getSubjectName());
        if (dto.getSubjectCode() != null) attendance.setSubjectCode(dto.getSubjectCode());
        if (dto.getDate() != null) attendance.setDate(dto.getDate());

        return toDTO(attendanceRepository.save(attendance));
    }

    @Transactional
    public void deleteAttendance(Long id) {
        if (!attendanceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Attendance record not found with id: " + id);
        }
        attendanceRepository.deleteById(id);
        log.info("Attendance record deleted: {}", id);
    }

    private AttendanceDTO toDTO(Attendance a) {
        AttendanceDTO dto = new AttendanceDTO();
        dto.setId(a.getId());
        dto.setStudentId(a.getStudent().getId());
        dto.setStudentName(a.getStudent().getName());
        dto.setSubjectName(a.getSubjectName());
        dto.setSubjectCode(a.getSubjectCode());
        dto.setDate(a.getDate());
        dto.setStatus(a.getStatus());
        dto.setDepartment(a.getDepartment());
        dto.setSection(a.getSection());
        dto.setSemester(a.getSemester());
        dto.setCreatedAt(a.getCreatedAt());
        if (a.getMarkedBy() != null) {
            dto.setMarkedById(a.getMarkedBy().getId());
            dto.setMarkedByName(a.getMarkedBy().getName());
        }
        return dto;
    }
}
