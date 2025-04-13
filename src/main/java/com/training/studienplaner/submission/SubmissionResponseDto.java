package com.training.studienplaner.submission;

import com.training.studienplaner.assignment.AssignmentShortDto;
import com.training.studienplaner.user.UserShortDto;

public record SubmissionResponseDto(
        Long submissionId,
        AssignmentShortDto assignment,
        UserShortDto student,
        Submission.Status status,
        Double grade
) {
}
