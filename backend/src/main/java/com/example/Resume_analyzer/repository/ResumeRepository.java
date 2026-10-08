package com.example.Resume_analyzer.repository;

import com.example.Resume_analyzer.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
}