package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.entity.Course;
import com.example.studentmanagement.entity.File;
import com.example.studentmanagement.repository.CourseRepository;
import com.example.studentmanagement.repository.FileRepository;
import com.example.studentmanagement.service.CourseService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private CourseRepository courseRepository;
    private FileRepository fileRepository;

    public CourseServiceImpl(CourseRepository courseRepository, FileRepository fileRepository) {
        this.courseRepository = courseRepository;
        this.fileRepository = fileRepository;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).get();
    }

    @Override
    public Course updateCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public void deleteCourseById(Long id) {
        courseRepository.deleteById(id);

    }

    @Override
    public void saveFile(MultipartFile file, Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow();
        try {
            File newFile = new File();
            newFile.setFileName(file.getOriginalFilename());
            newFile.setData(file.getBytes());
            newFile.setCourse(course);

            fileRepository.save(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
