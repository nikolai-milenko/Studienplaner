package com.training.studienplaner.assignment;

import com.training.studienplaner.course.CourseRepository;
import com.training.studienplaner.submission.SubmissionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;
    private final AssignmentMapper assignmentMapper;
    private final SubmissionService submissionService;

    public List<AssignmentResponseDto> getAllAssignments() {
        List<Assignment> assignments = assignmentRepository.findAll();
        return assignmentMapper.toResponseDto(assignments);
    }

    public AssignmentResponseDto getAssignmentById(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found"));
        return assignmentMapper.toResponseDto(assignment);
    }

    public void deleteAssignmentById(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found"));
        assignmentRepository.delete(assignment);
    }

    public AssignmentResponseDto createAssignment(AssignmentRequestDto dto) {
        Assignment assignment = assignmentMapper.toEntity(dto, courseRepository);
        Assignment saved = assignmentRepository.save(assignment);
        submissionService.generateSubmissionsForAssignment(saved);
        return assignmentMapper.toResponseDto(saved);
    }
}
