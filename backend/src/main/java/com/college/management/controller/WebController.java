package com.college.management.controller;

import com.college.management.dto.RegisterRequest;
import com.college.management.entity.Role;
import com.college.management.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebController {

    private final AuthService authService;

    public WebController(AuthService authService) {
        this.authService = authService;
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
    public String dashboard() {
        return "dashbord";
    }

    @GetMapping("/departments")
    public String departments() {
        return "department";
    }

    @GetMapping("/timetable")
    public String timetable() {
        return "timetable";
    }

    @GetMapping("/attendance")
    public String attendance() {
        return "attendance";
    }

    @GetMapping("/results")
    public String results() {
        return "results";
    }

    @GetMapping("/examinations")
    public String examinations() {
        return "examinations";
    }

    @GetMapping("/events")
    public String events() {
        return "events";
    }

    @GetMapping("/library")
    public String library() {
        return "library";
    }

    @GetMapping("/fees")
    public String fees() {
        return "fees";
    }

    @GetMapping("/services")
    public String services() {
        return "services";
    }
}
