package com.college.management.service;

import com.college.management.entity.Timetable;
import com.college.management.repository.TimetableRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimetableService {

    private final TimetableRepository timetableRepository;

    public TimetableService(TimetableRepository timetableRepository) {
        this.timetableRepository = timetableRepository;
    }

    public Timetable addTimetableEntry(Timetable timetable) {
        return timetableRepository.save(timetable);
    }

    public List<Timetable> getTimetableFiltered(String department, String section, Integer semester) {
        if (department != null && section != null && semester != null) {
            return timetableRepository.findByDepartmentAndSectionAndSemester(department, section, semester);
        } else if (department != null && semester != null) {
            return timetableRepository.findByDepartmentAndSemester(department, semester);
        } else if (department != null && section != null) {
            return timetableRepository.findByDepartmentAndSection(department, section);
        } else if (department != null) {
            return timetableRepository.findByDepartment(department);
        }
        return timetableRepository.findAll();
    }

    public List<Timetable> getAllTimetables() {
        return timetableRepository.findAll();
    }

    public Timetable updateTimetable(Long id, Timetable updated) {
        Timetable existing = timetableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Timetable entry not found with id: " + id));
        existing.setDepartment(updated.getDepartment());
        existing.setSection(updated.getSection());
        existing.setSemester(updated.getSemester());
        existing.setDay(updated.getDay());
        existing.setTimeSlot(updated.getTimeSlot());
        existing.setSubjectName(updated.getSubjectName());
        existing.setSubjectCode(updated.getSubjectCode());
        existing.setRoom(updated.getRoom());
        existing.setFacultyName(updated.getFacultyName());
        return timetableRepository.save(existing);
    }

    public void deleteTimetable(Long id) {
        if (!timetableRepository.existsById(id)) {
            throw new RuntimeException("Timetable entry not found with id: " + id);
        }
        timetableRepository.deleteById(id);
    }
}
