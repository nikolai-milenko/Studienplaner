package com.training.studienplaner.course;


import com.training.studienplaner.assignment.Assignment;
import com.training.studienplaner.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    @Captor
    private ArgumentCaptor<Course> courseCaptor;

    @Test
    @DisplayName("Soll einen Kurs erstellen und im Repository speichern")
    void saveCourse_shouldCreateCourse() {
        // given
        Course course = mock(Course.class);
        when(courseRepository.save(course)).thenReturn(course);

        //when
        Course result = courseService.createCourse(course);

        //then
        verify(courseRepository).save(course);
        assertNotNull(result);
        assertEquals(course, result);
    }

    @Test
    @DisplayName("Soll alle Kurse zurückgeben, wenn Kurse existieren")
    void getAllCourses_shouldReturnCourses_whenCoursesExist() {
        // given
        Course course1 = mock(Course.class);
        Course course2 = mock(Course.class);
        when(courseRepository.findAll()).thenReturn(List.of(course1, course2));

        // when
        List<Course> result = courseService.getAllCourses();

        // then
        assertEquals(2, result.size());
        assertEquals(course1, result.get(0));
        assertEquals(course2, result.get(1));
        verify(courseRepository).findAll();
    }

    @Test
    @DisplayName("Soll eine leere Liste zurückgeben, wenn keine Kurse existieren")
    void getAllCourses_shouldReturnEmptyList_whenNoCoursesExist() {
        // given
        when(courseRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        List<Course> result = courseService.getAllCourses();

        // then
        assertTrue(result.isEmpty());
        verify(courseRepository).findAll();
    }

    @Test
    @DisplayName("Soll Kurs anhand der ID zurückgeben, wenn Kurs existiert")
    void getCourseById_shouldReturnCourse_whenCourseExists() {
        // given
        Course course = mock(Course.class);
        long id = 1L;
        when(courseRepository.findById(id)).thenReturn(Optional.of(course));

        //when
        Course result = courseService.getCourseById(id);

        //then
        verify(courseRepository).findById(id);
        assertNotNull(result);
        assertEquals(course, result);
    }

    @Test
    @DisplayName("Soll eine Ausnahme werfen, wenn Kurs mit angegebener ID nicht gefunden wird")
    void getCourseById_shouldThrowException_whenCourseDoesNotExist() {
        // given
        long id = 1L;
        when(courseRepository.findById(id)).thenReturn(Optional.empty());

        // when + then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            courseService.getCourseById(id);
        });

        assertEquals("Course not found", exception.getMessage());
        verify(courseRepository).findById(id);
    }

    @Test
    @DisplayName("Soll Kurs löschen, wenn Kurs existiert")
    void deleteCourseById_shouldDeleteCourse_whenCourseExists() {
        // given
        long id = 1L;
        Course course = mock(Course.class);
        when(courseRepository.findById(id)).thenReturn(Optional.of(course));

        // when
        courseService.deleteCourseById(id);

        // then
        verify(courseRepository).findById(id);
        verify(courseRepository).delete(course);
    }

    @Test
    @DisplayName("Soll eine Ausnahme werfen, wenn Kurs mit angegebener ID nicht gefunden wird")
    void deleteCourseById_shouldDeleteCourse_whenCourseDoesNotExist() {
        // given
        long id = 1L;
        when(courseRepository.findById(id)).thenReturn(Optional.empty());

        // when
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            courseService.deleteCourseById(id);
        });

        // then
        assertEquals("Course not found", exception.getMessage());
        verify(courseRepository).findById(id);
    }

    @Test
    @DisplayName("Soll alle Assignments für einen Kurs zurückgeben, wenn Kurs existiert")
    void getAssignmentsByCourseId_shouldReturnAssignments_whenCourseExists() {
        // given
        long courseId = 1L;
        Assignment assignment1 = mock(Assignment.class);
        Assignment assignment2 = mock(Assignment.class);

        Course course = mock(Course.class);
        when(course.getAssignments()).thenReturn(List.of(assignment1, assignment2));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // when
        List<Assignment> result = courseService.getAssignmentsByCourseId(courseId);

        // then
        assertEquals(2, result.size());
        assertEquals(assignment1, result.get(0));
        assertEquals(assignment2, result.get(1));
        verify(courseRepository).findById(courseId);
    }

    @Test
    @DisplayName("Soll eine Ausnahme werfen, wenn Kurs mit angegebener ID nicht gefunden wird")
    void getAssignmentsByCourseId_shouldThrowException_whenCourseDoesNotExist() {
        // given
        long courseId = 1L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // when + then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            courseService.getAssignmentsByCourseId(courseId);
        });

        assertEquals("Course not found", exception.getMessage());
        verify(courseRepository).findById(courseId);
    }

    @Test
    @DisplayName("Soll alle Studenten für einen Kurs zurückgeben, wenn Kurs existiert")
    void getStudentsByCourseId_shouldReturnStudents_whenCourseExists() {
        // given
        long courseId = 1L;
        User student1 = mock(User.class);
        User student2 = mock(User.class);

        Course course = mock(Course.class);
        when(course.getStudents()).thenReturn(List.of(student1, student2));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // when
        List<User> result = courseService.getStudentsByCourseId(courseId);

        // then
        assertEquals(2, result.size());
        assertEquals(student1, result.get(0));
        assertEquals(student2, result.get(1));
        verify(courseRepository).findById(courseId);
    }

    @Test
    @DisplayName("Soll eine Ausnahme werfen, wenn Kurs mit angegebener ID nicht gefunden wird")
    void getStudentsByCourseId_shouldThrowException_whenCourseDoesNotExist() {
        // given
        long courseId = 1L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // when + then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            courseService.getStudentsByCourseId(courseId);
        });

        assertEquals("Course not found", exception.getMessage());
        verify(courseRepository).findById(courseId);
    }

}
