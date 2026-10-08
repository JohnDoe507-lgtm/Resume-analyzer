package com.example.Resume_analyzer.controller;

import com.example.Resume_analyzer.entity.Resume;
import com.example.Resume_analyzer.repository.ResumeRepository;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/pdf")
public class PdfUploadController {

    private final ResumeRepository resumeRepository;

    public PdfUploadController(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    @PostMapping("/upload")
    public String uploadPdf(@RequestParam("file") MultipartFile file) {

        try {
            // Read PDF
            PDDocument document = Loader.loadPDF(file.getBytes());

            // Extract text
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            String text = pdfTextStripper.getText(document);

            // Close PDF
            document.close();

            // Create Resume object
            Resume resume = new Resume();

            // Save file name
            resume.setFileName(file.getOriginalFilename());

            // Save extracted resume text
            resume.setResumeText(text);

            // Save into MySQL
            resumeRepository.save(resume);

            return text;

        } catch (IOException e) {
            return "Error reading PDF: " + e.getMessage();
        }
    }
}