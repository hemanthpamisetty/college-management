package com.college.management.service;

import com.college.management.dto.StudentDocumentDTO;
import com.college.management.entity.DocumentType;
import com.college.management.entity.StudentDocument;
import com.college.management.entity.User;
import com.college.management.exception.ResourceNotFoundException;
import com.college.management.repository.StudentDocumentRepository;
import com.college.management.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentDocumentService {

    private static final Logger log = LoggerFactory.getLogger(StudentDocumentService.class);
    private final StudentDocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public StudentDocumentService(StudentDocumentRepository documentRepository, UserRepository userRepository, FileStorageService fileStorageService) {
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
    public StudentDocumentDTO uploadDocument(Long studentId, MultipartFile file, String description, Long uploadedById) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        String storedFileName = fileStorageService.storeFile(file);

        StudentDocument doc = new StudentDocument();
        doc.setStudent(student);
        doc.setFileName(storedFileName);
        doc.setOriginalFileName(file.getOriginalFilename());
        doc.setFileType(determineFileType(file.getContentType()));
        doc.setFilePath(fileStorageService.getFilePath(storedFileName));
        doc.setFileSize(file.getSize());
        doc.setContentType(file.getContentType());
        doc.setDescription(description);

        if (uploadedById != null) {
            User uploader = userRepository.findById(uploadedById).orElse(null);
            doc.setUploadedBy(uploader);
        }

        StudentDocument saved = documentRepository.save(doc);
        log.info("Document '{}' uploaded for student '{}'", file.getOriginalFilename(), student.getName());
        return toDTO(saved);
    }

    public List<StudentDocumentDTO> getDocumentsByStudent(Long studentId) {
        return documentRepository.findByStudentId(studentId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public StudentDocumentDTO getDocumentById(Long documentId) {
        return toDTO(documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + documentId)));
    }

    public Resource downloadDocument(Long documentId) {
        StudentDocument doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + documentId));
        return fileStorageService.loadFileAsResource(doc.getFileName());
    }

    public Long getStudentIdForDocument(Long documentId) {
        StudentDocument doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + documentId));
        return doc.getStudent().getId();
    }

    @Transactional
    public void deleteDocument(Long documentId) {
        StudentDocument doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + documentId));

        fileStorageService.deleteFile(doc.getFileName());
        documentRepository.delete(doc);
        log.info("Document '{}' deleted for student '{}'", doc.getOriginalFileName(), doc.getStudent().getName());
    }

    private DocumentType determineFileType(String contentType) {
        if (contentType == null) return DocumentType.DOCUMENT;
        if (contentType.startsWith("image/")) return DocumentType.IMAGE;
        if (contentType.equals("application/pdf")) return DocumentType.PDF;
        return DocumentType.DOCUMENT;
    }

    private StudentDocumentDTO toDTO(StudentDocument d) {
        StudentDocumentDTO dto = new StudentDocumentDTO();
        dto.setId(d.getId());
        dto.setStudentId(d.getStudent().getId());
        dto.setStudentName(d.getStudent().getName());
        dto.setFileName(d.getFileName());
        dto.setOriginalFileName(d.getOriginalFileName());
        dto.setFileType(d.getFileType());
        dto.setFileSize(d.getFileSize());
        dto.setContentType(d.getContentType());
        dto.setDescription(d.getDescription());
        dto.setDownloadUrl("/api/documents/" + d.getId() + "/download");
        dto.setUploadedAt(d.getUploadedAt());
        if (d.getUploadedBy() != null) {
            dto.setUploadedById(d.getUploadedBy().getId());
            dto.setUploadedByName(d.getUploadedBy().getName());
        }
        return dto;
    }
}
