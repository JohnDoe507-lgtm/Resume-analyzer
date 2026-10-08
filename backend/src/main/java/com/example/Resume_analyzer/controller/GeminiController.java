package com.example.Resume_analyzer.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Resume_analyzer.service.GeminiService;

@RestController
@RequestMapping("/api/gemini")
@CrossOrigin(origins = "http://localhost:5173")
public class GeminiController {

    private final GeminiService geminiService;

    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/chat")
    public String chat(@RequestBody Map<String, String> request) {

        String resumeText = request.get("resumeText");
        String question = request.get("question");

        return geminiService.askGemini(
                resumeText,
                question
        );
    }
}