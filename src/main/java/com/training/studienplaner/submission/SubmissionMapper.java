package com.training.studienplaner.submission;
import com.training.studienplaner.assignment.AssignmentMapper;
import com.training.studienplaner.user.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AssignmentMapper.class, UserMapper.class})
public interface SubmissionMapper {
    // DTO → Entity
    Submission toEntity(SubmissionRequestDto dto);

    // Entity → Response DTO
    @Mapping(source = "assignment", target = "assignment")
    @Mapping(source = "student", target = "student")
    SubmissionResponseDto toResponseDto(Submission submission);
    List<SubmissionResponseDto> toResponseDto(List<Submission> submissionList);
}
