package com.college.management.service;

import com.college.management.dto.FeeDTO;
import com.college.management.entity.Fee;
import com.college.management.entity.FeeStatus;
import com.college.management.entity.User;
import com.college.management.exception.ResourceNotFoundException;
import com.college.management.repository.FeeRepository;
import com.college.management.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeeService {

    private static final Logger log = LoggerFactory.getLogger(FeeService.class);
    private final FeeRepository feeRepository;
    private final UserRepository userRepository;

    public FeeService(FeeRepository feeRepository, UserRepository userRepository) {
        this.feeRepository = feeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public FeeDTO createFee(FeeDTO dto) {
        User student = userRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + dto.getStudentId()));

        Fee fee = new Fee();
        fee.setStudent(student);
        fee.setFeeType(dto.getFeeType());
        fee.setAmount(dto.getAmount());
        fee.setDueDate(dto.getDueDate());
        fee.setStatus(dto.getStatus() != null ? dto.getStatus() : FeeStatus.UNPAID);
        fee.setSemester(dto.getSemester());
        fee.setAcademicYear(dto.getAcademicYear());

        Fee saved = feeRepository.save(fee);
        log.info("Fee created for student {} - {} Rs.{}", student.getName(), dto.getFeeType(), dto.getAmount());
        return toDTO(saved);
    }

    public List<FeeDTO> getByStudentId(Long studentId) {
        return feeRepository.findByStudentId(studentId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<FeeDTO> getUnpaidByStudent(Long studentId) {
        return feeRepository.findByStudentIdAndStatus(studentId, FeeStatus.UNPAID).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<FeeDTO> getAllFees() {
        return feeRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public FeeDTO payFee(Long feeId, String transactionId) {
        Fee fee = feeRepository.findById(feeId)
                .orElseThrow(() -> new ResourceNotFoundException("Fee not found with id: " + feeId));

        if (fee.getStatus() == FeeStatus.PAID) {
            throw new IllegalStateException("Fee already paid");
        }

        fee.setStatus(FeeStatus.PAID);
        fee.setPaidDate(LocalDate.now());
        fee.setTransactionId(transactionId);

        log.info("Fee {} paid by student {}", feeId, fee.getStudent().getName());
        return toDTO(feeRepository.save(fee));
    }

    @Transactional
    public FeeDTO updateFee(Long id, FeeDTO dto) {
        Fee fee = feeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fee not found with id: " + id));

        if (dto.getAmount() != null) fee.setAmount(dto.getAmount());
        if (dto.getDueDate() != null) fee.setDueDate(dto.getDueDate());
        if (dto.getStatus() != null) fee.setStatus(dto.getStatus());
        if (dto.getFeeType() != null) fee.setFeeType(dto.getFeeType());

        return toDTO(feeRepository.save(fee));
    }

    @Transactional
    public void deleteFee(Long id) {
        if (!feeRepository.existsById(id)) throw new ResourceNotFoundException("Fee not found with id: " + id);
        feeRepository.deleteById(id);
    }

    private FeeDTO toDTO(Fee f) {
        FeeDTO dto = new FeeDTO();
        dto.setId(f.getId());
        dto.setStudentId(f.getStudent().getId());
        dto.setStudentName(f.getStudent().getName());
        dto.setFeeType(f.getFeeType());
        dto.setAmount(f.getAmount());
        dto.setDueDate(f.getDueDate());
        dto.setPaidDate(f.getPaidDate());
        dto.setStatus(f.getStatus());
        dto.setSemester(f.getSemester());
        dto.setAcademicYear(f.getAcademicYear());
        dto.setTransactionId(f.getTransactionId());
        return dto;
    }
}
