package com.college.management.controller;

import com.college.management.dto.ApiResponse;
import com.college.management.dto.ResultDTO;
import com.college.management.service.ResultService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/results")
public class ResultApiController {

    private final ResultService resultService;

    public ResultApiController(ResultService resultService) {
        this.resultService = resultService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ResultDTO>> create(@Valid @RequestBody ResultDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Result created", resultService.createResult(dto)));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<ResultDTO>>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success(resultService.getByStudentId(studentId)));
    }

    @GetMapping("/student/{studentId}/semester/{semester}")
    public ResponseEntity<ApiResponse<List<ResultDTO>>> getByStudentAndSemester(@PathVariable Long studentId, @PathVariable Integer semester) {
        return ResponseEntity.ok(ApiResponse.success(resultService.getByStudentAndSemester(studentId, semester)));
    }

    @GetMapping("/student/{studentId}/gpa/{semester}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getGPA(@PathVariable Long studentId, @PathVariable Integer semester) {
        return ResponseEntity.ok(ApiResponse.success(resultService.calculateGPA(studentId, semester)));
    }

    @GetMapping("/student/{studentId}/cgpa")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCGPA(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success(resultService.calculateCGPA(studentId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ResultDTO>> update(@PathVariable Long id, @RequestBody ResultDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Result updated", resultService.updateResult(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        resultService.deleteResult(id);
        return ResponseEntity.ok(ApiResponse.success("Result deleted", null));
    }
}
