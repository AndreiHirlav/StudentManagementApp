package com.example.studentmanagement.controller;

import com.example.studentmanagement.entity.Assignment;
import com.example.studentmanagement.entity.File;
import com.example.studentmanagement.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @GetMapping
    public String viewAssignmentsPage(Model model) {
        // Obtine utilizatorul conectat
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal instanceof UserDetails
                ? ((UserDetails) principal).getUsername()
                : principal.toString();

        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        // Obtine toate assignmenturile
        List<Assignment> assignments = assignmentService.getAllAssignments();

        // Filtrare fisiere in functie de utilizator
        assignments.forEach(assignment -> {
            List<File> visibleFiles = isAdmin
                    ? assignment.getFiles() // Admin poate vedea toate fisierele
                    : assignment.getFiles().stream()
                    .filter(file -> file.getUploadedBy().equals(username)) // Student doar fisierele proprii
                    .toList();
            assignment.setFiles(visibleFiles); // Actualizeaza lista fișierelor vizibile
        });

        model.addAttribute("assignments", assignments);
        return "assignments";
    }

    @GetMapping("/new")
    public String showNewAssignmentForm(Model model) {
        model.addAttribute("assignment", new Assignment());
        return "create_assignment";
    }

    @PostMapping("/new")
    public String saveNewAssignment(@ModelAttribute Assignment assignment) {
        assignmentService.createAssignment(assignment);
        return "redirect:/assignments";
    }

    @GetMapping("/{id}/edit")
    public String showEditAssignmentForm(@PathVariable Long id, Model model) {
        Assignment assignment = assignmentService.getAssignmentById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid assignment Id:" + id));
        model.addAttribute("assignment", assignment);
        return "edit_assignment";
    }

    @PostMapping("/{id}/edit")
    public String updateAssignment(@PathVariable Long id, @ModelAttribute Assignment assignment) {
        assignment.setId(id);
        assignmentService.updateAssignment(id, assignment);
        return "redirect:/assignments";
    }

    @PostMapping("/{id}/delete")
    public String deleteAssignment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            assignmentService.deleteAssignment(id);
            redirectAttributes.addFlashAttribute("message", "Assignment deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting assignment: " + e.getMessage());
        }
        return "redirect:/assignments";
    }

    @GetMapping("/{id}")
    public String getAssignmentById(@PathVariable Long id, Model model) {
        Assignment assignment = assignmentService.getAssignmentById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid assignment Id:" + id));

        // Obtine utilizatorul conectat
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal instanceof UserDetails
                ? ((UserDetails) principal).getUsername()
                : principal.toString();

        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        List<File> visibleFiles = isAdmin
                ? assignment.getFiles() // Admin poate vedea toate fisierele
                : assignment.getFiles().stream()
                .filter(file -> file.getUploadedBy().equals(username)) // Student doar fisierele proprii
                .toList();

        model.addAttribute("assignment", assignment);
        model.addAttribute("files", visibleFiles);
        return "assignment-details";
    }

    @PostMapping("/{id}/upload")
    public String uploadFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        try {
            assignmentService.saveFile(id, file);
            redirectAttributes.addFlashAttribute("message", "File uploaded successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Could not upload file: " + e.getMessage());
        }
        return "redirect:/assignments";
    }

    @PostMapping("/{id}/delete-file")
    public String deleteFile(
            @PathVariable Long id,
            @RequestParam("fileName") String fileName,
            RedirectAttributes redirectAttributes) {
        try {
            assignmentService.deleteFile(id, fileName);
            redirectAttributes.addFlashAttribute("message", "File deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Could not delete file: " + e.getMessage());
        }
        return "redirect:/assignments";
    }

    @GetMapping("/download-file/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        Optional<File> fileOptional = assignmentService.getFileById(id);
        if (fileOptional.isPresent()) {
            File file = fileOptional.get();

            String fileName = file.getFileName();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(determineMediaType(fileName))
                    .body(file.getData());
        } else {
            // Returnează un răspuns 404 dacă fișierul nu este găsit
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    private MediaType determineMediaType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        switch (extension) {
            case "pdf":
                return MediaType.APPLICATION_PDF;
            case "doc":
            case "docx":
                return MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            case "xls":
            case "xlsx":
                return MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            case "png":
                return MediaType.IMAGE_PNG;
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            case "txt":
                return MediaType.TEXT_PLAIN;
            default:
                return MediaType.APPLICATION_OCTET_STREAM; // Tip generic pentru alte extensii
        }
    }

}


