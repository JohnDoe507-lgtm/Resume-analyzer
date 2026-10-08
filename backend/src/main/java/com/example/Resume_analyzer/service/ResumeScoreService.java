package com.example.Resume_analyzer.service;

import org.springframework.stereotype.Service;

@Service
public class ResumeScoreService {

    public int calculateScore(String resumeText) {

        if (resumeText == null || resumeText.trim().isEmpty()) {
            return 0;
        }

        String text = resumeText.toLowerCase();

        int score = 0;

        // Contact information
        if (text.contains("email")) {
            score += 10;
        }

        if (text.contains("phone") ||
            text.contains("mobile")) {
            score += 10;
        }

        // Education
        if (text.contains("education") ||
            text.contains("degree") ||
            text.contains("b.tech") ||
            text.contains("bachelor")) {
            score += 15;
        }

        // Skills
        if (text.contains("skills") ||
            text.contains("technical skills")) {
            score += 15;
        }

        // Experience
        if (text.contains("experience") ||
            text.contains("work experience")) {
            score += 15;
        }

        // Projects
        if (text.contains("project") ||
            text.contains("projects")) {
            score += 10;
        }

        // Career objective / summary
        if (text.contains("objective") ||
            text.contains("summary")) {
            score += 10;
        }

        // Certifications
        if (text.contains("certification") ||
            text.contains("certificate")) {
            score += 5;
        }

        // Achievements
        if (text.contains("achievement") ||
            text.contains("award")) {
            score += 5;
        }

        // Limit score to 100
        if (score > 100) {
            score = 100;
        }

        return score;
    }


    public String getRating(int score) {

        if (score >= 90) {
            return "Excellent";
        }

        if (score >= 75) {
            return "Above Average";
        }

        if (score >= 60) {
            return "Good";
        }

        if (score >= 40) {
            return "Average";
        }

        return "Bad";
    }
}