package com.college.management.controller;

import com.college.management.dto.ApiResponse;
import com.college.management.dto.FeeDTO;
import com.college.management.service.FeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/fees")
public class FeeApiController {

    private final FeeService feeService;

    public FeeApiController(FeeService feeService) {
        this.feeService = feeService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FeeDTO>> create(@Valid @RequestBody FeeDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Fee created", feeService.createFee(dto)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FeeDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(feeService.getAllFees()));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<FeeDTO>>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success(feeService.getByStudentId(studentId)));
    }

    @GetMapping("/student/{studentId}/unpaid")
    public ResponseEntity<ApiResponse<List<FeeDTO>>> getUnpaid(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success(feeService.getUnpaidByStudent(studentId)));
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<ApiResponse<FeeDTO>> payFee(@PathVariable Long id, @RequestParam(required = false) String transactionId) {
        return ResponseEntity.ok(ApiResponse.success("Fee paid", feeService.payFee(id, transactionId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FeeDTO>> update(@PathVariable Long id, @RequestBody FeeDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Fee updated", feeService.updateFee(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        feeService.deleteFee(id);
        return ResponseEntity.ok(ApiResponse.success("Fee deleted", null));
    }
}
