package com.example.recruitment.service;

import com.example.recruitment.model.Profile;
import com.example.recruitment.model.User;
import com.example.recruitment.repository.ProfileRepository;
import com.example.recruitment.repository.UserRepository;
import com.example.recruitment.utils.ResumeFileValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResumeService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Value("${resume.api.url}")
    private String resumeApiUrl;

    @Value("${resume.api.key}")
    private String resumeApiKey;

    @Value("${resume.upload.dir:uploaded-resumes}")
    private String uploadDir;

    public ResumeService(ProfileRepository profileRepository,
                         UserRepository userRepository,
                         RestTemplate restTemplate) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    public Profile uploadResume(Long userId, MultipartFile file) throws IOException {
        // Validate file type
        if (!ResumeFileValidator.isValid(file)) {
            throw new IllegalArgumentException("Only PDF or DOCX resumes are accepted.");
        }

        // Construct absolute path for upload directory
        String folder = Paths.get(System.getProperty("user.dir"), uploadDir).toString();

        // Create directory if it doesn't exist
        File dir = new File(folder);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Failed to create directory: " + folder);
        }

        // Define the full path to save the file
        String filepath = Paths.get(folder, userId + "-" + file.getOriginalFilename()).toString();
        File savedFile = new File(filepath);

        try {
            file.transferTo(savedFile);
        } catch (IOException ex) {
            throw new IOException("Failed to save resume file.", ex);
        }

        // Prepare binary upload for apilayer
        FileSystemResource resource = new FileSystemResource(savedFile);

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", resumeApiKey); // apikey header must be all lowercase
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        HttpEntity<FileSystemResource> requestEntity = new HttpEntity<>(resource, headers);

        ResponseEntity<Map> response;
        try {
            response = restTemplate.exchange(
                    resumeApiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class);
        } catch (Exception ex) {
            throw new IOException("Failed to parse resume with third-party API.", ex);
        }

        Map<String, Object> responseBody = response.getBody();

        // Parse fields from API response (handle lists as comma-separated strings)
        String skills = "";
        String education = "";
        String experience = "";

        if (responseBody != null) {
            if (responseBody.get("skills") instanceof List) {
                skills = ((List<?>) responseBody.get("skills"))
                        .stream().map(Object::toString).collect(Collectors.joining(", "));
            } else if (responseBody.get("skills") != null) {
                skills = responseBody.get("skills").toString();
            }

            if (responseBody.get("education") instanceof List) {
                education = ((List<?>) responseBody.get("education"))
                        .stream().map(Object::toString).collect(Collectors.joining(", "));
            } else if (responseBody.get("education") != null) {
                education = responseBody.get("education").toString();
            }

            if (responseBody.get("experience") instanceof List) {
                experience = ((List<?>) responseBody.get("experience"))
                        .stream().map(Object::toString).collect(Collectors.joining(", "));
            } else if (responseBody.get("experience") != null) {
                experience = responseBody.get("experience").toString();
            }
        }

        // Update or create profile record
        Profile profile = profileRepository.findByUserId(userId);
        if (profile == null) {
            profile = new Profile();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            profile.setUser(user);
        }

        profile.setResumeFilePath(filepath);
        profile.setSkills(skills);
        profile.setEducation(education);
        profile.setExperience(experience);

        return profileRepository.save(profile);
    }
}
