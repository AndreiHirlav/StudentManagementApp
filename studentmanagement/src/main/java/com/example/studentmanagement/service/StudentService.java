package com.example.studentmanagement.service;

import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.entity.User;

import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();
    Student saveStudent(Student student);

    Student getStudentById(Long id);
    Student updateStudent(Student student);
    void deleteStudentById(Long id);
    Student addStudentFromUser(User user);
    void updateStudentFromUser(User user);
}
