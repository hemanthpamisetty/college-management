package com.college.management.controller;

import com.college.management.dto.ApiResponse;
import com.college.management.dto.StudentDocumentDTO;
import com.college.management.service.StudentDocumentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class StudentDocumentController {

    private final StudentDocumentService documentService;

    public StudentDocumentController(StudentDocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/students/{studentId}/documents/upload")
    public ResponseEntity<ApiResponse<StudentDocumentDTO>> uploadDocument(
            @PathVariable Long studentId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "uploadedById", required = false) Long uploadedById) {

        StudentDocumentDTO doc = documentService.uploadDocument(studentId, file, description, uploadedById);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Document uploaded successfully", doc));
    }

    @GetMapping("/students/{studentId}/documents")
    public ResponseEntity<ApiResponse<List<StudentDocumentDTO>>> getStudentDocuments(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success(documentService.getDocumentsByStudent(studentId)));
    }

    @GetMapping("/documents/{documentId}")
    public ResponseEntity<ApiResponse<StudentDocumentDTO>> getDocument(@PathVariable Long documentId) {
        return ResponseEntity.ok(ApiResponse.success(documentService.getDocumentById(documentId)));
    }

    @GetMapping("/documents/{documentId}/download")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long documentId) {
        StudentDocumentDTO docDTO = documentService.getDocumentById(documentId);
        Resource resource = documentService.downloadDocument(documentId);

        String contentType = docDTO.getContentType() != null ? docDTO.getContentType() : "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + docDTO.getOriginalFileName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/documents/{documentId}")
    public ResponseEntity<ApiResponse<Void>> deleteDocument(@PathVariable Long documentId) {
        documentService.deleteDocument(documentId);
        return ResponseEntity.ok(ApiResponse.success("Document deleted successfully", null));
    }
}
