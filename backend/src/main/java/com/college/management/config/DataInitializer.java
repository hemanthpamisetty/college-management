package com.college.management.config;

import com.college.management.entity.*;
import com.college.management.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AttendanceRepository attendanceRepository;
    private final ResultRepository resultRepository;
    private final ExaminationRepository examinationRepository;
    private final EventRepository eventRepository;
    private final LibraryBookRepository bookRepository;
    private final FeeRepository feeRepository;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           AttendanceRepository attendanceRepository, ResultRepository resultRepository,
                           ExaminationRepository examinationRepository, EventRepository eventRepository,
                           LibraryBookRepository bookRepository, FeeRepository feeRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.attendanceRepository = attendanceRepository;
        this.resultRepository = resultRepository;
        this.examinationRepository = examinationRepository;
        this.eventRepository = eventRepository;
        this.bookRepository = bookRepository;
        this.feeRepository = feeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("---------------------------------------------------------");
        log.info("[DATA-INIT] Starting data initialization...");

        // ---- Admin ----
        User admin = null;
        if (!userRepository.existsByEmail("admin@college.edu")) {
            admin = User.builder()
                    .name("System Admin")
                    .email("admin@college.edu")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .registrationNumber("ADMIN001")
                    .build();
            admin = userRepository.save(admin);
            log.info("[DATA-INIT] Admin account CREATED: admin@college.edu / admin123");
        } else {
            admin = userRepository.findByEmail("admin@college.edu").orElse(null);
            log.info("[DATA-INIT] Admin account already EXISTS.");
        }

        // ---- Sample Students ----
        User student1 = createStudentIfNotExists("Rahul Kumar", "rahul@college.edu", "STU2024001", "CSE", "A", 4);
        User student2 = createStudentIfNotExists("Priya Sharma", "priya@college.edu", "STU2024002", "CSE", "A", 4);
        User student3 = createStudentIfNotExists("Arjun Reddy", "arjun@college.edu", "STU2024003", "ECE", "B", 3);

        // ---- Sample Faculty ----
        if (!userRepository.existsByEmail("faculty@college.edu")) {
            User faculty = User.builder()
                    .name("Dr. Ramesh Kumar")
                    .email("faculty@college.edu")
                    .password(passwordEncoder.encode("faculty123"))
                    .role(Role.FACULTY)
                    .facultyId("FAC001")
                    .department("CSE")
                    .build();
            userRepository.save(faculty);
            log.info("[DATA-INIT] Faculty account CREATED: faculty@college.edu / faculty123");
        }

        // ---- Sample Attendance ----
        if (attendanceRepository.count() == 0 && student1 != null) {
            for (int i = 0; i < 10; i++) {
                Attendance a = new Attendance();
                a.setStudent(student1);
                a.setSubjectName("Data Structures");
                a.setSubjectCode("CS301");
                a.setDate(LocalDate.now().minusDays(i + 1));
                a.setStatus(i < 8 ? AttendanceStatus.PRESENT : AttendanceStatus.ABSENT);
                a.setDepartment("CSE");
                a.setSection("A");
                a.setSemester(4);
                attendanceRepository.save(a);
            }
            log.info("[DATA-INIT] Sample attendance records created.");
        }

        // ---- Sample Results ----
        if (resultRepository.count() == 0 && student1 != null) {
            createResult(student1, "Data Structures", "CS301", ExamType.SEMESTER, 82.0, 100.0, 4, "2025-26", 4);
            createResult(student1, "Operating Systems", "CS302", ExamType.SEMESTER, 75.0, 100.0, 4, "2025-26", 3);
            createResult(student1, "Database Systems", "CS303", ExamType.SEMESTER, 90.0, 100.0, 4, "2025-26", 4);
            createResult(student1, "Computer Networks", "CS304", ExamType.SEMESTER, 68.0, 100.0, 4, "2025-26", 3);
            log.info("[DATA-INIT] Sample results created.");
        }

        // ---- Sample Examinations ----
        if (examinationRepository.count() == 0) {
            createExam("Semester End Exam", "Data Structures", "CS301", "CSE", 4, LocalDate.now().plusDays(20), LocalTime.of(9, 0), LocalTime.of(12, 0), "Hall A", ExamType.SEMESTER);
            createExam("Semester End Exam", "Operating Systems", "CS302", "CSE", 4, LocalDate.now().plusDays(23), LocalTime.of(9, 0), LocalTime.of(12, 0), "Hall B", ExamType.SEMESTER);
            createExam("Mid Term Exam", "Digital Electronics", "EC201", "ECE", 3, LocalDate.now().plusDays(10), LocalTime.of(14, 0), LocalTime.of(16, 0), "Room 302", ExamType.MIDTERM);
            log.info("[DATA-INIT] Sample examinations created.");
        }

        // ---- Sample Events ----
        if (eventRepository.count() == 0) {
            createEvent("Tech Fest 2026", "Annual technology festival with workshops and competitions.", LocalDate.now().plusDays(30), "Main Auditorium", EventCategory.TECHNICAL, admin);
            createEvent("Placement Drive - TCS", "Campus recruitment drive by TCS.", LocalDate.now().plusDays(15), "Seminar Hall", EventCategory.PLACEMENT, admin);
            createEvent("Annual Sports Meet", "Inter-department sports competition.", LocalDate.now().plusDays(45), "Sports Ground", EventCategory.SPORTS, admin);
            createEvent("Cultural Fest - Utsav 2026", "Three-day cultural extravaganza.", LocalDate.now().plusDays(60), "Open Air Theatre", EventCategory.CULTURAL, admin);
            log.info("[DATA-INIT] Sample events created.");
        }

        // ---- Sample Library Books ----
        if (bookRepository.count() == 0) {
            createBook("Data Structures and Algorithms", "Thomas H. Cormen", "978-0262033848", "MIT Press", "Computer Science", 5, "CS-A1", 2009);
            createBook("Operating System Concepts", "Abraham Silberschatz", "978-1119800361", "Wiley", "Computer Science", 3, "CS-A2", 2021);
            createBook("Computer Networking", "James Kurose", "978-0133594140", "Pearson", "Computer Science", 4, "CS-B1", 2016);
            createBook("Digital Signal Processing", "John G. Proakis", "978-0131873742", "Pearson", "Electronics", 3, "EC-A1", 2006);
            createBook("Engineering Mathematics", "B.S. Grewal", "978-8174091956", "Khanna Publishers", "Mathematics", 6, "MA-A1", 2020);
            log.info("[DATA-INIT] Sample library books created.");
        }

        // ---- Sample Fees ----
        if (feeRepository.count() == 0 && student1 != null) {
            createFee(student1, FeeType.TUITION, new BigDecimal("75000.00"), LocalDate.now().plusDays(30), FeeStatus.UNPAID, 4, "2025-26");
            createFee(student1, FeeType.EXAM, new BigDecimal("2500.00"), LocalDate.now().plusDays(15), FeeStatus.PAID, 4, "2025-26");
            createFee(student1, FeeType.LIBRARY, new BigDecimal("1000.00"), LocalDate.now().plusDays(30), FeeStatus.UNPAID, 4, "2025-26");
            if (student2 != null) {
                createFee(student2, FeeType.TUITION, new BigDecimal("75000.00"), LocalDate.now().plusDays(30), FeeStatus.UNPAID, 4, "2025-26");
            }
            log.info("[DATA-INIT] Sample fees created.");
        }

        log.info("[DATA-INIT] Data initialization complete!");
        log.info("---------------------------------------------------------");
    }

    private User createStudentIfNotExists(String name, String email, String regNo, String dept, String section, int semester) {
        if (!userRepository.existsByEmail(email)) {
            User student = User.builder()
                    .name(name)
                    .email(email)
                    .password(passwordEncoder.encode("student123"))
                    .role(Role.STUDENT)
                    .registrationNumber(regNo)
                    .department(dept)
                    .section(section)
                    .semester(semester)
                    .build();
            student = userRepository.save(student);
            log.info("[DATA-INIT] Student created: {} / student123", email);
            return student;
        }
        return userRepository.findByEmail(email).orElse(null);
    }

    private void createResult(User student, String subject, String code, ExamType type, double marks, double total, int sem, String year, int credits) {
        Result r = new Result();
        r.setStudent(student);
        r.setSubjectName(subject);
        r.setSubjectCode(code);
        r.setExamType(type);
        r.setMarksObtained(marks);
        r.setTotalMarks(total);
        r.setGrade(calculateGrade(marks, total));
        r.setSemester(sem);
        r.setAcademicYear(year);
        r.setCredits(credits);
        resultRepository.save(r);
    }

    private void createExam(String name, String subject, String code, String dept, int sem, LocalDate date, LocalTime start, LocalTime end, String room, ExamType type) {
        Examination e = new Examination();
        e.setExamName(name);
        e.setSubjectName(subject);
        e.setSubjectCode(code);
        e.setDepartment(dept);
        e.setSemester(sem);
        e.setExamDate(date);
        e.setStartTime(start);
        e.setEndTime(end);
        e.setRoom(room);
        e.setExamType(type);
        examinationRepository.save(e);
    }

    private void createEvent(String title, String desc, LocalDate date, String location, EventCategory category, User createdBy) {
        Event e = new Event();
        e.setTitle(title);
        e.setDescription(desc);
        e.setEventDate(date);
        e.setLocation(location);
        e.setCategory(category);
        e.setCreatedBy(createdBy);
        e.setIsActive(true);
        eventRepository.save(e);
    }

    private void createBook(String title, String author, String isbn, String publisher, String category, int copies, String shelf, int year) {
        LibraryBook b = new LibraryBook();
        b.setTitle(title);
        b.setAuthor(author);
        b.setIsbn(isbn);
        b.setPublisher(publisher);
        b.setCategory(category);
        b.setTotalCopies(copies);
        b.setAvailableCopies(copies);
        b.setShelfLocation(shelf);
        b.setPublishedYear(year);
        bookRepository.save(b);
    }

    private void createFee(User student, FeeType type, BigDecimal amount, LocalDate dueDate, FeeStatus status, int semester, String year) {
        Fee f = new Fee();
        f.setStudent(student);
        f.setFeeType(type);
        f.setAmount(amount);
        f.setDueDate(dueDate);
        f.setStatus(status);
        f.setSemester(semester);
        f.setAcademicYear(year);
        if (status == FeeStatus.PAID) {
            f.setPaidDate(LocalDate.now().minusDays(5));
            f.setTransactionId("TXN" + System.currentTimeMillis());
        }
        feeRepository.save(f);
    }

    private String calculateGrade(double marks, double total) {
        double pct = (marks / total) * 100;
        if (pct >= 90) return "O";
        if (pct >= 80) return "A+";
        if (pct >= 70) return "A";
        if (pct >= 60) return "B+";
        if (pct >= 50) return "B";
        if (pct >= 40) return "C";
        return "F";
    }
}
