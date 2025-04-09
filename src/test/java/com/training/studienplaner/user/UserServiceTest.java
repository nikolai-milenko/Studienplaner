package com.training.studienplaner.user;

import com.training.studienplaner.course.Course;
import com.training.studienplaner.course.CourseService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    @DisplayName("Soll den Benutzer im Repository speichern und den gespeicherten Benutzer zurückgeben")
    void createUser_shouldSaveUserAndReturnSavedUser() {
        // given
        User user = new User();
        user.setName("John");
        user.setEmail("john@example.com");

        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        User result = userService.createUser(user);

        // then
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertEquals("John", capturedUser.getName());
        assertEquals("john@example.com", capturedUser.getEmail());
        assertEquals(user, result);
    }

    @Test
    @DisplayName("Soll eine Ausnahme werfen, wenn das Speichern des Benutzers im Repository fehlschlägt")
    void createUser_shouldThrowException_whenRepositoryFails() {
        // given
        User user = new User();
        user.setName("John");
        user.setEmail("john@example.com");

        when(userRepository.save(user))
                .thenThrow(new DataIntegrityViolationException("Duplicate email"));

        // when + then
        assertThrows(DataIntegrityViolationException.class, () -> {
            userService.createUser(user);
        });

        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Soll alle Benutzer aus dem Repository abrufen, wenn Benutzer vorhanden sind")
    void getAll_shouldReturnUsers_whenUsersExist() {
        User user = new User();
        user.setName("John");
        user.setEmail("john@example.com");

        User user2 = new User();
        user2.setName("Jane");
        user2.setEmail("jane@example.com");

        userRepository.save(user);
        userRepository.save(user2);

        when(userRepository.findAll()).thenReturn(List.of(user, user2));

        //when
        List<User> result = userService.getAll();

        //then
        assertEquals(2, result.size());
        assertTrue(result.contains(user));
        assertTrue(result.contains(user2));
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Soll eine leere Liste zurückgeben, wenn keine Benutzer vorhanden sind")
    void getAll_shouldReturnEmptyList_whenNoUsersExist() {
        // given
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        List<User> result = userService.getAll();

        // then
        assertTrue(result.isEmpty());
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Soll den Benutzer zurückgeben, wenn der Benutzer mit der ID existiert")
    void getById_shouldReturnUser_whenUserExists() {
        // given
        Long id = 1L;
        User user = new User();
        user.setUserId(id);
        user.setName("John");
        user.setEmail("john@example.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // when
        User result = userService.getById(id);

        // then
        assertEquals(user, result);
        verify(userRepository).findById(id);
    }

    @Test
    @DisplayName("Soll eine Ausnahme werfen, wenn der Benutzer mit der ID nicht gefunden wird")
    void getById_shouldThrowException_whenUserNotFound() {
        // given
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // when + then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.getById(id);
        });

        // optional
        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findById(id);
    }

    @Test
    @DisplayName("Soll den Benutzer löschen, wenn der Benutzer existiert")
    void deleteById_shouldDeleteUser_whenUserExists() {
        // given
        Long id = 1L;
        when(userRepository.existsById(id)).thenReturn(true);

        // when
        userService.deleteById(id);

        // then
        verify(userRepository).existsById(id);
        verify(userRepository).deleteById(id);
    }

    @Test
    @DisplayName("Soll eine Ausnahme werfen, wenn der Benutzer nicht existiert")
    void deleteById_shouldThrowException_whenUserNotFound() {
        // given
        Long id = 1L;
        when(userRepository.existsById(id)).thenReturn(false);

        // when + then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.deleteById(id);
        });

        assertEquals("User not found", exception.getMessage());

        // and verify
        verify(userRepository).existsById(id);
        verify(userRepository, never()).deleteById(id);
    }

    @Test
    @DisplayName("Soll den Benutzer anhand der E-Mail-Adresse zurückgeben, wenn der Benutzer existiert")
    void findByEmail_shouldReturnUser_whenUserExists() {
        // given
        User user = new User();
        user.setName("John");
        user.setEmail("john@example.com");
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        // when
        User result = userService.findByEmail("john@example.com");

        //then
        assertEquals(user, result);
        verify(userRepository).findByEmail("john@example.com");
    }

    @Test
    @DisplayName("Soll eine Ausnahme werfen, wenn kein Benutzer mit der angegebenen E-Mail-Adresse gefunden wird")
    void findByEmail_shouldThrowException_whenNoUsersExist() {
        String email = "john@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        //when + then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.findByEmail(email);
        });

        assertEquals("User not found by email", exception.getMessage());
        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Soll alle Studenten zurückgeben, wenn Studenten existieren")
    void getAllStudents_shouldReturnStudent_whenStudentExists() {
        // given
        User user = new User();
        user.setRole(User.Role.STUDENT);
        User user2 = new User();
        user2.setRole(User.Role.STUDENT);

        List<User> students = List.of(user, user2);
        when(userRepository.findAllByRole(User.Role.STUDENT)).thenReturn(List.of(user, user2));

        //when
        List<User> result = userService.findUsersByRole(User.Role.STUDENT);

        // then
        assertFalse(result.isEmpty());
        assertEquals(students, result);
        verify(userRepository).findAllByRole(User.Role.STUDENT);
    }

    @Test
    @DisplayName("Soll eine Ausnahme werfen, wenn keine Studenten existieren")
    void getAllStudents_shouldThrowException_whenNoStudentsExist() {
        // given
        when(userRepository.findAllByRole(User.Role.STUDENT)).thenReturn(Collections.emptyList());

        // when + then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.findUsersByRole(User.Role.STUDENT);
        });

        assertEquals("There are no users with role " + User.Role.STUDENT, exception.getMessage());
        verify(userRepository).findAllByRole(User.Role.STUDENT);
    }

    @Test
    @DisplayName("Soll alle Kurse des Benutzers zurückgeben, wenn der Benutzer existiert")
    void getAllCoursesForUser_shouldReturnCourses_whenUserExists() {
        // given
        Long userId = 1L;

        Course course1 = mock(Course.class);
        Course course2 = mock(Course.class);
        List<Course> courses = List.of(course1, course2);

        User user = new User();
        user.setCoursesList(courses);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        List<Course> result = userService.getAllCoursesForUser(userId);

        // then
        assertEquals(courses, result);
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Soll eine Ausnahme werfen, wenn der Benutzer nicht gefunden wird")
    void getAllCoursesForUser_shouldThrowException_whenUserNotFound() {
        // given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when + then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.getAllCoursesForUser(userId);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Soll den Benutzer in den Kurs einschreiben, wenn Benutzer und Kurs existieren")
    void enrollUserToCourse_shouldEnrollUser_whenUserAndCourseExist() {
        // given
        Long userId = 1L;
        Long courseId = 2L;

        User user = new User();
        user.setCoursesList(new ArrayList<>());

        Course course = mock(Course.class);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(courseService.getCourseById(courseId)).thenReturn(course);

        // when
        userService.enrollUserToCourse(userId, courseId);

        // then
        assertTrue(user.getCoursesList().contains(course));
        verify(userRepository).findById(userId);
        verify(courseService).getCourseById(courseId);
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Soll eine Ausnahme werfen, wenn der Benutzer nicht gefunden wird")
    void enrollUserToCourse_shouldThrowException_whenUserNotFound() {
        // given
        Long userId = 1L;
        Long courseId = 2L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when + then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.enrollUserToCourse(userId, courseId);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(userId);
        verify(courseService, never()).getCourseById(anyLong());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Soll eine Ausnahme werfen, wenn der Kurs nicht gefunden wird")
    void enrollUserToCourse_shouldThrowException_whenCourseNotFound() {
        // given
        Long userId = 1L;
        Long courseId = 2L;

        User user = new User();
        user.setCoursesList(new ArrayList<>());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(courseService.getCourseById(courseId)).thenThrow(new EntityNotFoundException("Course not found"));

        // when + then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.enrollUserToCourse(userId, courseId);
        });

        assertEquals("Course not found", exception.getMessage());
        verify(userRepository).findById(userId);
        verify(courseService).getCourseById(courseId);
        verify(userRepository, never()).save(any());
    }

}
