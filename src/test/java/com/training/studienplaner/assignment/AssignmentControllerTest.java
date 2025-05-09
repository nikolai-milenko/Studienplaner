package com.training.studienplaner.assignment;

import com.training.studienplaner.security.AuthorizationService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class AssignmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssignmentService assignmentService;

    @MockBean(name = "authz")
    AuthorizationService authz;

    @Test
    @DisplayName("TEACHER darf alle Assignments sehen")
    @WithMockUser(username = "teacher@schule.de", roles = {"TEACHER"})
    void getAllAssignments_shouldReturnListOfAssignments_whenAuthorized() throws Exception {
        AssignmentResponseDto responseDto = new AssignmentResponseDto(
                1L,
                "Test Assignment",
                "Test description",
                Assignment.AssignmentType.HOMEWORK,
                LocalDateTime.now(),
                null
        );

        when(authz.canAccessAny(any(), eq("TEACHER"), eq("STUDENT"))).thenReturn(true);
        when(assignmentService.getAllAssignments()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/assignments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].assignmentId").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test Assignment"));

        verify(assignmentService).getAllAssignments();
    }

    @Test
    @DisplayName("STUDENT darf nicht alle Assignments sehen")
    @WithMockUser(username = "student@uni.de", roles = {"STUDENT"})
    void getAllAssignments_shouldReturnForbidden_whenStudent() throws Exception {
        mockMvc.perform(get("/assignments"))
                .andExpect(status().isForbidden());

        verify(assignmentService, never()).getAllAssignments();
    }

    @Test
    @DisplayName("Soll 500 zurückgeben, wenn beim Abrufen der Assignments ein Fehler auftritt")
    @WithMockUser(username = "teacher@schule.de", roles = {"TEACHER"})
    void getAllAssignments_shouldReturnServerError_whenServiceFails() throws Exception {
        when(authz.canAccessAny(any(), eq("TEACHER"), eq("STUDENT"))).thenReturn(true);
        when(assignmentService.getAllAssignments()).thenThrow(new RuntimeException("Service failure"));

        mockMvc.perform(get("/assignments"))
                .andExpect(status().isInternalServerError());

        verify(assignmentService).getAllAssignments();
    }

    @Test
    @DisplayName("TEACHER darf Assignment anhand der ID sehen")
    @WithMockUser(username = "teacher@schule.de", roles = {"TEACHER"})
    void getAssignmentById_shouldReturnAssignment_whenAuthorized() throws Exception {
        long id = 2L;
        AssignmentResponseDto responseDto = new AssignmentResponseDto(
                id,
                "Test Assignment",
                "Test Description",
                Assignment.AssignmentType.HOMEWORK,
                LocalDateTime.now(),
                null
        );

        when(authz.canAccessAny(any(), eq("TEACHER"), eq("STUDENT"))).thenReturn(true);
        when(assignmentService.getAssignmentById(id)).thenReturn(responseDto);

        mockMvc.perform(get("/assignments/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assignmentId").value(id))
                .andExpect(jsonPath("$.title").value("Test Assignment"));

        verify(assignmentService).getAssignmentById(id);
    }

    @Test
    @DisplayName("Soll 404 zurückgeben, wenn Assignment mit angegebener ID nicht gefunden wird")
    @WithMockUser(username = "admin@system.de", roles = {"ADMIN"})
    void getAssignmentById_shouldReturnNotFound_whenAssignmentDoesNotExist() throws Exception {
        long id = 2L;

        when(authz.canAccessAny(any(), eq("TEACHER"), eq("STUDENT"))).thenReturn(true);
        when(assignmentService.getAssignmentById(id))
                .thenThrow(new EntityNotFoundException("Assignment not found"));

        mockMvc.perform(get("/assignments/" + id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Assignment not found"));

        verify(assignmentService).getAssignmentById(id);
    }

    @Test
    @DisplayName("TEACHER darf ein Assignment erstellen")
    @WithMockUser(username = "teacher@schule.de", roles = {"TEACHER"})
    void createAssignment_shouldCreateAssignment_andReturnCreatedStatus_whenAuthorized() throws Exception {
        AssignmentRequestDto requestDto = new AssignmentRequestDto(
                "Test Assignment",
                "Test Description",
                Assignment.AssignmentType.HOMEWORK,
                LocalDateTime.now(),
                1L
        );

        AssignmentResponseDto responseDto = new AssignmentResponseDto(
                2L,
                "Test Assignment",
                "Test Description",
                Assignment.AssignmentType.HOMEWORK,
                requestDto.deadline(),
                null
        );

        when(authz.canAccessAny(any(), eq("TEACHER"))).thenReturn(true);
        when(assignmentService.createAssignment(any())).thenReturn(responseDto);

        mockMvc.perform(post("/assignments")
                        .contentType("application/json")
                        .content("""
                        {
                            "title": "Test Assignment",
                            "description": "Test Description",
                            "type": "HOMEWORK",
                            "deadline": "%s",
                            "courseId": 1
                        }
                        """.formatted(requestDto.deadline().toString())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.assignmentId").value(2L))
                .andExpect(jsonPath("$.title").value("Test Assignment"));

        verify(assignmentService).createAssignment(any());
    }

    @Test
    @DisplayName("STUDENT darf kein Assignment erstellen")
    @WithMockUser(username = "student@uni.de", roles = {"STUDENT"})
    void createAssignment_shouldReturnForbidden_whenStudent() throws Exception {
        mockMvc.perform(post("/assignments")
                        .contentType("application/json")
                        .content("""
                        {
                            "title": "Test Assignment",
                            "description": "Test Description",
                            "type": "HOMEWORK",
                            "deadline": "%s",
                            "courseId": 1
                        }
                        """.formatted(LocalDateTime.now().toString())))
                .andExpect(status().isForbidden());

        verify(assignmentService, never()).createAssignment(any());
    }

    @Test
    @DisplayName("Soll 404 zurückgeben, wenn beim Erstellen des Assignments ein Fehler auftritt")
    @WithMockUser(username = "teacher@schule.de", roles = {"TEACHER"})
    void createAssignment_shouldReturnNotFound_whenServiceThrowsEntityNotFoundException() throws Exception {
        AssignmentRequestDto requestDto = new AssignmentRequestDto(
                "Test Assignment",
                "Test Description",
                Assignment.AssignmentType.HOMEWORK,
                LocalDateTime.now(),
                1L
        );

        when(authz.canAccessAny(any(), eq("TEACHER"))).thenReturn(true);
        when(assignmentService.createAssignment(any()))
                .thenThrow(new EntityNotFoundException("Course not found"));

        mockMvc.perform(post("/assignments")
                        .contentType("application/json")
                        .content("""
                        {
                            "title": "Test Assignment",
                            "description": "Test Description",
                            "type": "HOMEWORK",
                            "deadline": "%s",
                            "courseId": 1
                        }
                        """.formatted(requestDto.deadline().toString())))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Course not found"));

        verify(assignmentService).createAssignment(any());
    }

    @Test
    @DisplayName("TEACHER darf Assignment löschen und 204 zurückbekommen")
    @WithMockUser(username = "teacher@schule.de", roles = {"TEACHER"})
    void deleteAssignment_shouldDeleteAssignment_andReturnNoContent_whenAuthorized() throws Exception {
        long id = 2L;

        when(authz.canAccessAny(any(), eq("TEACHER"))).thenReturn(true);
        mockMvc.perform(delete("/assignments/" + id))
                .andExpect(status().isNoContent());

        verify(assignmentService).deleteAssignmentById(id);
    }

    @Test
    @DisplayName("STUDENT darf Assignment nicht löschen")
    @WithMockUser(username = "student@uni.de", roles = {"STUDENT"})
    void deleteAssignment_shouldReturnForbidden_whenStudent() throws Exception {
        mockMvc.perform(delete("/assignments/2"))
                .andExpect(status().isForbidden());

        verify(assignmentService, never()).deleteAssignmentById(any());
    }

    @Test
    @DisplayName("Soll 404 zurückgeben, wenn Assignment zum Löschen nicht gefunden wird")
    @WithMockUser(username = "teacher@schule.de", roles = {"TEACHER"})
    void deleteAssignment_shouldReturnNotFound_whenAssignmentDoesNotExist() throws Exception {
        long id = 2L;

        when(authz.canAccessAny(any(), eq("TEACHER"))).thenReturn(true);
        doThrow(new EntityNotFoundException("Assignment not found"))
                .when(assignmentService).deleteAssignmentById(id);

        mockMvc.perform(delete("/assignments/" + id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Assignment not found"));

        verify(assignmentService).deleteAssignmentById(id);
    }
}
