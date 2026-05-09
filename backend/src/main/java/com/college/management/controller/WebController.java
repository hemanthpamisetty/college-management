package com.college.management.controller;

import com.college.management.dto.RegisterRequest;
import com.college.management.entity.Role;
import com.college.management.entity.User;
import com.college.management.repository.*;
import com.college.management.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final ResultRepository resultRepository;
    private final ExaminationRepository examinationRepository;
    private final EventRepository eventRepository;
    private final LibraryBookRepository libraryBookRepository;
    private final FeeRepository feeRepository;
    private final StudentDocumentRepository documentRepository;

    public WebController(AuthService authService, UserRepository userRepository,
                         AttendanceRepository attendanceRepository, ResultRepository resultRepository,
                         ExaminationRepository examinationRepository, EventRepository eventRepository,
                         LibraryBookRepository libraryBookRepository, FeeRepository feeRepository,
                         StudentDocumentRepository documentRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.attendanceRepository = attendanceRepository;
        this.resultRepository = resultRepository;
        this.examinationRepository = examinationRepository;
        this.eventRepository = eventRepository;
        this.libraryBookRepository = libraryBookRepository;
        this.feeRepository = feeRepository;
        this.documentRepository = documentRepository;
    }

    /**
     * Adds current user details (name, role, isAdmin, etc.) to the model
     * so all templates can display the correct user info and role-based UI.
     */
    private void addUserContext(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String email = auth.getName();
            userRepository.findByEmail(email).ifPresent(user -> {
                model.addAttribute("currentUser", user);
                model.addAttribute("userName", user.getName());
                model.addAttribute("userEmail", user.getEmail());
                model.addAttribute("userRole", user.getRole().name());
                model.addAttribute("userRoleDisplay", formatRole(user.getRole()));
                model.addAttribute("isAdmin", user.getRole() == Role.ADMIN);
                model.addAttribute("isFaculty", user.getRole() == Role.FACULTY);
                model.addAttribute("isStudent", user.getRole() == Role.STUDENT);
                model.addAttribute("userId", user.getId());
            });
        }
    }

    /**
     * Adds admin-specific dashboard stats (total counts from all tables).
     */
    private void addAdminStats(Model model) {
        model.addAttribute("totalStudents", userRepository.findAllByRole(Role.STUDENT).size());
        model.addAttribute("totalFaculty", userRepository.findAllByRole(Role.FACULTY).size());
        model.addAttribute("totalAttendance", attendanceRepository.count());
        model.addAttribute("totalResults", resultRepository.count());
        model.addAttribute("totalExams", examinationRepository.count());
        model.addAttribute("totalEvents", eventRepository.count());
        model.addAttribute("totalBooks", libraryBookRepository.count());
        model.addAttribute("totalFees", feeRepository.count());
        model.addAttribute("totalDocuments", documentRepository.count());
    }

    private String formatRole(Role role) {
        return switch (role) {
            case ADMIN -> "Administrator";
            case FACULTY -> "Faculty";
            case STUDENT -> "Student";
            case HOD -> "Head of Department";
        };
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute RegisterRequest registerRequest, Model model) {
        try {
            // Set default role if not provided (though form should handle it)
            if (registerRequest.getRole() == null) {
                registerRequest.setRole(Role.STUDENT);
            }
            authService.registerUser(registerRequest);
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("registerRequest", registerRequest);
            return "register";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        addUserContext(model);
        // If admin, add live stats from DB
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            userRepository.findByEmail(auth.getName()).ifPresent(user -> {
                if (user.getRole() == Role.ADMIN) {
                    addAdminStats(model);
                }
            });
        }
        return "dashbord";
    }

    @GetMapping("/departments")
    public String departments(Model model) {
        addUserContext(model);
        return "department";
    }

    @GetMapping("/timetable")
    public String timetable(Model model) {
        addUserContext(model);
        return "timetable";
    }

    @GetMapping("/attendance")
    public String attendance(Model model) {
        addUserContext(model);
        return "attendance";
    }

    @GetMapping("/results")
    public String results(Model model) {
        addUserContext(model);
        return "results";
    }

    @GetMapping("/examinations")
    public String examinations(Model model) {
        addUserContext(model);
        return "examinations";
    }

    @GetMapping("/events")
    public String events(Model model) {
        addUserContext(model);
        return "events";
    }

    @GetMapping("/library")
    public String library(Model model) {
        addUserContext(model);
        return "library";
    }

    @GetMapping("/fees")
    public String fees(Model model) {
        addUserContext(model);
        return "fees";
    }

    @GetMapping("/services")
    public String services(Model model) {
        addUserContext(model);
        return "services";
    }

    @GetMapping("/admin/students")
    public String adminStudents(Model model) {
        addUserContext(model);
        return "admin-students";
    }

    @GetMapping("/admin/panel")
    public String adminPanel(Model model) {
        addUserContext(model);
        return "admin-panel";
    }
}
