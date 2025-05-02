package com.training.studienplaner.assignment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @GetMapping
    @PreAuthorize("@authz.canAccessAny(principal, 'TEACHER', 'STUDENT')")
    public ResponseEntity<List<AssignmentResponseDto>> getAllAssignments() {
        List<AssignmentResponseDto> response = assignmentService.getAllAssignments();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@authz.canAccessAny(principal, 'TEACHER', 'STUDENT')")
    public ResponseEntity<AssignmentResponseDto> getAssignmentById(@PathVariable Long id) {
        AssignmentResponseDto response = assignmentService.getAssignmentById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("@authz.canAccessAny(principal, 'TEACHER')")
    public ResponseEntity<AssignmentResponseDto> createAssignment(@RequestBody AssignmentRequestDto dto) {
        AssignmentResponseDto response = assignmentService.createAssignment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authz.canAccessAny(principal, 'TEACHER')")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignmentById(id);
        return ResponseEntity.noContent().build();
    }
}
