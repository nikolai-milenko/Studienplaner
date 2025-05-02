package com.training.studienplaner.submission;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    @GetMapping
    @PreAuthorize("@authz.canAccessAny(principal, 'TEACHER')")
    public ResponseEntity<List<SubmissionResponseDto>> getSubmissions() {
        return ResponseEntity.ok(submissionService.getAllSubmissions());
    }

    @GetMapping("/{id}")
    @PreAuthorize("@authz.canAccessAny(principal, 'TEACHER')")
    public ResponseEntity<SubmissionResponseDto> getSubmissionById(@PathVariable Long id) {
        return ResponseEntity.ok(submissionService.getSubmissionById(id));
    }

    @PostMapping
    @PreAuthorize("@authz.canAccessAny(principal, 'STUDENT')")
    public ResponseEntity<SubmissionResponseDto> createSubmission(@Valid @RequestBody SubmissionRequestDto submissionDto) {
        SubmissionResponseDto response = submissionService.saveSubmission(submissionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authz.canAccessAny(principal, 'STUDENT')")
    public ResponseEntity<Void> deleteSubmission(@PathVariable Long id) {
        submissionService.deleteSubmissionById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/assignment/{assignmentId}")
    @PreAuthorize("@authz.canAccessAny(principal, 'TEACHER')")
    public ResponseEntity<List<SubmissionResponseDto>> getSubmissionsByAssignmentId(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(submissionService.getSubmissionsByAssignmentId(assignmentId));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("@authz.canAccessAny(principal, 'TEACHER')")
    public ResponseEntity<List<SubmissionResponseDto>> getSubmissionsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(submissionService.getSubmissionsByUserId(userId));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("@authz.canAccessAny(principal, 'TEACHER')")
    public ResponseEntity<SubmissionResponseDto> updateSubmissionStatus(@PathVariable Long id, @RequestBody Submission.Status status) {
        return ResponseEntity.ok(submissionService.updateSubmissionStatus(id, status));
    }

    @PutMapping("/{id}/grade")
    @PreAuthorize("@authz.canAccessAny(principal, 'TEACHER')")
    public ResponseEntity<SubmissionResponseDto> updateSubmissionGrade(@PathVariable Long id, @RequestBody Double grade) {
        return ResponseEntity.ok(submissionService.updateSubmissionGrade(id, grade));
    }
}
