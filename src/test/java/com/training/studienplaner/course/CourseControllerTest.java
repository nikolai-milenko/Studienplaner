package com.training.studienplaner.course;

import com.training.studienplaner.assignment.AssignmentResponseDto;
import com.training.studienplaner.security.AuthorizationService;
import com.training.studienplaner.user.UserResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @MockBean(name = "authz")
    AuthorizationService authz;

    @Test
    @DisplayName("STUDENT darf alle Kurse sehen")
    @WithMockUser(username = "student@uni.de", roles = {"STUDENT"})
    void getAllCourses_shouldReturnCourses_whenStudent() throws Exception {
        CourseResponseDto responseDto = new CourseResponseDto(
                1L, "Test Course", "Test Description", null, (short) 5, null, null
        );

        when(authz.canAccessAny(any(), eq("TEACHER"), eq("STUDENT"))).thenReturn(true);
        when(courseService.getAllCourses()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseId").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test Course"));

        verify(courseService).getAllCourses();
    }

    @Test
    @DisplayName("TEACHER darf Kurs anhand der ID sehen")
    @WithMockUser(username = "teacher@schule.de", roles = {"TEACHER"})
    void getCourseById_shouldReturnCourse_whenAuthorized() throws Exception {
        CourseResponseDto responseDto = new CourseResponseDto(
                1L, "Test Course", "Test Description", null, (short) 5, null, null
        );

        when(authz.canAccessAny(any(), eq("TEACHER"), eq("STUDENT"))).thenReturn(true);
        when(courseService.getCourseById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId").value(1L))
                .andExpect(jsonPath("$.title").value("Test Course"));

        verify(courseService).getCourseById(1L);
    }

    @Test
    @DisplayName("Soll 404 zurückgeben, wenn Kurs nicht existiert")
    @WithMockUser(username = "teacher@schule.de", roles = {"TEACHER"})
    void getCourseById_shouldReturnNotFound_whenCourseDoesNotExist() throws Exception {
        when(authz.canAccessAny(any(), eq("TEACHER"), eq("STUDENT"))).thenReturn(true);
        when(courseService.getCourseById(1L))
                .thenThrow(new EntityNotFoundException("Course not found"));

        mockMvc.perform(get("/courses/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Course not found"));

        verify(courseService).getCourseById(1L);
    }

    @Test
    @DisplayName("TEACHER darf neuen Kurs erstellen")
    @WithMockUser(username = "teacher@schule.de", roles = {"TEACHER"})
    void createCourse_shouldCreateCourse_whenTeacher() throws Exception {
        when(authz.canAccessAny(any(), eq("TEACHER"))).thenReturn(true);
        CourseResponseDto responseDto = new CourseResponseDto(
                1L, "Test Course", "Test Description", null, (short) 5, null, null
        );

        when(courseService.createCourse(any())).thenReturn(responseDto);

        mockMvc.perform(post("/courses")
                        .contentType("application/json")
                        .content("""
                        {
                            "title": "Test Course",
                            "description": "Test Description",
                            "tutorId": null,
                            "ects": 5
                        }
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.courseId").value(1L))
                .andExpect(jsonPath("$.title").value("Test Course"));

        verify(courseService).createCourse(any());
    }

    @Test
    @DisplayName("STUDENT darf keinen Kurs erstellen")
    @WithMockUser(username = "student@uni.de", roles = {"STUDENT"})
    void createCourse_shouldReturnForbidden_whenStudent() throws Exception {
        mockMvc.perform(post("/courses")
                        .contentType("application/json")
                        .content("""
                        {
                            "title": "Test Course",
                            "description": "Test Description",
                            "tutorId": null,
                            "ects": 5
                        }
                    """))
                .andExpect(status().isForbidden());

        verify(courseService, never()).createCourse(any());
    }

    @Test
    @DisplayName("ADMIN darf Kurs löschen")
    @WithMockUser(username = "admin@system.de", roles = {"ADMIN"})
    void deleteCourse_shouldDeleteCourse_whenAdmin() throws Exception {
        when(authz.canAccessAny(any())).thenReturn(true);
        mockMvc.perform(delete("/courses/1"))
                .andExpect(status().isNoContent());

        verify(courseService).deleteCourseById(1L);
    }

    @Test
    @DisplayName("TEACHER darf Kurs nicht löschen")
    @WithMockUser(username = "teacher@schule.de", roles = {"TEACHER"})
    void deleteCourse_shouldReturnForbidden_whenTeacher() throws Exception {
        mockMvc.perform(delete("/courses/1"))
                .andExpect(status().isForbidden());

        verify(courseService, never()).deleteCourseById(any());
    }

    @Test
    @DisplayName("STUDENT darf Assignments für Kurs sehen")
    @WithMockUser(username = "student@uni.de", roles = {"STUDENT"})
    void getAssignmentsByCourseId_shouldReturnAssignments_whenAuthorized() throws Exception {
        AssignmentResponseDto responseDto = new AssignmentResponseDto(
                1L, "Assignment Title", "Assignment Description", null, null, null
        );

        when(authz.canAccessAny(any(), eq("TEACHER"), eq("STUDENT"))).thenReturn(true);
        when(courseService.getAssignmentsByCourseId(1L)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/courses/1/assignments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].assignmentId").value(1L))
                .andExpect(jsonPath("$[0].title").value("Assignment Title"));

        verify(courseService).getAssignmentsByCourseId(1L);
    }


    @Test
    @DisplayName("TEACHER darf Studenten eines Kurses sehen")
    @WithMockUser(username = "teacher@schule.de", roles = {"TEACHER"})
    void getStudentsByCourseId_shouldReturnStudents_whenAuthorized() throws Exception {
        UserResponseDto responseDto = new UserResponseDto(
                1L, "John", "Doe", null, "john@example.com", null
        );

        when(authz.canAccessAny(any(), eq("TEACHER"), eq("STUDENT"))).thenReturn(true);
        when(courseService.getStudentsByCourseId(1L)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/courses/1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"));

        verify(courseService).getStudentsByCourseId(1L);
    }

    @Test
    @DisplayName("STUDENT darf Studentenliste nicht sehen")
    @WithMockUser(username = "student@uni.de", roles = {"STUDENT"})
    void getStudentsByCourseId_shouldReturnForbidden_whenStudent() throws Exception {
        mockMvc.perform(get("/courses/1/students"))
                .andExpect(status().isForbidden());

        verify(courseService, never()).getStudentsByCourseId(anyLong());
    }
}
