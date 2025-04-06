package com.training.studienplaner.submission;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;
    private final SubmissionMapper submissionMapper;

    @GetMapping
    public ResponseEntity<List<SubmissionResponseDto>> getSubmissions() {
        List<Submission> submissions = submissionService.getAllSubmissions();
        List<SubmissionResponseDto> response = submissionMapper.toResponseDto(submissions);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubmissionResponseDto> getSubmissionById(@PathVariable Long id) {
        Submission submission = submissionService.getSubmissionById(id);
        SubmissionResponseDto response = submissionMapper.toResponseDto(submission);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<SubmissionResponseDto> createSubmission(@RequestBody SubmissionRequestDto submissionDto) {
        Submission submission = submissionMapper.toEntity(submissionDto);
        Submission savedSubmission = submissionService.saveSubmission(submission);
        SubmissionResponseDto response = submissionMapper.toResponseDto(savedSubmission);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubmission(@PathVariable Long id) {
        submissionService.deleteSubmissionById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<List<SubmissionResponseDto>> getSubmissionsByAssignmentId(@PathVariable Long assignmentId) {
        List<Submission> submissions = submissionService.getSubmissionsByAssignmentId(assignmentId);
        List<SubmissionResponseDto> response = submissionMapper.toResponseDto(submissions);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SubmissionResponseDto>> getSubmissionsByUserId(@PathVariable Long userId) {
        List<Submission> submissions = submissionService.getSubmissionsByUserId(userId);
        List<SubmissionResponseDto> response = submissionMapper.toResponseDto(submissions);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<SubmissionResponseDto> updateSubmissionStatus(@PathVariable Long id, @RequestBody Submission.Status status) {
        Submission updatedSubmission = submissionService.updateSubmissionStatus(id, status);
        SubmissionResponseDto response = submissionMapper.toResponseDto(updatedSubmission);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/grade")
    public ResponseEntity<SubmissionResponseDto> updateSubmissionGrade(@PathVariable Long id, @RequestBody Short grade) {
        Submission updatedSubmission = submissionService.updateSubmissionGrade(id, grade);
        SubmissionResponseDto response = submissionMapper.toResponseDto(updatedSubmission);
        return ResponseEntity.ok(response);
    }
}
