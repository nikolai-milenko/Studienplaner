package com.training.studienplaner.course;

import com.training.studienplaner.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(target = "tutor", source = "tutorId", qualifiedByName = "mapTutor")
    Course toEntity(CourseRequestDto dto);

    CourseResponseDto toResponseDto(Course course);

    List<CourseResponseDto> toResponseDto(List<Course> courses);

    CourseShortDto toShortDto(Course course);

    @Named("mapTutor")
    default User mapTutor(Long tutorId) {
        if (tutorId == null) {
            return null;
        }
        User tutor = new User();
        tutor.setUserId(tutorId);
        return tutor;
    }
}
