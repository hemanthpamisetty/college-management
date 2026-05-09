package com.college.management.service;

import com.college.management.dto.ExaminationDTO;
import com.college.management.entity.Examination;
import com.college.management.exception.ResourceNotFoundException;
import com.college.management.repository.ExaminationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExaminationService {

    private static final Logger log = LoggerFactory.getLogger(ExaminationService.class);
    private final ExaminationRepository examinationRepository;

    public ExaminationService(ExaminationRepository examinationRepository) {
        this.examinationRepository = examinationRepository;
    }

    @Transactional
    public ExaminationDTO createExamination(ExaminationDTO dto) {
        Examination exam = new Examination();
        exam.setExamName(dto.getExamName());
        exam.setSubjectName(dto.getSubjectName());
        exam.setSubjectCode(dto.getSubjectCode());
        exam.setDepartment(dto.getDepartment());
        exam.setSemester(dto.getSemester());
        exam.setExamDate(dto.getExamDate());
        exam.setStartTime(dto.getStartTime());
        exam.setEndTime(dto.getEndTime());
        exam.setRoom(dto.getRoom());
        exam.setExamType(dto.getExamType());
        exam.setInstructions(dto.getInstructions());

        Examination saved = examinationRepository.save(exam);
        log.info("Examination created: {} for {}", dto.getExamName(), dto.getDepartment());
        return toDTO(saved);
    }

    public List<ExaminationDTO> getAllExaminations() {
        return examinationRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ExaminationDTO getById(Long id) {
        return toDTO(examinationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Examination not found with id: " + id)));
    }

    public List<ExaminationDTO> getUpcomingExams() {
        return examinationRepository.findByExamDateAfter(LocalDate.now()).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ExaminationDTO> getByDepartmentAndSemester(String department, Integer semester) {
        return examinationRepository.findByDepartmentAndSemester(department, semester).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ExaminationDTO> getUpcomingByDeptAndSem(String department, Integer semester) {
        return examinationRepository.findByDepartmentAndSemesterAndExamDateAfter(department, semester, LocalDate.now())
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public ExaminationDTO updateExamination(Long id, ExaminationDTO dto) {
        Examination exam = examinationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Examination not found with id: " + id));

        if (dto.getExamName() != null) exam.setExamName(dto.getExamName());
        if (dto.getSubjectName() != null) exam.setSubjectName(dto.getSubjectName());
        if (dto.getExamDate() != null) exam.setExamDate(dto.getExamDate());
        if (dto.getStartTime() != null) exam.setStartTime(dto.getStartTime());
        if (dto.getEndTime() != null) exam.setEndTime(dto.getEndTime());
        if (dto.getRoom() != null) exam.setRoom(dto.getRoom());
        if (dto.getInstructions() != null) exam.setInstructions(dto.getInstructions());

        return toDTO(examinationRepository.save(exam));
    }

    @Transactional
    public void deleteExamination(Long id) {
        if (!examinationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Examination not found with id: " + id);
        }
        examinationRepository.deleteById(id);
    }

    private ExaminationDTO toDTO(Examination e) {
        ExaminationDTO dto = new ExaminationDTO();
        dto.setId(e.getId());
        dto.setExamName(e.getExamName());
        dto.setSubjectName(e.getSubjectName());
        dto.setSubjectCode(e.getSubjectCode());
        dto.setDepartment(e.getDepartment());
        dto.setSemester(e.getSemester());
        dto.setExamDate(e.getExamDate());
        dto.setStartTime(e.getStartTime());
        dto.setEndTime(e.getEndTime());
        dto.setRoom(e.getRoom());
        dto.setExamType(e.getExamType());
        dto.setInstructions(e.getInstructions());
        return dto;
    }
}
