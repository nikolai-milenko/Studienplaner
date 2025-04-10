package com.training.studienplaner.user;

import com.training.studienplaner.course.Course;
import com.training.studienplaner.course.CourseMapper;
import com.training.studienplaner.course.CourseResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private CourseMapper courseMapper;

    @Test
    @DisplayName("Soll User erstellen und 201 zurückgeben")
    void createUser_shouldCreateUser_andReturnCreated() throws Exception {
        UserRequestDto requestDto = mock(UserRequestDto.class);
        User user = mock(User.class);
        UserResponseDto responseDto = mock(UserResponseDto.class);

        when(userMapper.toEntity(any())).thenReturn(user);
        when(userService.createUser(user)).thenReturn(user);
        when(userMapper.toResponseDto(user)).thenReturn(responseDto);

        mockMvc.perform(post("/users/")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isCreated());

        verify(userMapper).toEntity(any());
        verify(userService).createUser(user);
        verify(userMapper).toResponseDto(user);
    }

    @Test
    @DisplayName("Soll alle User zurückgeben, wenn vorhanden")
    void getAllUsers_shouldReturnAllUsers_whenExist() throws Exception {
        User user = mock(User.class);
        UserResponseDto responseDto = mock(UserResponseDto.class);

        when(userService.getAll()).thenReturn(List.of(user));
        when(userMapper.toResponseDto(List.of(user))).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/users/"))
                .andExpect(status().isOk());

        verify(userService).getAll();
        verify(userMapper).toResponseDto(List.of(user));
    }

    @Test
    @DisplayName("Soll User anhand der ID zurückgeben, wenn vorhanden")
    void getUser_shouldReturnUser_whenExists() throws Exception {
        User user = mock(User.class);
        UserResponseDto responseDto = mock(UserResponseDto.class);

        when(userService.getById(1L)).thenReturn(user);
        when(userMapper.toResponseDto(user)).thenReturn(responseDto);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());

        verify(userService).getById(1L);
        verify(userMapper).toResponseDto(user);
    }

    @Test
    @DisplayName("Soll 404 zurückgeben, wenn User nicht gefunden wird")
    void getUser_shouldReturnNotFound_whenDoesNotExist() throws Exception {
        when(userService.getById(1L)).thenThrow(new EntityNotFoundException("User not found"));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));

        verify(userService).getById(1L);
    }

    @Test
    @DisplayName("Soll User löschen und 204 zurückgeben")
    void deleteUser_shouldDeleteUser_andReturnNoContent() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteById(1L);
    }

    @Test
    @DisplayName("Soll 404 zurückgeben, wenn User zum Löschen nicht gefunden wird")
    void deleteUser_shouldReturnNotFound_whenDoesNotExist() throws Exception {
        doThrow(new EntityNotFoundException("User not found"))
                .when(userService).deleteById(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));

        verify(userService).deleteById(1L);
    }

    @Test
    @DisplayName("Soll alle Studenten zurückgeben, wenn vorhanden")
    void getStudents_shouldReturnStudents_whenExist() throws Exception {
        User user = mock(User.class);
        UserResponseDto responseDto = mock(UserResponseDto.class);

        when(userService.findUsersByRole(User.Role.STUDENT)).thenReturn(List.of(user));
        when(userMapper.toResponseDto(List.of(user))).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/users/students"))
                .andExpect(status().isOk());

        verify(userService).findUsersByRole(User.Role.STUDENT);
        verify(userMapper).toResponseDto(List.of(user));
    }

    @Test
    @DisplayName("Soll 404 zurückgeben, wenn keine Studenten vorhanden sind")
    void getStudents_shouldReturnNotFound_whenNoStudentsExist() throws Exception {
        when(userService.findUsersByRole(User.Role.STUDENT))
                .thenThrow(new EntityNotFoundException("No students found"));

        mockMvc.perform(get("/users/students"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No students found"));

        verify(userService).findUsersByRole(User.Role.STUDENT);
    }

    @Test
    @DisplayName("Soll User anhand der Email zurückgeben, wenn vorhanden")
    void getUserByEmail_shouldReturnUser_whenExists() throws Exception {
        User user = mock(User.class);
        UserResponseDto responseDto = mock(UserResponseDto.class);

        when(userService.findByEmail("test@example.com")).thenReturn(user);
        when(userMapper.toResponseDto(user)).thenReturn(responseDto);

        mockMvc.perform(get("/users/email/test@example.com"))
                .andExpect(status().isOk());

        verify(userService).findByEmail("test@example.com");
        verify(userMapper).toResponseDto(user);
    }

    @Test
    @DisplayName("Soll 404 zurückgeben, wenn User anhand der Email nicht gefunden wird")
    void getUserByEmail_shouldReturnNotFound_whenDoesNotExist() throws Exception {
        when(userService.findByEmail("test@example.com"))
                .thenThrow(new EntityNotFoundException("User not found"));

        mockMvc.perform(get("/users/email/test@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));

        verify(userService).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("Soll alle Kurse für User zurückgeben, wenn vorhanden")
    void getUserCourses_shouldReturnCourses_whenExist() throws Exception {
        Course course = mock(Course.class);
        CourseResponseDto responseDto = mock(CourseResponseDto.class);

        when(userService.getAllCoursesForUser(1L)).thenReturn(List.of(course));
        when(courseMapper.toResponseDto(List.of(course))).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/users/1/courses"))
                .andExpect(status().isOk());

        verify(userService).getAllCoursesForUser(1L);
        verify(courseMapper).toResponseDto(List.of(course));
    }

    @Test
    @DisplayName("Soll User für Kurs registrieren und 200 zurückgeben")
    void createUserCourse_shouldEnrollUserToCourse_andReturnOk() throws Exception {
        mockMvc.perform(post("/users/1/courses/1"))
                .andExpect(status().isOk());

        verify(userService).enrollUserToCourse(1L, 1L);
    }

}
