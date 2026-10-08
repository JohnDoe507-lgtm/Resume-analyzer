package com.example.Resume_analyzer.service;

import org.springframework.stereotype.Service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

@Service
public class GeminiService {

    private final Client client;

    public GeminiService() {
        client = new Client();
    }

    public String askGemini(String resumeText, String question) {

        String prompt = """
                You are an AI Resume Assistant.

                Analyze the user's resume and answer the user's question.

                Resume:
                %s

                User Question:
                %s

                Give a clear, useful and simple answer.
                Focus on the resume when the question is related to the resume.
                """.formatted(resumeText, question);

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-3.8-flash",
                        prompt,
                        null
                );

        return response.text();
    }
}