package com.training.studienplaner.submission;

import jakarta.validation.Valid;
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

    @GetMapping
    public ResponseEntity<List<SubmissionResponseDto>> getSubmissions() {
        return ResponseEntity.ok(submissionService.getAllSubmissions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubmissionResponseDto> getSubmissionById(@PathVariable Long id) {
        return ResponseEntity.ok(submissionService.getSubmissionById(id));
    }

    @PostMapping
    public ResponseEntity<SubmissionResponseDto> createSubmission(@Valid @RequestBody SubmissionRequestDto submissionDto) {
        SubmissionResponseDto response = submissionService.saveSubmission(submissionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubmission(@PathVariable Long id) {
        submissionService.deleteSubmissionById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<List<SubmissionResponseDto>> getSubmissionsByAssignmentId(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(submissionService.getSubmissionsByAssignmentId(assignmentId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SubmissionResponseDto>> getSubmissionsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(submissionService.getSubmissionsByUserId(userId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<SubmissionResponseDto> updateSubmissionStatus(@PathVariable Long id, @RequestBody Submission.Status status) {
        return ResponseEntity.ok(submissionService.updateSubmissionStatus(id, status));
    }

    @PutMapping("/{id}/grade")
    public ResponseEntity<SubmissionResponseDto> updateSubmissionGrade(@PathVariable Long id, @RequestBody Double grade) {
        return ResponseEntity.ok(submissionService.updateSubmissionGrade(id, grade));
    }
}
