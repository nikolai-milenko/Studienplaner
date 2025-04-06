package com.training.studienplaner.submission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByAssignmentAssignmentId(Long assignmentId);
    List<Submission> findByStudentUserId(Long userId);
}
