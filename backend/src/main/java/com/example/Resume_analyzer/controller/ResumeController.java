 package com.example.Resume_analyzer.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Resume_analyzer.entity.Resume;
import com.example.Resume_analyzer.repository.ResumeRepository;
import com.example.Resume_analyzer.service.ResumeScoreService;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeRepository resumeRepository;
    private final ResumeScoreService resumeScoreService;

    public ResumeController(
            ResumeRepository resumeRepository,
            ResumeScoreService resumeScoreService) {

        this.resumeRepository = resumeRepository;
        this.resumeScoreService = resumeScoreService;
    }

    // =========================
    // GET ALL RESUMES
    // =========================

    @GetMapping
    public List<Resume> getAllResumes() {

        return resumeRepository.findAll();
    }

    // =========================
    // CREATE RESUME
    // =========================

    @PostMapping
    public Resume createResume(
            @RequestBody Resume resume) {

        return resumeRepository.save(resume);
    }

    // =========================
    // DELETE RESUME
    // =========================

    @DeleteMapping("/{id}")
    public String deleteResume(
            @PathVariable Long id) {

        Resume resume = resumeRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Resume not found"
                        )
                );

        resumeRepository.delete(resume);

        return "Resume deleted successfully";
    }

    // =========================
    // GET RESUME SCORE
    // =========================

    @GetMapping("/{id}/score")
    public Map<String, Object> getResumeScore(
            @PathVariable Long id) {

        Resume resume = resumeRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Resume not found"
                        )
                );

        int score =
                resumeScoreService.calculateScore(
                        resume.getResumeText()
                );

        String rating =
                resumeScoreService.getRating(score);

        Map<String, Object> result =
                new HashMap<>();

        result.put(
                "resumeId",
                resume.getId()
        );

        result.put(
                "fileName",
                resume.getFileName()
        );

        result.put(
                "percentage",
                score
        );

        result.put(
                "rating",
                rating
        );

        return result;
    }
}