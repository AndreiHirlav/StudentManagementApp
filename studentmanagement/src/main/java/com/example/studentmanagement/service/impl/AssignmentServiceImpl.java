package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.entity.Assignment;
import com.example.studentmanagement.entity.File;
import com.example.studentmanagement.repository.AssignmentRepository;
import com.example.studentmanagement.repository.FileRepository;
import com.example.studentmanagement.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class AssignmentServiceImpl implements AssignmentService {
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private FileRepository fileRepository;
    @Override
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    @Override
    public Optional<Assignment> getAssignmentById(Long id) {
        return assignmentRepository.findById(id);
    }

    @Override
    public Assignment createAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    @Override
    public Assignment updateAssignment(Long id, Assignment updatedAssignment) {
        return assignmentRepository.findById(id)
                .map(assignment -> {
                    assignment.setName(updatedAssignment.getName());
                    assignment.setSubject(updatedAssignment.getSubject());
                    assignment.setDeadline(updatedAssignment.getDeadline());
                    return assignmentRepository.save(assignment);
                })
                .orElseThrow(() -> new RuntimeException("Assignment not found with id " + id));

    }

    @Override
    public void deleteAssignment(Long id) {
        assignmentRepository.deleteById(id);
    }

    @Override
    public void saveFile(Long assignmentId, MultipartFile file) throws Exception {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id " + assignmentId));

        File newFile = new File();
        newFile.setFileName(file.getOriginalFilename());
        newFile.setData(file.getBytes());
        newFile.setAssignment(assignment);

        // Obține utilizatorul conectat
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal instanceof UserDetails
                ? ((UserDetails) principal).getUsername()
                : principal.toString();
        newFile.setUploadedBy(username);

        assignment.getFiles().add(newFile);
        assignmentRepository.save(assignment);
    }

    @Override
    public void deleteFile(Long assignmentId, String fileName) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id " + assignmentId));

        File fileToRemove = assignment.getFiles().stream()
                .filter(file -> file.getFileName().equals(fileName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("File not found with name " + fileName));

        assignment.getFiles().remove(fileToRemove);
        assignmentRepository.save(assignment);
    }

    public Optional<File> getFileById(Long id) {
        return fileRepository.findById(id);
    }

}
