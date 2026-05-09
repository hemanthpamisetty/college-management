package com.college.management.repository;

import com.college.management.entity.Examination;
import com.college.management.entity.ExamType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExaminationRepository extends JpaRepository<Examination, Long> {
    List<Examination> findByDepartmentAndSemester(String department, Integer semester);
    List<Examination> findByExamDateAfter(LocalDate date);
    List<Examination> findByExamDateBetween(LocalDate start, LocalDate end);
    List<Examination> findByDepartmentAndSemesterAndExamDateAfter(String department, Integer semester, LocalDate date);
    List<Examination> findByExamType(ExamType examType);
}
