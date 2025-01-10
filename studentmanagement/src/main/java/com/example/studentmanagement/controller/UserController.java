package com.example.studentmanagement.controller;

import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.time.format.DateTimeFormatter;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user/details")
    public String userDetails(Model model, Principal principal) {
        String email = principal.getName();
        User user = userService.findUserByEmail(email);

        if(user.getUltimaAccesare() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedDate = user.getUltimaAccesare().format(formatter);
            model.addAttribute("ultimaAccesare", formattedDate);
        }
        model.addAttribute("user", user);
        return "userDetails";
    }

    @PostMapping("/user/update")
    public String updateUserDetails(@ModelAttribute("user") User updatedUser, Principal principal) {
        String email = principal.getName();
        User existingUser = userService.findUserByEmail(email);

        if(updatedUser.getNume() != null && !updatedUser.getNume().isEmpty()) {
            existingUser.setNume(updatedUser.getNume());
        }
        if(updatedUser.getPrenume() != null && !updatedUser.getPrenume().isEmpty()) {
            existingUser.setPrenume(updatedUser.getPrenume());
        }
        if(updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
            existingUser.setEmail(updatedUser.getEmail());
        }

        userService.save(existingUser);
        return "redirect:/user/details";
    }

    @GetMapping("/user/update")
    public String editUser(Model model, Principal principal) {
        String email = principal.getName();
        User user = userService.findUserByEmail(email);
        model.addAttribute("user", user);
        return "userEdit";  // The name of the template for the edit page
    }
}
