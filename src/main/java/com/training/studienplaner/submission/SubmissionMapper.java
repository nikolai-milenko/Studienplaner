package com.training.studienplaner.submission;
import com.training.studienplaner.assignment.AssignmentMapper;
import com.training.studienplaner.user.UserMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AssignmentMapper.class, UserMapper.class})
public interface SubmissionMapper {
    // DTO → Entity
    Submission toEntity(SubmissionRequestDto dto);

    // Entity → Response DTO
    SubmissionResponseDto toResponseDto(Submission submission);
    List<SubmissionResponseDto> toResponseDto(List<Submission> submissionList);
}
