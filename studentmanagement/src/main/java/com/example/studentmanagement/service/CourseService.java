package com.example.studentmanagement.service;

import com.example.studentmanagement.entity.Course;
import com.example.studentmanagement.entity.File;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

public interface CourseService {
    List<Course> getAllCourses();
    Course saveCourse(Course course);

    Course getCourseById(Long id);
    Course updateCourse(Course course);
    void deleteCourseById(Long id);
    void saveFile(MultipartFile file, Long courseId);
}
