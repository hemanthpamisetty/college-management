package com.college.management.repository;

import com.college.management.entity.Fee;
import com.college.management.entity.FeeStatus;
import com.college.management.entity.FeeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeeRepository extends JpaRepository<Fee, Long> {
    List<Fee> findByStudentId(Long studentId);
    List<Fee> findByStudentIdAndStatus(Long studentId, FeeStatus status);
    List<Fee> findByStudentIdAndSemester(Long studentId, Integer semester);
    List<Fee> findByStudentIdAndFeeType(Long studentId, FeeType feeType);
    List<Fee> findByStatus(FeeStatus status);
}
