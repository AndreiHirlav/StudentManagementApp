package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.entity.Course;
import com.example.studentmanagement.entity.File;
import com.example.studentmanagement.repository.CourseRepository;
import com.example.studentmanagement.repository.FileRepository;
import com.example.studentmanagement.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private CourseRepository courseRepository;

    public FileServiceImpl(FileRepository fileRepository, CourseRepository courseRepository) {
        this.fileRepository = fileRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public File getFileById(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("Fișierul nu a fost găsit"));
    }

    @Override
    public void deleteFile(Long fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("Fisierul nu a fost gasit"));
        fileRepository.delete(file);
    }
}
