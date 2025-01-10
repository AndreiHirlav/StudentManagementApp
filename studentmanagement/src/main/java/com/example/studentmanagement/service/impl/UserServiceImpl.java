package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.entity.Role;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.repository.RoleRepository;
import com.example.studentmanagement.repository.UserRepository;
import com.example.studentmanagement.service.StudentService;
import com.example.studentmanagement.service.TeacherService;
import com.example.studentmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void saveUserWithRole(User user, String roleName) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleRepository.findByName(roleName);
        if(role == null) {
            role = new Role(roleName);
            roleRepository.save(role);
        }

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        user.setUltimaAccesare(LocalDateTime.now());

        userRepository.save(user);
        if ("ROLE_USER".equals(roleName)) {
            studentService.addStudentFromUser(user); // Adds to the student list
        } else if ("ROLE_ADMIN".equals(roleName)) {
            teacherService.addTeacherFromUser(user); // Adds to the teacher list
        }
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);

        if (user.getRoles().stream().anyMatch(role -> "ROLE_USER".equals(role.getName()))) {
            studentService.updateStudentFromUser(user);
        } else if (user.getRoles().stream().anyMatch(role -> "ROLE_ADMIN".equals(role.getName()))) {
            teacherService.updateTeacherFromUser(user);
        }
    }

    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }
}
