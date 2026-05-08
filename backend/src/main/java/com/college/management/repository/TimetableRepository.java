package com.college.management.repository;

import com.college.management.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    List<Timetable> findByDepartmentAndSectionAndSemester(String department, String section, Integer semester);
    List<Timetable> findByDepartment(String department);
    List<Timetable> findByDepartmentAndSemester(String department, Integer semester);
    List<Timetable> findByDepartmentAndSection(String department, String section);
}
