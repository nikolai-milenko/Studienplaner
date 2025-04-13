package com.training.studienplaner.submission;

import com.training.studienplaner.assignment.Assignment;
import com.training.studienplaner.assignment.AssignmentMapper;
import com.training.studienplaner.assignment.AssignmentRepository;
import com.training.studienplaner.user.User;
import com.training.studienplaner.user.UserMapper;
import com.training.studienplaner.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AssignmentMapper.class, UserMapper.class})
public interface SubmissionMapper {
    // DTO → Entity
    @Mapping(target = "assignment", source = "assignmentId", qualifiedByName = "mapAssignment")
    Submission toEntity(SubmissionRequestDto dto, @Context AssignmentRepository assignmentRepository, @Context UserRepository userRepository);

    // Entity → Response DTO
    SubmissionResponseDto toResponseDto(Submission submission);
    List<SubmissionResponseDto> toResponseDto(List<Submission> submissionList);

    @Named("mapAssignment")
    default Assignment mapAssignment(Long assignmentId, @Context AssignmentRepository assignmentRepository) {
        if (assignmentId == null) {
            return null;
        }
        return assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found: " + assignmentId));
    }

    @Named("mapUser")
    default User mapUser(Long userId, @Context UserRepository userRepository) {
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    }
}
