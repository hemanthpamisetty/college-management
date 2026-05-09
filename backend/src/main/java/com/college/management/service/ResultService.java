package com.college.management.service;

import com.college.management.dto.ResultDTO;
import com.college.management.entity.Result;
import com.college.management.entity.User;
import com.college.management.exception.ResourceNotFoundException;
import com.college.management.repository.ResultRepository;
import com.college.management.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResultService {

    private static final Logger log = LoggerFactory.getLogger(ResultService.class);
    private final ResultRepository resultRepository;
    private final UserRepository userRepository;

    public ResultService(ResultRepository resultRepository, UserRepository userRepository) {
        this.resultRepository = resultRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ResultDTO createResult(ResultDTO dto) {
        User student = userRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + dto.getStudentId()));

        Result result = new Result();
        result.setStudent(student);
        result.setSubjectName(dto.getSubjectName());
        result.setSubjectCode(dto.getSubjectCode());
        result.setExamType(dto.getExamType());
        result.setMarksObtained(dto.getMarksObtained());
        result.setTotalMarks(dto.getTotalMarks());
        result.setGrade(dto.getGrade() != null ? dto.getGrade() : calculateGrade(dto.getMarksObtained(), dto.getTotalMarks()));
        result.setSemester(dto.getSemester());
        result.setAcademicYear(dto.getAcademicYear());
        result.setCredits(dto.getCredits());

        Result saved = resultRepository.save(result);
        log.info("Result created for student {} in subject {}", student.getName(), dto.getSubjectCode());
        return toDTO(saved);
    }

    public List<ResultDTO> getByStudentId(Long studentId) {
        return resultRepository.findByStudentId(studentId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ResultDTO> getByStudentAndSemester(Long studentId, Integer semester) {
        return resultRepository.findByStudentIdAndSemester(studentId, semester).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Map<String, Object> calculateGPA(Long studentId, Integer semester) {
        List<Result> results = resultRepository.findByStudentIdAndSemester(studentId, semester);
        double totalGradePoints = 0;
        int totalCredits = 0;

        for (Result r : results) {
            double gradePoint = getGradePoint(r.getGrade());
            totalGradePoints += gradePoint * r.getCredits();
            totalCredits += r.getCredits();
        }

        double gpa = totalCredits > 0 ? totalGradePoints / totalCredits : 0.0;

        Map<String, Object> result = new HashMap<>();
        result.put("studentId", studentId);
        result.put("semester", semester);
        result.put("gpa", Math.round(gpa * 100.0) / 100.0);
        result.put("totalCredits", totalCredits);
        result.put("subjectsCount", results.size());
        return result;
    }

    public Map<String, Object> calculateCGPA(Long studentId) {
        List<Result> allResults = resultRepository.findByStudentId(studentId);
        double totalGradePoints = 0;
        int totalCredits = 0;

        for (Result r : allResults) {
            double gradePoint = getGradePoint(r.getGrade());
            totalGradePoints += gradePoint * r.getCredits();
            totalCredits += r.getCredits();
        }

        double cgpa = totalCredits > 0 ? totalGradePoints / totalCredits : 0.0;

        Map<String, Object> result = new HashMap<>();
        result.put("studentId", studentId);
        result.put("cgpa", Math.round(cgpa * 100.0) / 100.0);
        result.put("totalCredits", totalCredits);
        return result;
    }

    @Transactional
    public ResultDTO updateResult(Long id, ResultDTO dto) {
        Result result = resultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Result not found with id: " + id));

        if (dto.getMarksObtained() != null) result.setMarksObtained(dto.getMarksObtained());
        if (dto.getTotalMarks() != null) result.setTotalMarks(dto.getTotalMarks());
        if (dto.getGrade() != null) result.setGrade(dto.getGrade());
        else result.setGrade(calculateGrade(result.getMarksObtained(), result.getTotalMarks()));
        if (dto.getSubjectName() != null) result.setSubjectName(dto.getSubjectName());

        return toDTO(resultRepository.save(result));
    }

    @Transactional
    public void deleteResult(Long id) {
        if (!resultRepository.existsById(id)) {
            throw new ResourceNotFoundException("Result not found with id: " + id);
        }
        resultRepository.deleteById(id);
    }

    private String calculateGrade(Double marks, Double total) {
        if (marks == null || total == null || total == 0) return "F";
        double pct = (marks / total) * 100;
        if (pct >= 90) return "O";
        if (pct >= 80) return "A+";
        if (pct >= 70) return "A";
        if (pct >= 60) return "B+";
        if (pct >= 50) return "B";
        if (pct >= 40) return "C";
        return "F";
    }

    private double getGradePoint(String grade) {
        if (grade == null) return 0;
        return switch (grade) {
            case "O" -> 10.0;
            case "A+" -> 9.0;
            case "A" -> 8.0;
            case "B+" -> 7.0;
            case "B" -> 6.0;
            case "C" -> 5.0;
            default -> 0.0;
        };
    }

    private ResultDTO toDTO(Result r) {
        ResultDTO dto = new ResultDTO();
        dto.setId(r.getId());
        dto.setStudentId(r.getStudent().getId());
        dto.setStudentName(r.getStudent().getName());
        dto.setSubjectName(r.getSubjectName());
        dto.setSubjectCode(r.getSubjectCode());
        dto.setExamType(r.getExamType());
        dto.setMarksObtained(r.getMarksObtained());
        dto.setTotalMarks(r.getTotalMarks());
        dto.setGrade(r.getGrade());
        dto.setSemester(r.getSemester());
        dto.setAcademicYear(r.getAcademicYear());
        dto.setCredits(r.getCredits());
        dto.setPercentage(r.getTotalMarks() > 0 ? Math.round((r.getMarksObtained() / r.getTotalMarks()) * 10000.0) / 100.0 : 0.0);
        dto.setCreatedAt(r.getCreatedAt());
        return dto;
    }
}
