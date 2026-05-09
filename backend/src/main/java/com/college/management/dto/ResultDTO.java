package com.college.management.dto;

import com.college.management.entity.ExamType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ResultDTO {
    private Long id;
    @NotNull(message = "Student ID is required")
    private Long studentId;
    private String studentName;
    @NotBlank(message = "Subject name is required")
    private String subjectName;
    @NotBlank(message = "Subject code is required")
    private String subjectCode;
    @NotNull(message = "Exam type is required")
    private ExamType examType;
    @NotNull(message = "Marks obtained is required")
    private Double marksObtained;
    @NotNull(message = "Total marks is required")
    private Double totalMarks;
    private String grade;
    @NotNull(message = "Semester is required")
    private Integer semester;
    @NotBlank(message = "Academic year is required")
    private String academicYear;
    @NotNull(message = "Credits is required")
    private Integer credits;
    private Double percentage;
    private LocalDateTime createdAt;

    public ResultDTO() {}

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
    public ExamType getExamType() { return examType; }
    public void setExamType(ExamType examType) { this.examType = examType; }
    public Double getMarksObtained() { return marksObtained; }
    public void setMarksObtained(Double marksObtained) { this.marksObtained = marksObtained; }
    public Double getTotalMarks() { return totalMarks; }
    public void setTotalMarks(Double totalMarks) { this.totalMarks = totalMarks; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }
    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }
    public Double getPercentage() { return percentage; }
    public void setPercentage(Double percentage) { this.percentage = percentage; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
