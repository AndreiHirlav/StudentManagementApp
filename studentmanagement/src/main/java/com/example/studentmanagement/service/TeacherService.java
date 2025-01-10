package com.example.studentmanagement.service;

import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.entity.Teacher;
import com.example.studentmanagement.entity.User;

import java.util.List;

public interface TeacherService {
    List<Teacher> getAllTeachers();
    Teacher saveTeacher(Teacher teacher);

    Teacher getTeacherById(Long id);
    Teacher updateTeacher(Teacher teacher);
    void deleteTeacherById(Long id);
    Teacher addTeacherFromUser(User user);
    void updateTeacherFromUser(User user);
}
