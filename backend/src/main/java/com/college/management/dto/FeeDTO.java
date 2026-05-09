package com.college.management.dto;

import com.college.management.entity.FeeStatus;
import com.college.management.entity.FeeType;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class FeeDTO {
    private Long id;
    @NotNull(message = "Student ID is required")
    private Long studentId;
    private String studentName;
    @NotNull(message = "Fee type is required")
    private FeeType feeType;
    @NotNull(message = "Amount is required")
    private BigDecimal amount;
    @NotNull(message = "Due date is required")
    private LocalDate dueDate;
    private LocalDate paidDate;
    private FeeStatus status;
    @NotNull(message = "Semester is required")
    private Integer semester;
    @NotNull(message = "Academic year is required")
    private String academicYear;
    private String transactionId;

    public FeeDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public FeeType getFeeType() { return feeType; }
    public void setFeeType(FeeType feeType) { this.feeType = feeType; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public LocalDate getPaidDate() { return paidDate; }
    public void setPaidDate(LocalDate paidDate) { this.paidDate = paidDate; }
    public FeeStatus getStatus() { return status; }
    public void setStatus(FeeStatus status) { this.status = status; }
    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }
    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
}
