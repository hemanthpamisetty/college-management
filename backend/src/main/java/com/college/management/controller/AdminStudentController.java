package com.college.management.controller;

import com.college.management.dto.*;
import com.college.management.entity.*;
import com.college.management.exception.ResourceNotFoundException;
import com.college.management.repository.*;
import com.college.management.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/admin")
public class AdminStudentController {

    private final UserRepository userRepository;
    private final StudentDocumentService documentService;
    private final StudentDocumentRepository documentRepository;
    private final AttendanceRepository attendanceRepository;
    private final AttendanceService attendanceService;
    private final ResultRepository resultRepository;
    private final ResultService resultService;
    private final EventRepository eventRepository;
    private final EventService eventService;
    private final ExaminationRepository examinationRepository;
    private final ExaminationService examinationService;
    private final LibraryBookRepository libraryBookRepository;
    private final LibraryService libraryService;
    private final FeeRepository feeRepository;
    private final FeeService feeService;
    private final PasswordEncoder passwordEncoder;

    public AdminStudentController(UserRepository userRepository, StudentDocumentService documentService,
                                  StudentDocumentRepository documentRepository, AttendanceRepository attendanceRepository,
                                  AttendanceService attendanceService, ResultRepository resultRepository,
                                  ResultService resultService, EventRepository eventRepository,
                                  EventService eventService, ExaminationRepository examinationRepository,
                                  ExaminationService examinationService, LibraryBookRepository libraryBookRepository,
                                  LibraryService libraryService, FeeRepository feeRepository,
                                  FeeService feeService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.documentService = documentService;
        this.documentRepository = documentRepository;
        this.attendanceRepository = attendanceRepository;
        this.attendanceService = attendanceService;
        this.resultRepository = resultRepository;
        this.resultService = resultService;
        this.eventRepository = eventRepository;
        this.eventService = eventService;
        this.examinationRepository = examinationRepository;
        this.examinationService = examinationService;
        this.libraryBookRepository = libraryBookRepository;
        this.libraryService = libraryService;
        this.feeRepository = feeRepository;
        this.feeService = feeService;
        this.passwordEncoder = passwordEncoder;
    }

    // ===================== DASHBOARD STATS =====================

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStats() {
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalStudents", userRepository.findAllByRole(Role.STUDENT).size());
        stats.put("totalFaculty", userRepository.findAllByRole(Role.FACULTY).size());
        stats.put("totalAttendance", attendanceRepository.count());
        stats.put("totalResults", resultRepository.count());
        stats.put("totalExams", examinationRepository.count());
        stats.put("totalEvents", eventRepository.count());
        stats.put("totalBooks", libraryBookRepository.count());
        stats.put("totalFees", feeRepository.count());
        stats.put("totalDocuments", documentRepository.count());
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    // ===================== STUDENT MANAGEMENT =====================

    @GetMapping("/students")
    public ResponseEntity<ApiResponse<List<StudentDetailDTO>>> getAllStudents(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Integer semester,
            @RequestParam(required = false) String search) {

        List<User> students = userRepository.findAllByRole(Role.STUDENT);
        if (department != null && !department.isEmpty()) {
            students = students.stream().filter(s -> department.equalsIgnoreCase(s.getDepartment())).collect(Collectors.toList());
        }
        if (semester != null) {
            students = students.stream().filter(s -> semester.equals(s.getSemester())).collect(Collectors.toList());
        }
        if (search != null && !search.isEmpty()) {
            String lower = search.toLowerCase();
            students = students.stream().filter(s ->
                    (s.getName() != null && s.getName().toLowerCase().contains(lower)) ||
                    (s.getEmail() != null && s.getEmail().toLowerCase().contains(lower)) ||
                    (s.getRegistrationNumber() != null && s.getRegistrationNumber().toLowerCase().contains(lower))
            ).collect(Collectors.toList());
        }
        List<StudentDetailDTO> result = students.stream().map(this::toStudentDetail).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<ApiResponse<StudentDetailDTO>> getStudentDetail(@PathVariable Long id) {
        User student = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        StudentDetailDTO dto = toStudentDetail(student);
        dto.setDocuments(documentService.getDocumentsByStudent(id));
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<ApiResponse<StudentDetailDTO>> updateStudent(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        User student = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        if (updates.containsKey("name")) student.setName((String) updates.get("name"));
        if (updates.containsKey("email")) student.setEmail((String) updates.get("email"));
        if (updates.containsKey("department")) student.setDepartment((String) updates.get("department"));
        if (updates.containsKey("section")) student.setSection((String) updates.get("section"));
        if (updates.containsKey("semester")) student.setSemester((Integer) updates.get("semester"));
        if (updates.containsKey("registrationNumber")) student.setRegistrationNumber((String) updates.get("registrationNumber"));
        if (updates.containsKey("password") && updates.get("password") != null && !((String)updates.get("password")).isEmpty()) {
            student.setPassword(passwordEncoder.encode((String) updates.get("password")));
        }
        userRepository.save(student);
        return ResponseEntity.ok(ApiResponse.success("Student updated", toStudentDetail(student)));
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {
        User student = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        userRepository.delete(student);
        return ResponseEntity.ok(ApiResponse.success("Student deleted", null));
    }

    // ===================== ALL USERS (for dropdowns) =====================

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAllUsers() {
        List<Map<String, Object>> users = userRepository.findAll().stream().map(u -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", u.getId());
            m.put("name", u.getName());
            m.put("email", u.getEmail());
            m.put("role", u.getRole().name());
            m.put("department", u.getDepartment());
            m.put("semester", u.getSemester());
            return m;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    // ===================== ATTENDANCE (ALL) =====================

    @GetMapping("/attendance/all")
    public ResponseEntity<ApiResponse<List<AttendanceDTO>>> getAllAttendance() {
        List<Attendance> records = attendanceRepository.findAll();
        List<AttendanceDTO> dtos = records.stream().map(this::toAttendanceDTO).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(dtos));
    }

    // ===================== RESULTS (ALL) =====================

    @GetMapping("/results/all")
    public ResponseEntity<ApiResponse<List<ResultDTO>>> getAllResults() {
        List<Result> records = resultRepository.findAll();
        List<ResultDTO> dtos = records.stream().map(this::toResultDTO).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(dtos));
    }

    // ===================== EVENTS (ALL) =====================

    @GetMapping("/events/all")
    public ResponseEntity<ApiResponse<List<EventDTO>>> getAllEvents() {
        List<Event> records = eventRepository.findAll();
        List<EventDTO> dtos = records.stream().map(this::toEventDTO).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(dtos));
    }

    // ===================== EXAMINATIONS (ALL) =====================

    @GetMapping("/examinations/all")
    public ResponseEntity<ApiResponse<List<ExaminationDTO>>> getAllExaminations() {
        List<Examination> records = examinationRepository.findAll();
        List<ExaminationDTO> dtos = records.stream().map(this::toExaminationDTO).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(dtos));
    }

    // ===================== LIBRARY (ALL) =====================

    @GetMapping("/library/all")
    public ResponseEntity<ApiResponse<List<LibraryBookDTO>>> getAllBooks() {
        List<LibraryBook> records = libraryBookRepository.findAll();
        List<LibraryBookDTO> dtos = records.stream().map(this::toLibraryBookDTO).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(dtos));
    }

    // ===================== FEES (ALL) =====================

    @GetMapping("/fees/all")
    public ResponseEntity<ApiResponse<List<FeeDTO>>> getAllFees() {
        List<Fee> records = feeRepository.findAll();
        List<FeeDTO> dtos = records.stream().map(this::toFeeDTO).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(dtos));
    }

    // ===================== DTO MAPPERS =====================

    private StudentDetailDTO toStudentDetail(User u) {
        StudentDetailDTO dto = new StudentDetailDTO();
        dto.setId(u.getId());
        dto.setName(u.getName());
        dto.setEmail(u.getEmail());
        dto.setRole(u.getRole());
        dto.setRegistrationNumber(u.getRegistrationNumber());
        dto.setDepartment(u.getDepartment());
        dto.setSection(u.getSection());
        dto.setSemester(u.getSemester());
        dto.setDocumentCount(documentRepository.countByStudentId(u.getId()));
        return dto;
    }

    private AttendanceDTO toAttendanceDTO(Attendance a) {
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

    private ResultDTO toResultDTO(Result r) {
        ResultDTO dto = new ResultDTO();
        dto.setId(r.getId());
        dto.setStudentId(r.getStudent().getId());
        dto.setStudentName(r.getStudent().getName());
        dto.setSubjectName(r.getSubjectName());
        dto.setSubjectCode(r.getSubjectCode());
        dto.setExamType(r.getExamType());
        dto.setMarksObtained(r.getMarksObtained());
        dto.setTotalMarks(r.getTotalMarks());
        dto.setGrade(r.getGrade());
        dto.setSemester(r.getSemester());
        dto.setAcademicYear(r.getAcademicYear());
        dto.setCredits(r.getCredits());
        dto.setPercentage(r.getTotalMarks() > 0 ? (r.getMarksObtained() / r.getTotalMarks()) * 100 : 0);
        dto.setCreatedAt(r.getCreatedAt());
        return dto;
    }

    private EventDTO toEventDTO(Event e) {
        EventDTO dto = new EventDTO();
        dto.setId(e.getId());
        dto.setTitle(e.getTitle());
        dto.setDescription(e.getDescription());
        dto.setEventDate(e.getEventDate());
        dto.setLocation(e.getLocation());
        dto.setCategory(e.getCategory());
        dto.setIsActive(e.getIsActive());
        dto.setImageUrl(e.getImageUrl());
        dto.setCreatedAt(e.getCreatedAt());
        if (e.getCreatedBy() != null) {
            dto.setCreatedById(e.getCreatedBy().getId());
            dto.setCreatedByName(e.getCreatedBy().getName());
        }
        return dto;
    }

    private ExaminationDTO toExaminationDTO(Examination ex) {
        ExaminationDTO dto = new ExaminationDTO();
        dto.setId(ex.getId());
        dto.setSubjectName(ex.getSubjectName());
        dto.setSubjectCode(ex.getSubjectCode());
        dto.setExamType(ex.getExamType());
        dto.setExamDate(ex.getExamDate());
        dto.setStartTime(ex.getStartTime());
        dto.setEndTime(ex.getEndTime());
        dto.setRoom(ex.getRoom());
        dto.setDepartment(ex.getDepartment());
        dto.setSemester(ex.getSemester());
        return dto;
    }

    private LibraryBookDTO toLibraryBookDTO(LibraryBook b) {
        LibraryBookDTO dto = new LibraryBookDTO();
        dto.setId(b.getId());
        dto.setTitle(b.getTitle());
        dto.setAuthor(b.getAuthor());
        dto.setIsbn(b.getIsbn());
        dto.setCategory(b.getCategory());
        dto.setTotalCopies(b.getTotalCopies());
        dto.setAvailableCopies(b.getAvailableCopies());
        dto.setShelfLocation(b.getShelfLocation());
        return dto;
    }

    private FeeDTO toFeeDTO(Fee f) {
        FeeDTO dto = new FeeDTO();
        dto.setId(f.getId());
        dto.setStudentId(f.getStudent().getId());
        dto.setStudentName(f.getStudent().getName());
        dto.setFeeType(f.getFeeType());
        dto.setAmount(f.getAmount());
        dto.setDueDate(f.getDueDate());
        dto.setPaidDate(f.getPaidDate());
        dto.setStatus(f.getStatus());
        dto.setSemester(f.getSemester());
        dto.setAcademicYear(f.getAcademicYear());
        dto.setTransactionId(f.getTransactionId());
        return dto;
    }
}
