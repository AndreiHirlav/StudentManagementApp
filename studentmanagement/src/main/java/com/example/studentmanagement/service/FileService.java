package com.example.studentmanagement.service;

import com.example.studentmanagement.entity.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    File getFileById(Long fileId);
    void deleteFile(Long fileId);
}
