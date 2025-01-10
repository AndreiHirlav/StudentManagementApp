package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.repository.UserRepository;
import com.example.studentmanagement.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    private StudentRepository studentRepository;
    private UserRepository userRepository;

    public StudentServiceImpl(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).get();
    }

    @Override
    public Student updateStudent(Student student) {
        User user = userRepository.findByEmail(student.getEmail());
        user.setNume(student.getNume());
        user.setPrenume(student.getPrenume());
        user.setEmail(student.getEmail());
        userRepository.save(user);
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudentById(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public Student addStudentFromUser(User user) {
        Student student = new Student();
        student.setNume(user.getNume());
        student.setPrenume(user.getPrenume());
        student.setEmail(user.getEmail());

        return studentRepository.save(student);
    }

    @Override
    public void updateStudentFromUser(User user) {
        Optional<Student> studentSrch = studentRepository.findByEmail(user.getEmail());
        if(studentSrch.isPresent()) {
            Student student = studentSrch.get();
            student.setNume(user.getNume());
            student.setPrenume(user.getPrenume());
            student.setEmail(user.getEmail());
            studentRepository.save(student);
        }
    }
}
