package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.File;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.List;

public interface FileRepository extends JpaRepositoryImplementation<File, Long> {
}
