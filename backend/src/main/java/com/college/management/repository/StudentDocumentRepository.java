package com.college.management.repository;

import com.college.management.entity.DocumentType;
import com.college.management.entity.StudentDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentDocumentRepository extends JpaRepository<StudentDocument, Long> {
    List<StudentDocument> findByStudentId(Long studentId);
    List<StudentDocument> findByStudentIdAndFileType(Long studentId, DocumentType fileType);
    long countByStudentId(Long studentId);
}
