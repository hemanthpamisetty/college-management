package com.college.management.dto;

import com.college.management.entity.Role;
import java.util.List;

public class StudentDetailDTO {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private String registrationNumber;
    private String department;
    private String section;
    private Integer semester;
    private List<StudentDocumentDTO> documents;
    private long documentCount;
    private Double attendancePercentage;
    private Double gpa;

    public StudentDetailDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }
    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }
    public List<StudentDocumentDTO> getDocuments() { return documents; }
    public void setDocuments(List<StudentDocumentDTO> documents) { this.documents = documents; }
    public long getDocumentCount() { return documentCount; }
    public void setDocumentCount(long documentCount) { this.documentCount = documentCount; }
    public Double getAttendancePercentage() { return attendancePercentage; }
    public void setAttendancePercentage(Double attendancePercentage) { this.attendancePercentage = attendancePercentage; }
    public Double getGpa() { return gpa; }
    public void setGpa(Double gpa) { this.gpa = gpa; }
}
