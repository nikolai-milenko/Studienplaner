package com.training.studienplaner.assignment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @GetMapping
    public ResponseEntity<List<AssignmentResponseDto>> getAllAssignments() {
        List<AssignmentResponseDto> response = assignmentService.getAllAssignments();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssignmentResponseDto> getAssignmentById(@PathVariable Long id) {
        AssignmentResponseDto response = assignmentService.getAssignmentById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AssignmentResponseDto> createAssignment(@RequestBody AssignmentRequestDto dto) {
        AssignmentResponseDto response = assignmentService.createAssignment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignmentById(id);
        return ResponseEntity.noContent().build();
    }
}
