package com.training.studienplaner.user;

import com.training.studienplaner.course.Course;
import com.training.studienplaner.course.CourseMapper;
import com.training.studienplaner.course.CourseResponseDto;
import com.training.studienplaner.course.CourseService;
import com.training.studienplaner.course.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseService courseService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Soll den Benutzer im Repository speichern und den gespeicherten Benutzer zurückgeben")
    void createUser_shouldSaveUserAndReturnSavedUser() {
        UserRequestDto requestDto = mock(UserRequestDto.class);
        User user = new User();
        UserResponseDto responseDto = mock(UserResponseDto.class);

        when(userMapper.toEntity(requestDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDto(user)).thenReturn(responseDto);

        UserResponseDto result = userService.createUser(requestDto);

        assertEquals(responseDto, result);
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Soll alle Benutzer aus dem Repository abrufen, wenn Benutzer vorhanden sind")
    void getAll_shouldReturnUsers_whenUsersExist() {
        User user = new User();
        List<User> users = List.of(user);
        List<UserResponseDto> responseDtos = List.of(mock(UserResponseDto.class));

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toResponseDto(users)).thenReturn(responseDtos);

        List<UserResponseDto> result = userService.getAll();

        assertEquals(responseDtos, result);
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Soll eine leere Liste zurückgeben, wenn keine Benutzer vorhanden sind")
    void getAll_shouldReturnEmptyList_whenNoUsersExist() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        when(userMapper.toResponseDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<UserResponseDto> result = userService.getAll();

        assertTrue(result.isEmpty());
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Soll den Benutzer zurückgeben, wenn der Benutzer mit der ID existiert")
    void getById_shouldReturnUser_whenUserExists() {
        User user = new User();
        UserResponseDto responseDto = mock(UserResponseDto.class);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponseDto(user)).thenReturn(responseDto);

        UserResponseDto result = userService.getById(1L);

        assertEquals(responseDto, result);
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Soll eine Ausnahme werfen, wenn der Benutzer mit der ID nicht gefunden wird")
    void getById_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.getById(1L));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Soll den Benutzer löschen, wenn der Benutzer existiert")
    void deleteById_shouldDeleteUser_whenUserExists() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteById(1L);

        verify(userRepository).delete(user);
    }

    @Test
    @DisplayName("Soll eine Ausnahme werfen, wenn der Benutzer nicht existiert")
    void deleteById_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.deleteById(1L));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Soll den Benutzer anhand der E-Mail-Adresse zurückgeben, wenn der Benutzer existiert")
    void findByEmail_shouldReturnUser_whenUserExists() {
        User user = new User();
        UserResponseDto responseDto = mock(UserResponseDto.class);

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(userMapper.toResponseDto(user)).thenReturn(responseDto);

        UserResponseDto result = userService.findByEmail("john@example.com");

        assertEquals(responseDto, result);
        verify(userRepository).findByEmail("john@example.com");
    }

    @Test
    @DisplayName("Soll eine Ausnahme werfen, wenn kein Benutzer mit der angegebenen E-Mail-Adresse gefunden wird")
    void findByEmail_shouldThrowException_whenNoUsersExist() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.findByEmail("john@example.com"));

        assertEquals("User not found by email", exception.getMessage());
        verify(userRepository).findByEmail("john@example.com");
    }

    @Test
    @DisplayName("Soll alle Studenten zurückgeben, wenn Studenten existieren")
    void getAllStudents_shouldReturnStudent_whenStudentExists() {
        User user = new User();
        List<User> users = List.of(user);
        List<UserResponseDto> responseDtos = List.of(mock(UserResponseDto.class));

        when(userRepository.findAllByRole(User.Role.STUDENT)).thenReturn(users);
        when(userMapper.toResponseDto(users)).thenReturn(responseDtos);

        List<UserResponseDto> result = userService.findUsersByRole(User.Role.STUDENT);

        assertEquals(responseDtos, result);
        verify(userRepository).findAllByRole(User.Role.STUDENT);
    }

    @Test
    @DisplayName("Soll eine Ausnahme werfen, wenn keine Studenten existieren")
    void getAllStudents_shouldThrowException_whenNoStudentsExist() {
        when(userRepository.findAllByRole(User.Role.STUDENT)).thenReturn(Collections.emptyList());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.findUsersByRole(User.Role.STUDENT));

        assertEquals("There are no users with role STUDENT", exception.getMessage());
        verify(userRepository).findAllByRole(User.Role.STUDENT);
    }

    @Test
    @DisplayName("Soll alle Kurse des Benutzers zurückgeben, wenn der Benutzer existiert")
    void getAllCoursesForUser_shouldReturnCourses_whenUserExists() {
        User user = new User();
        List<Course> courses = List.of(new Course());
        user.setCoursesList(courses);
        List<CourseResponseDto> courseDtos = List.of(mock(CourseResponseDto.class));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(courseMapper.toResponseDto(courses)).thenReturn(courseDtos);

        List<CourseResponseDto> result = userService.getAllCoursesForUser(1L);

        assertEquals(courseDtos, result);
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Soll eine Ausnahme werfen, wenn der Benutzer nicht gefunden wird")
    void getAllCoursesForUser_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.getAllCoursesForUser(1L));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Soll den Benutzer in den Kurs einschreiben, wenn Benutzer und Kurs existieren")
    void enrollUserToCourse_shouldEnrollUser_whenUserAndCourseExist() {
        Long userId = 1L;
        Long courseId = 2L;

        User user = new User();
        user.setCoursesList(new ArrayList<>());

        Course course = new Course();
        course.setCourseId(courseId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        userService.enrollUserToCourse(userId, courseId);

        assertTrue(user.getCoursesList().contains(course));
        verify(userRepository).findById(userId);
        verify(courseRepository).findById(courseId);
        verify(userRepository).save(user);
    }



    @Test
    @DisplayName("Soll eine Ausnahme werfen, wenn der Benutzer nicht gefunden wird")
    void enrollUserToCourse_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.enrollUserToCourse(1L, 1L));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(1L);
    }
}
