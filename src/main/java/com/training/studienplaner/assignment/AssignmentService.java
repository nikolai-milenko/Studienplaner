package com.training.studienplaner.assignment;

import com.training.studienplaner.submission.SubmissionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final SubmissionService submissionService;

    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public Assignment getAssignmentById(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found"));
    }

    public void deleteAssignmentById(Long id) {
        Assignment assignment = getAssignmentById(id);
        assignmentRepository.delete(assignment);
    }

    public Assignment createAssignment(Assignment assignment) {
        Assignment saved = assignmentRepository.save(assignment);
        submissionService.generateSubmissionsForAssignment(saved);
        return saved;
    }
}
