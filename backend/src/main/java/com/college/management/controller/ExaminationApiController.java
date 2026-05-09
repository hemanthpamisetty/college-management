package com.college.management.controller;

import com.college.management.dto.ApiResponse;
import com.college.management.dto.ExaminationDTO;
import com.college.management.service.ExaminationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/examinations")
public class ExaminationApiController {

    private final ExaminationService examinationService;

    public ExaminationApiController(ExaminationService examinationService) {
        this.examinationService = examinationService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ExaminationDTO>> create(@Valid @RequestBody ExaminationDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Examination created", examinationService.createExamination(dto)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExaminationDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(examinationService.getAllExaminations()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExaminationDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(examinationService.getById(id)));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponse<List<ExaminationDTO>>> getUpcoming() {
        return ResponseEntity.ok(ApiResponse.success(examinationService.getUpcomingExams()));
    }

    @GetMapping("/department")
    public ResponseEntity<ApiResponse<List<ExaminationDTO>>> getByDeptAndSem(@RequestParam String department, @RequestParam Integer semester) {
        return ResponseEntity.ok(ApiResponse.success(examinationService.getByDepartmentAndSemester(department, semester)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExaminationDTO>> update(@PathVariable Long id, @RequestBody ExaminationDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Examination updated", examinationService.updateExamination(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        examinationService.deleteExamination(id);
        return ResponseEntity.ok(ApiResponse.success("Examination deleted", null));
    }
}
