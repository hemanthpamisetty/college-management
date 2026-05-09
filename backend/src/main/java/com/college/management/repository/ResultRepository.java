package com.college.management.repository;

import com.college.management.entity.Result;
import com.college.management.entity.ExamType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByStudentId(Long studentId);
    List<Result> findByStudentIdAndSemester(Long studentId, Integer semester);
    List<Result> findByStudentIdAndExamType(Long studentId, ExamType examType);
    List<Result> findByStudentIdAndAcademicYear(Long studentId, String academicYear);
}
