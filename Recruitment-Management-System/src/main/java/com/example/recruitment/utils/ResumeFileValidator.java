package com.example.recruitment.utils;

import org.springframework.web.multipart.MultipartFile;

public class ResumeFileValidator {
    public static boolean isValid(MultipartFile file) {
        if (file == null) return false;
        String contentType = file.getContentType();
        return contentType != null &&
                (contentType.equals("application/pdf")
                        || contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                        || contentType.equals("application/msword")); // Legacy DOC format
    }
}
