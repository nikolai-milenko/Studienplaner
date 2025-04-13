package com.training.studienplaner.course;

import com.training.studienplaner.assignment.Assignment;
import com.training.studienplaner.assignment.AssignmentMapper;
import com.training.studienplaner.assignment.AssignmentResponseDto;
import com.training.studienplaner.user.User;
import com.training.studienplaner.user.UserMapper;
import com.training.studienplaner.user.UserResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private AssignmentMapper assignmentMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private CourseService courseService;

    @Test
    @DisplayName("Soll Kurs erstellen")
    void createCourse_shouldCreateCourse() {
        CourseRequestDto requestDto = mock(CourseRequestDto.class);
        Course course = mock(Course.class);
        Course savedCourse = mock(Course.class);
        CourseResponseDto responseDto = mock(CourseResponseDto.class);

        when(courseMapper.toEntity(requestDto)).thenReturn(course);
        when(courseRepository.save(course)).thenReturn(savedCourse);
        when(courseMapper.toResponseDto(savedCourse)).thenReturn(responseDto);

        CourseResponseDto result = courseService.createCourse(requestDto);

        assertEquals(responseDto, result);
        verify(courseMapper).toEntity(requestDto);
        verify(courseRepository).save(course);
        verify(courseMapper).toResponseDto(savedCourse);
    }

    @Test
    @DisplayName("Soll alle Kurse zurückgeben")
    void getAllCourses_shouldReturnCourses() {
        Course course = mock(Course.class);
        CourseResponseDto responseDto = mock(CourseResponseDto.class);

        when(courseRepository.findAll()).thenReturn(List.of(course));
        when(courseMapper.toResponseDto(List.of(course))).thenReturn(List.of(responseDto));

        List<CourseResponseDto> result = courseService.getAllCourses();

        assertEquals(1, result.size());
        assertEquals(responseDto, result.get(0));
        verify(courseRepository).findAll();
        verify(courseMapper).toResponseDto(List.of(course));
    }

    @Test
    @DisplayName("Soll Kurs anhand der ID zurückgeben")
    void getCourseById_shouldReturnCourse() {
        Course course = mock(Course.class);
        CourseResponseDto responseDto = mock(CourseResponseDto.class);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseMapper.toResponseDto(course)).thenReturn(responseDto);

        CourseResponseDto result = courseService.getCourseById(1L);

        assertEquals(responseDto, result);
        verify(courseRepository).findById(1L);
        verify(courseMapper).toResponseDto(course);
    }

    @Test
    @DisplayName("Soll Exception werfen, wenn Kurs nicht existiert")
    void getCourseById_shouldThrowException() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> courseService.getCourseById(1L));

        assertEquals("Course not found", exception.getMessage());
        verify(courseRepository).findById(1L);
    }

    @Test
    @DisplayName("Soll Kurs löschen")
    void deleteCourseById_shouldDeleteCourse() {
        Course course = mock(Course.class);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        courseService.deleteCourseById(1L);

        verify(courseRepository).findById(1L);
        verify(courseRepository).delete(course);
    }

    @Test
    @DisplayName("Soll Assignments für Kurs zurückgeben")
    void getAssignmentsByCourseId_shouldReturnAssignments() {
        Course course = mock(Course.class);
        Assignment assignment = mock(Assignment.class);
        AssignmentResponseDto responseDto = mock(AssignmentResponseDto.class);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(course.getAssignments()).thenReturn(List.of(assignment));
        when(assignmentMapper.toResponseDto(List.of(assignment))).thenReturn(List.of(responseDto));

        List<AssignmentResponseDto> result = courseService.getAssignmentsByCourseId(1L);

        assertEquals(1, result.size());
        assertEquals(responseDto, result.get(0));
        verify(courseRepository).findById(1L);
        verify(assignmentMapper).toResponseDto(List.of(assignment));
    }

    @Test
    @DisplayName("Soll Studenten für Kurs zurückgeben")
    void getStudentsByCourseId_shouldReturnStudents() {
        Course course = mock(Course.class);
        User user = mock(User.class);
        UserResponseDto responseDto = mock(UserResponseDto.class);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(course.getStudents()).thenReturn(List.of(user));
        when(userMapper.toResponseDto(List.of(user))).thenReturn(List.of(responseDto));

        List<UserResponseDto> result = courseService.getStudentsByCourseId(1L);

        assertEquals(1, result.size());
        assertEquals(responseDto, result.get(0));
        verify(courseRepository).findById(1L);
        verify(userMapper).toResponseDto(List.of(user));
    }
}
