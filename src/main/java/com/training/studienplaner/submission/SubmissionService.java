package com.training.studienplaner.submission;

import com.training.studienplaner.assignment.Assignment;
import com.training.studienplaner.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionService {
    private final SubmissionRepository submissionRepository;

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

    public void createSubmission(Submission submission) {
        submissionRepository.save(submission);
    }

    public List<Submission> getAllSubmissions() {
        return submissionRepository.findAll();
    }

    public Submission getSubmissionById(Long id) {
        return submissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Submission not found"));
    }

    public void deleteSubmissionById(Long id) {
        Submission submission = getSubmissionById(id);
        submissionRepository.delete(submission);
    }

    public List<Submission> getSubmissionsByAssignmentId(Long assignmentId) {
        return submissionRepository.findByAssignmentAssignmentId(assignmentId);
    }

    public List<Submission> getSubmissionsByUserId(Long userId) {
        return submissionRepository.findByStudentUserId(userId);
    }

    public Submission saveSubmission(Submission submission) {
        return submissionRepository.save(submission);
    }

    public Submission updateSubmissionStatus(Long id, Submission.Status status) {
        Submission submission = getSubmissionById(id);
        submission.setStatus(status);
        return submissionRepository.save(submission);
    }

    public Submission updateSubmissionGrade(Long id, Short grade) {
        Submission submission = getSubmissionById(id);
        submission.setGrade(grade);
        return submissionRepository.save(submission);
    }

}
