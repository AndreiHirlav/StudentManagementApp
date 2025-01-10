package com.example.studentmanagement.controller;

import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.service.UserService;
import com.example.studentmanagement.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@ModelAttribute("user") User user,
                               @RequestParam("role") String role,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        User existingUser = userService.findUserByEmail(user.getEmail());

        if (existingUser != null && existingUser.getEmail() != null) {
            result.rejectValue("email", null, "There is already an account with the same email");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }

        userService.saveUserWithRole(user, role);

        return "redirect:/login";
    }
}

