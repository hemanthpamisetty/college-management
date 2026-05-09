package com.college.management.repository;

import com.college.management.entity.Attendance;
import com.college.management.entity.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentId(Long studentId);
    List<Attendance> findByStudentIdAndSubjectCode(Long studentId, String subjectCode);
    List<Attendance> findByDepartmentAndSemester(String department, Integer semester);
    List<Attendance> findByStudentIdAndDateBetween(Long studentId, LocalDate start, LocalDate end);
    List<Attendance> findByDateAndDepartmentAndSemester(LocalDate date, String department, Integer semester);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.student.id = :studentId AND a.subjectCode = :subjectCode")
    long countByStudentIdAndSubjectCode(@Param("studentId") Long studentId, @Param("subjectCode") String subjectCode);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.student.id = :studentId AND a.subjectCode = :subjectCode AND a.status = :status")
    long countByStudentIdAndSubjectCodeAndStatus(@Param("studentId") Long studentId, @Param("subjectCode") String subjectCode, @Param("status") AttendanceStatus status);
}
