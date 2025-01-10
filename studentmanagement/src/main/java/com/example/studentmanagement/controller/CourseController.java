package com.example.studentmanagement.controller;

import com.example.studentmanagement.entity.Course;
import com.example.studentmanagement.entity.File;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.entity.Teacher;
import com.example.studentmanagement.repository.TeacherRepository;
import com.example.studentmanagement.service.CourseService;
import com.example.studentmanagement.service.FileService;
import com.example.studentmanagement.service.TeacherService;
import com.example.studentmanagement.service.impl.TeacherServiceImpl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CourseController {
    private CourseService courseService;
    private TeacherService teacherService;
    private FileService fileService;

    public CourseController(CourseService courseService, TeacherService teacherService, FileService fileService) {
        this.courseService = courseService;
        this.teacherService = teacherService;
        this.fileService = fileService;
    }

    //handler method for handling students lists and return mode/view
    @GetMapping("/courses")
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());

        return "courses";
    }

    @GetMapping("/courses/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String createCourseForm(Model model) {
        //course object to hold course form data
        Course course = new Course();
        model.addAttribute("course", course);

        List<Teacher> teachers = teacherService.getAllTeachers();
        model.addAttribute("teachers", teachers);

        return "create_course";
    }

    @PostMapping("/courses")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveCourse(@ModelAttribute("course") Course course) {
        courseService.saveCourse(course);
        return "redirect:/courses";
    }

    @GetMapping("/courses/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editCourseForm(@PathVariable Long id, Model model) {
        model.addAttribute("course", courseService.getCourseById(id));

        List<Teacher> teachers = teacherService.getAllTeachers();
        model.addAttribute("teachers", teachers);
        return "edit_course";
    }

    @PostMapping("/courses/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateCourse(@PathVariable Long id,
                                @ModelAttribute("course") Course course,
                                Model model) {

        Course existingCourse = courseService.getCourseById(id);
        existingCourse.setId(id);
        existingCourse.setDenumire(course.getDenumire());
        existingCourse.setTeacher(course.getTeacher());
        existingCourse.setNrCredite(course.getNrCredite());

        courseService.updateCourse(existingCourse);
        return "redirect:/courses";
    }

    @GetMapping("/courses/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourseById(id);
        return "redirect:/courses";
    }

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("courseId") Long courseId,
                             RedirectAttributes redirectAttributes) {
        try {
            courseService.saveFile(file, courseId);
            redirectAttributes.addFlashAttribute("message", "File uploaded successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "File upload failed!");
        }
        return "redirect:/courses";
    }

    @GetMapping("/view-pdf/{id}")
    public ResponseEntity<byte[]> viewPdf(@PathVariable Long id) {
        File file = fileService.getFileById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename(file.getFileName()).build());

        return ResponseEntity.ok().headers(headers).body(file.getData());
    }

    @PostMapping("/delete-pdf/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deletePdf(@PathVariable Long id,
                            @RequestParam("courseId") Long courseId,
                            RedirectAttributes redirectAttributes) {
        try {
            fileService.deleteFile(id);
            redirectAttributes.addFlashAttribute("message", "File deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "File deletion failed!");
        }

        return "redirect:/courses";
    }
}
