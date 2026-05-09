package com.college.management.repository;

import com.college.management.entity.BookIssue;
import com.college.management.entity.BookIssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookIssueRepository extends JpaRepository<BookIssue, Long> {
    List<BookIssue> findByStudentId(Long studentId);
    List<BookIssue> findByStatus(BookIssueStatus status);
    List<BookIssue> findByStudentIdAndStatus(Long studentId, BookIssueStatus status);
    List<BookIssue> findByBookId(Long bookId);
    long countByStudentIdAndStatus(Long studentId, BookIssueStatus status);
}
