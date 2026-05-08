package com.college.management.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "timetables")
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String department; // e.g., "CSE", "ECE", "MECH"

    @Column(nullable = false)
    private String section; // e.g., "A", "B", "C"

    @Column(nullable = false)
    private Integer semester; // e.g., 1-8

    @Column(nullable = false)
    private String day; // e.g., "Monday", "Tuesday"

    @Column(nullable = false)
    private String timeSlot; // e.g., "9:00–10:00"

    @Column(nullable = false)
    private String subjectName; // e.g., "Data Structures"

    @Column(nullable = false)
    private String subjectCode; // e.g., "DS"

    @Column(nullable = false)
    private String room; // e.g., "Room 302", "Lab 1"

    private String facultyName; // optional

    public Timetable() {}

    public Timetable(Long id, String department, String section, Integer semester, String day,
                     String timeSlot, String subjectName, String subjectCode, String room, String facultyName) {
        this.id = id;
        this.department = department;
        this.section = section;
        this.semester = semester;
        this.day = day;
        this.timeSlot = timeSlot;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.room = room;
        this.facultyName = facultyName;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }
    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }
    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }
    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
    public String getFacultyName() { return facultyName; }
    public void setFacultyName(String facultyName) { this.facultyName = facultyName; }
}
