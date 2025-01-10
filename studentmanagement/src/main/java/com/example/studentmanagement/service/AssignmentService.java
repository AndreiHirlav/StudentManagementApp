package com.example.studentmanagement.service;

import com.example.studentmanagement.entity.Assignment;
import com.example.studentmanagement.entity.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface AssignmentService {
    List<Assignment> getAllAssignments();
    Optional<Assignment> getAssignmentById(Long id);
    Assignment createAssignment(Assignment assignment);
    Assignment updateAssignment(Long id, Assignment updatedAssignment);
    void deleteAssignment(Long id);
    void saveFile(Long assignmentId, MultipartFile file) throws Exception;
    public void deleteFile(Long assignmentId, String fileName);
    public Optional<File> getFileById(Long id);
}
