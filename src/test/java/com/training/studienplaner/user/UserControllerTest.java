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
    @DisplayName("Neuen User soll erstellt werden")
    void createUser_shouldCreateUser_andReturnCreated() throws Exception {
        UserRequestDto requestDto = mock(UserRequestDto.class);
        UserResponseDto responseDto = mock(UserResponseDto.class);

        when(userService.createUser(any())).thenReturn(responseDto);

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isCreated());

        verify(userService).createUser(any());
    }

    @Test
    @DisplayName("Alle User sollen gefunden werden")
    void getAllUsers_shouldReturnAllUsers_whenExist() throws Exception {
        UserResponseDto responseDto = mock(UserResponseDto.class);

        when(userService.getAll()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());

        verify(userService).getAll();
    }

    @Test
    @DisplayName("User anhand der ID soll gefunden werden")
    void getUser_shouldReturnUser_whenExists() throws Exception {
        UserResponseDto responseDto = mock(UserResponseDto.class);

        when(userService.getById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());

        verify(userService).getById(1L);
    }

    @Test
    @DisplayName("404 wenn User nicht gefunden wird")
    void getUser_shouldReturnNotFound_whenDoesNotExist() throws Exception {
        when(userService.getById(1L)).thenThrow(new EntityNotFoundException("User not found"));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));

        verify(userService).getById(1L);
    }

    @Test
    @DisplayName("User soll gelöscht werden")
    void deleteUser_shouldDeleteUser_andReturnNoContent() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteById(1L);
    }

    @Test
    @DisplayName("404 wenn User beim Löschen nicht gefunden wird")
    void deleteUser_shouldReturnNotFound_whenDoesNotExist() throws Exception {
        doThrow(new EntityNotFoundException("User not found"))
                .when(userService).deleteById(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));

        verify(userService).deleteById(1L);
    }

    @Test
    @DisplayName("Alle Studenten sollen gefunden werden")
    void getStudents_shouldReturnStudents_whenExist() throws Exception {
        UserResponseDto responseDto = mock(UserResponseDto.class);

        when(userService.findUsersByRole(User.Role.STUDENT)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/users/students"))
                .andExpect(status().isOk());

        verify(userService).findUsersByRole(User.Role.STUDENT);
    }

    @Test
    @DisplayName("404 wenn keine Studenten gefunden werden")
    void getStudents_shouldReturnNotFound_whenNoStudentsExist() throws Exception {
        when(userService.findUsersByRole(User.Role.STUDENT))
                .thenThrow(new EntityNotFoundException("No students found"));

        mockMvc.perform(get("/users/students"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No students found"));

        verify(userService).findUsersByRole(User.Role.STUDENT);
    }

    @Test
    @DisplayName("User anhand der Email soll gefunden werden")
    void getUserByEmail_shouldReturnUser_whenExists() throws Exception {
        UserResponseDto responseDto = mock(UserResponseDto.class);

        when(userService.findByEmail("test@example.com")).thenReturn(responseDto);

        mockMvc.perform(get("/users/email/test@example.com"))
                .andExpect(status().isOk());

        verify(userService).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("404 wenn User anhand der Email nicht gefunden wird")
    void getUserByEmail_shouldReturnNotFound_whenDoesNotExist() throws Exception {
        when(userService.findByEmail("test@example.com"))
                .thenThrow(new EntityNotFoundException("User not found"));

        mockMvc.perform(get("/users/email/test@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));

        verify(userService).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("Alle Kurse für User sollen gefunden werden")
    void getUserCourses_shouldReturnCourses_whenExist() throws Exception {
        CourseResponseDto responseDto = mock(CourseResponseDto.class);

        when(userService.getAllCoursesForUser(1L)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/users/1/courses"))
                .andExpect(status().isOk());

        verify(userService).getAllCoursesForUser(1L);
    }

    @Test
    @DisplayName("User soll zu Kurs hinzugefügt werden")
    void createUserCourse_shouldEnrollUserToCourse_andReturnOk() throws Exception {
        mockMvc.perform(post("/users/1/courses/1"))
                .andExpect(status().isOk());

        verify(userService).enrollUserToCourse(1L, 1L);
    }
}
