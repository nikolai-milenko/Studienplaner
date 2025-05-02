package com.training.studienplaner.user;

import com.training.studienplaner.course.CourseMapper;
import com.training.studienplaner.course.CourseResponseDto;
import com.training.studienplaner.security.AuthorizationService;
import com.training.studienplaner.security.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private CourseMapper courseMapper;

    @MockBean(name = "authz")
    AuthorizationService authz;

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
    @DisplayName("ADMIN darf alle User sehen")
    @WithMockUser(username = "admin@system.de", roles = {"ADMIN"})
    void getAllUsers_shouldReturnAllUsers_whenAuthorized() throws Exception {
        UserResponseDto responseDto = mock(UserResponseDto.class);

        when(authz.canAccessAny(any(Authentication.class))).thenReturn(true);
        when(userService.getAll()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());

        verify(userService).getAll();
    }

    @Test
    @DisplayName("TEACHER darf User anhand ID sehen")
    @WithMockUser(username = "teacher@uni.de", roles = {"TEACHER"})
    void getUser_shouldReturnUser_whenTeacherAuthorized() throws Exception {
        UserResponseDto responseDto = mock(UserResponseDto.class);

        when(authz.canAccessAny(any(), eq("TEACHER"))).thenReturn(true);
        when(userService.getById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());

        verify(userService).getById(1L);
    }

    @Test
    @DisplayName("STUDENT darf keinen anderen User sehen")
    @WithMockUser(username = "student@uni.de", roles = {"STUDENT"})
    void getUser_shouldReturnForbidden_whenStudent() throws Exception {
        when(authz.canAccessAny(any(), eq("TEACHER"))).thenReturn(false);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isForbidden());

        verify(userService, never()).getById(1L);
    }

    @Test
    @DisplayName("404 wenn User nicht gefunden wird (für TEACHER)")
    @WithMockUser(username = "teacher@uni.de", roles = {"TEACHER"})
    void getUser_shouldReturnNotFound_whenDoesNotExist() throws Exception {
        when(authz.canAccessAny(any(), eq("TEACHER"))).thenReturn(true);
        when(userService.getById(1L)).thenThrow(new EntityNotFoundException("User not found"));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));

        verify(userService).getById(1L);
    }

    @Test
    @DisplayName("ADMIN darf User löschen")
    @WithMockUser(username = "admin@system.de", roles = {"ADMIN"})
    void deleteUser_shouldDeleteUser_andReturnNoContent_whenAuthorized() throws Exception {
        when(authz.canAccessAny(any())).thenReturn(true);
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteById(1L);
    }

    @Test
    @DisplayName("STUDENT darf keinen User löschen")
    @WithMockUser(username = "student@uni.de", roles = {"STUDENT"})
    void deleteUser_shouldReturnForbidden_whenUnauthorized() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isForbidden());

        verify(userService, never()).deleteById(any());
    }

    @Test
    @DisplayName("404 wenn User beim Löschen nicht gefunden wird (für ADMIN)")
    @WithMockUser(username = "admin@system.de", roles = {"ADMIN"})
    void deleteUser_shouldReturnNotFound_whenDoesNotExist() throws Exception {
        when(authz.canAccessAny(any())).thenReturn(true);
        doThrow(new EntityNotFoundException("User not found"))
                .when(userService).deleteById(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));

        verify(userService).deleteById(1L);
    }

    @Test
    @DisplayName("TEACHER darf alle Studenten sehen")
    @WithMockUser(username = "teacher@uni.de", roles = {"TEACHER"})
    void getStudents_shouldReturnStudents_whenAuthorized() throws Exception {
        UserResponseDto responseDto = mock(UserResponseDto.class);

        when(authz.canAccessAny(any(), eq("TEACHER"))).thenReturn(true);
        when(userService.findUsersByRole(User.Role.STUDENT)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/users/students"))
                .andExpect(status().isOk());

        verify(userService).findUsersByRole(User.Role.STUDENT);
    }

    @Test
    @DisplayName("STUDENT darf nicht alle Studenten sehen")
    @WithMockUser(username = "student@uni.de", roles = {"STUDENT"})
    void getStudents_shouldReturnForbidden_whenNotAuthorized() throws Exception {
        mockMvc.perform(get("/users/students"))
                .andExpect(status().isForbidden());

        verify(userService, never()).findUsersByRole(any());
    }

    @Test
    @DisplayName("404 wenn keine Studenten gefunden werden (für TEACHER)")
    @WithMockUser(username = "teacher@uni.de", roles = {"TEACHER"})
    void getStudents_shouldReturnNotFound_whenNoStudentsExist() throws Exception {
        when(authz.canAccessAny(any(), eq("TEACHER"))).thenReturn(true);
        when(userService.findUsersByRole(User.Role.STUDENT))
                .thenThrow(new EntityNotFoundException("No students found"));

        mockMvc.perform(get("/users/students"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No students found"));

        verify(userService).findUsersByRole(User.Role.STUDENT);
    }

    @Test
    @DisplayName("ADMIN darf User anhand Email sehen")
    @WithMockUser(username = "admin@system.de", roles = {"ADMIN"})
    void getUserByEmail_shouldReturnUser_whenAuthorized() throws Exception {
        UserResponseDto responseDto = mock(UserResponseDto.class);

        when(authz.canAccessAny(any(), eq("TEACHER"))).thenReturn(true);
        when(userService.findByEmail("test@example.com")).thenReturn(responseDto);

        mockMvc.perform(get("/users/email/test@example.com"))
                .andExpect(status().isOk());

        verify(userService).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("STUDENT darf nicht User anhand Email suchen")
    @WithMockUser(username = "student@uni.de", roles = {"STUDENT"})
    void getUserByEmail_shouldReturnForbidden_whenUnauthorized() throws Exception {
        mockMvc.perform(get("/users/email/test@example.com"))
                .andExpect(status().isForbidden());

        verify(userService, never()).findByEmail(any());
    }

    @Test
    @DisplayName("404 wenn User anhand der Email nicht gefunden wird (für ADMIN)")
    void getUserByEmail_shouldReturnNotFound_whenDoesNotExist() throws Exception {
        User mockUser = User.builder()
                .userId(99L)
                .email("admin@system.de")
                .password("dummy")
                .role(User.Role.ADMIN)
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(mockUser);

        when(authz.canAccessAny(any(), eq("TEACHER"))).thenReturn(true);
        when(userService.findByEmail("test@example.com"))
                .thenThrow(new EntityNotFoundException("User not found"));

        mockMvc.perform(get("/users/email/test@example.com")
                        .with(user(userDetails)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));

        verify(userService).findByEmail("test@example.com");
    }


    @Test
    @DisplayName("ADMIN darf alle Kurse eines Users sehen")
    void getUserCourses_shouldReturnCourses_whenAuthorized() throws Exception {
        CourseResponseDto responseDto = mock(CourseResponseDto.class);

        User mockUser = User.builder()
                .userId(99L)
                .email("admin@system.de")
                .password("dummy")
                .role(User.Role.ADMIN)
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(mockUser);

        when(userService.getAllCoursesForUser(1L)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/users/1/courses")
                        .with(user(userDetails)))
                .andExpect(status().isOk());

        verify(userService).getAllCoursesForUser(1L);
    }

    @Test
    @DisplayName("STUDENT darf keine fremden Kurse sehen")
    @WithMockUser(username = "student@uni.de", roles = {"STUDENT"})
    void getUserCourses_shouldReturnForbidden_whenStudentAccessesOtherUser() throws Exception {
        mockMvc.perform(get("/users/1/courses"))
                .andExpect(status().isForbidden());

        verify(userService, never()).getAllCoursesForUser(any());
    }

    @Test
    @DisplayName("ADMIN darf User zu Kurs hinzufügen")
    void createUserCourse_shouldEnrollUserToCourse_whenAdmin() throws Exception {
        User mockUser = User.builder()
                .userId(1L)
                .email("admin@system.de")
                .password("dummy")
                .role(User.Role.ADMIN)
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(mockUser);

        mockMvc.perform(post("/users/1/courses/1")
                        .with(user(userDetails)))
                .andExpect(status().isOk());

        verify(userService).enrollUserToCourse(1L, 1L);
    }

    @Test
    @DisplayName("STUDENT darf sich selbst zu Kurs hinzufügen")
    void createUserCourse_shouldEnrollSelfToCourse_whenStudent() throws Exception {
        com.training.studienplaner.user.User mockUser = com.training.studienplaner.user.User.builder()
                .userId(1L)
                .email("student@uni.de")
                .password("dummy")
                .role(com.training.studienplaner.user.User.Role.STUDENT)
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(mockUser);

        mockMvc.perform(post("/users/1/courses/1")
                        .with(user(userDetails)))
                .andExpect(status().isOk());

        verify(userService).enrollUserToCourse(1L, 1L);
    }

    @Test
    @DisplayName("STUDENT darf keinen anderen User zu Kurs hinzufügen")
    void createUserCourse_shouldReturnForbidden_whenStudentAccessesOtherUser() throws Exception {
        com.training.studienplaner.user.User mockUser = com.training.studienplaner.user.User.builder()
                .userId(2L)
                .email("student@uni.de")
                .password("dummy")
                .role(com.training.studienplaner.user.User.Role.STUDENT)
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(mockUser);

        mockMvc.perform(post("/users/1/courses/1")
                        .with(user(userDetails)))
                .andExpect(status().isForbidden());

        verify(userService, never()).enrollUserToCourse(any(), any());
    }
}
