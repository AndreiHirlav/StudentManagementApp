package com.example.studentmanagement.service;

import com.example.studentmanagement.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void saveUserWithRole(User user, String roleName);
    User findUserByEmail(String email);
    public void save(User user);

}

