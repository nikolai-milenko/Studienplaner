package com.training.studienplaner.course;

import com.training.studienplaner.assignment.AssignmentMapper;
import com.training.studienplaner.assignment.AssignmentResponseDto;
import com.training.studienplaner.user.UserMapper;
import com.training.studienplaner.user.UserResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final AssignmentMapper assignmentMapper;
    private final UserMapper userMapper;

    public CourseResponseDto createCourse(CourseRequestDto courseDto) {
        Course course = courseMapper.toEntity(courseDto);
        Course savedCourse = courseRepository.save(course);
        return courseMapper.toResponseDto(savedCourse);
    }

    public List<CourseResponseDto> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courseMapper.toResponseDto(courses);
    }

    public CourseResponseDto getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        return courseMapper.toResponseDto(course);
    }

    public void deleteCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        courseRepository.delete(course);
    }

    public List<AssignmentResponseDto> getAssignmentsByCourseId(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        return assignmentMapper.toResponseDto(course.getAssignments());
    }

    public List<UserResponseDto> getStudentsByCourseId(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        return userMapper.toResponseDto(course.getStudents());
    }

    public Course getCourseEntityById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
    }

}
