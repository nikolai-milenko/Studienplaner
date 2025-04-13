package com.training.studienplaner.submission;

import com.training.studienplaner.assignment.Assignment;
import com.training.studienplaner.assignment.AssignmentRepository;
import com.training.studienplaner.user.User;
import com.training.studienplaner.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final SubmissionMapper submissionMapper;

    public void generateSubmissionsForAssignment(Assignment assignment) {
        List<User> students = assignment.getCourse().getStudents().stream()
                .filter(user -> user.getRole() == User.Role.STUDENT)
                .toList();

        List<Submission> submissions = students.stream()
                .map(student -> Submission.builder()
                        .assignment(assignment)
                        .student(student)
                        .status(Submission.Status.NOT_SUBMITTED)
                        .build())
                .toList();

        submissionRepository.saveAll(submissions);
    }

    public List<SubmissionResponseDto> getAllSubmissions() {
        List<Submission> submissions = submissionRepository.findAll();
        return submissionMapper.toResponseDto(submissions);
    }

    public SubmissionResponseDto getSubmissionById(Long id) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Submission not found"));
        return submissionMapper.toResponseDto(submission);
    }

    public void deleteSubmissionById(Long id) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Submission not found"));
        submissionRepository.delete(submission);
    }

    public List<SubmissionResponseDto> getSubmissionsByAssignmentId(Long assignmentId) {
        List<Submission> submissions = submissionRepository.findByAssignmentAssignmentId(assignmentId);
        return submissionMapper.toResponseDto(submissions);
    }

    public List<SubmissionResponseDto> getSubmissionsByUserId(Long userId) {
        List<Submission> submissions = submissionRepository.findByStudentUserId(userId);
        return submissionMapper.toResponseDto(submissions);
    }

    public SubmissionResponseDto saveSubmission(SubmissionRequestDto dto) {
        /**
         * hardcode below!
         *
         * NEED to implement Spring security and use getCurrentUserId getting user id from SecurityContextHolder
         */
        Long hardcodedUserId = 1L;

        Assignment assignment = assignmentRepository.findById(dto.assignmentId())
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found"));

        User user = userRepository.findById(hardcodedUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setContent(dto.content());
        submission.setStudent(user);

        Submission saved = submissionRepository.save(submission);
        return submissionMapper.toResponseDto(saved);
    }


    public SubmissionResponseDto updateSubmissionStatus(Long id, Submission.Status status) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Submission not found"));
        submission.setStatus(status);
        Submission updated = submissionRepository.save(submission);
        return submissionMapper.toResponseDto(updated);
    }

    public SubmissionResponseDto updateSubmissionGrade(Long id, Double grade) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Submission not found"));
        submission.setGrade(grade);
        Submission updated = submissionRepository.save(submission);
        return submissionMapper.toResponseDto(updated);
    }
}
