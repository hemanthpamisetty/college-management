package com.college.management.dto;

import com.college.management.entity.AttendanceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AttendanceDTO {
    private Long id;
    @NotNull(message = "Student ID is required")
    private Long studentId;
    private String studentName;
    @NotBlank(message = "Subject name is required")
    private String subjectName;
    @NotBlank(message = "Subject code is required")
    private String subjectCode;
    @NotNull(message = "Date is required")
    private LocalDate date;
    @NotNull(message = "Status is required")
    private AttendanceStatus status;
    private Long markedById;
    private String markedByName;
    @NotBlank(message = "Department is required")
    private String department;
    private String section;
    @NotNull(message = "Semester is required")
    private Integer semester;
    private LocalDateTime createdAt;

    public AttendanceDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public AttendanceStatus getStatus() { return status; }
    public void setStatus(AttendanceStatus status) { this.status = status; }
    public Long getMarkedById() { return markedById; }
    public void setMarkedById(Long markedById) { this.markedById = markedById; }
    public String getMarkedByName() { return markedByName; }
    public void setMarkedByName(String markedByName) { this.markedByName = markedByName; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }
    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
