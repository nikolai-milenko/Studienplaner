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
    private final AssignmentMapper assignmentMapper;

    @GetMapping
    public ResponseEntity<List<AssignmentResponseDto>> getAllAssignments() {
        List<Assignment> assignments = assignmentService.getAllAssignments();
        List<AssignmentResponseDto> response = assignmentMapper.toResponseDto(assignments);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssignmentResponseDto> getAssignmentById(@PathVariable Long id) {
        Assignment assignment = assignmentService.getAssignmentById(id);
        AssignmentResponseDto response = assignmentMapper.toResponseDto(assignment);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AssignmentResponseDto> createAssignment(@RequestBody AssignmentRequestDto dto) {
        Assignment assignment = assignmentMapper.toEntity(dto);
        Assignment created = assignmentService.createAssignment(assignment);
        AssignmentResponseDto response = assignmentMapper.toResponseDto(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignmentById(id);
        return ResponseEntity.noContent().build();
    }
}
