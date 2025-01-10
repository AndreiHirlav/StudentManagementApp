package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.entity.Teacher;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.repository.TeacherRepository;
import com.example.studentmanagement.repository.UserRepository;
import com.example.studentmanagement.service.TeacherService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {
    private TeacherRepository teacherRepository;
    private UserRepository userRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository, UserRepository userRepository) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    public Teacher saveTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    @Override
    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id).get();
    }

    @Override
    public Teacher updateTeacher(Teacher teacher) {
        User user = userRepository.findByEmail(teacher.getEmail());
        user.setNume(teacher.getNume());
        user.setPrenume(teacher.getPrenume());
        user.setEmail(teacher.getEmail());
        userRepository.save(user);
        return teacherRepository.save(teacher);
    }

    @Override
    public void deleteTeacherById(Long id) {
        teacherRepository.deleteById(id);
    }

    @Override
    public Teacher addTeacherFromUser(User user) {
        Teacher teacher = new Teacher();
        teacher.setNume(user.getNume());
        teacher.setPrenume(user.getPrenume());
        teacher.setEmail(user.getEmail());

        return teacherRepository.save(teacher);
    }

    @Override
    public void updateTeacherFromUser(User user) {
        Optional<Teacher> teacherSrch = teacherRepository.findByEmail(user.getEmail());
        if (teacherSrch.isPresent()) {
            Teacher teacher = teacherSrch.get();
            teacher.setNume(user.getNume());
            teacher.setPrenume(user.getPrenume());
            teacher.setEmail(user.getEmail());
            teacherRepository.save(teacher);
        }
    }
}
