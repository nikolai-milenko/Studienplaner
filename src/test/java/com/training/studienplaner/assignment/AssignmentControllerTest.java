package com.training.studienplaner.assignment;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AssignmentController.class)
class AssignmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssignmentService assignmentService;

    @Test
    @DisplayName("Soll alle Assignments zurückgeben")
    void getAllAssignments_shouldReturnListOfAssignments() throws Exception {
        // given
        AssignmentResponseDto responseDto = new AssignmentResponseDto(
                1L,
                "Test Assignment",
                "Test description",
                Assignment.AssignmentType.HOMEWORK,
                LocalDateTime.now(),
                null
        );

        when(assignmentService.getAllAssignments()).thenReturn(List.of(responseDto));

        // when + then
        mockMvc.perform(get("/assignments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].assignmentId").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test Assignment"));

        verify(assignmentService).getAllAssignments();
    }

    @Test
    @DisplayName("Soll 500 zurückgeben, wenn beim Abrufen der Assignments ein Fehler auftritt")
    void getAllAssignments_shouldReturnServerError_whenServiceFails() throws Exception {
        when(assignmentService.getAllAssignments()).thenThrow(new RuntimeException("Service failure"));

        mockMvc.perform(get("/assignments"))
                .andExpect(status().isInternalServerError());

        verify(assignmentService).getAllAssignments();
    }

    @Test
    @DisplayName("Soll Assignment anhand der ID zurückgeben, wenn Assignment existiert")
    void getAssignmentById_shouldReturnAssignment_whenAssignmentExists() throws Exception {
        long id = 2L;
        AssignmentResponseDto responseDto = new AssignmentResponseDto(
                id,
                "Test Assignment",
                "Test Description",
                Assignment.AssignmentType.HOMEWORK,
                LocalDateTime.now(),
                null
        );

        when(assignmentService.getAssignmentById(id)).thenReturn(responseDto);

        mockMvc.perform(get("/assignments/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assignmentId").value(id))
                .andExpect(jsonPath("$.title").value("Test Assignment"));

        verify(assignmentService).getAssignmentById(id);
    }

    @Test
    @DisplayName("Soll 404 zurückgeben, wenn Assignment mit angegebener ID nicht gefunden wird")
    void getAssignmentById_shouldReturnNotFound_whenAssignmentDoesNotExist() throws Exception {
        long id = 2L;
        when(assignmentService.getAssignmentById(id))
                .thenThrow(new EntityNotFoundException("Assignment not found"));

        mockMvc.perform(get("/assignments/" + id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Assignment not found"));

        verify(assignmentService).getAssignmentById(id);
    }

    @Test
    @DisplayName("Soll ein Assignment erstellen und 201 zurückgeben")
    void createAssignment_shouldCreateAssignment_andReturnCreatedStatus() throws Exception {
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
    @DisplayName("Soll 404 zurückgeben, wenn beim Erstellen des Assignments ein Fehler auftritt")
    void createAssignment_shouldReturnNotFound_whenServiceThrowsEntityNotFoundException() throws Exception {
        AssignmentRequestDto requestDto = new AssignmentRequestDto(
                "Test Assignment",
                "Test Description",
                Assignment.AssignmentType.HOMEWORK,
                LocalDateTime.now(),
                1L
        );

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
    @DisplayName("Soll Assignment löschen und 204 zurückgeben")
    void deleteAssignment_shouldDeleteAssignment_andReturnNoContent() throws Exception {
        long id = 2L;

        mockMvc.perform(delete("/assignments/" + id))
                .andExpect(status().isNoContent());

        verify(assignmentService).deleteAssignmentById(id);
    }

    @Test
    @DisplayName("Soll 404 zurückgeben, wenn Assignment zum Löschen nicht gefunden wird")
    void deleteAssignment_shouldReturnNotFound_whenAssignmentDoesNotExist() throws Exception {
        long id = 2L;
        doThrow(new EntityNotFoundException("Assignment not found"))
                .when(assignmentService).deleteAssignmentById(id);

        mockMvc.perform(delete("/assignments/" + id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Assignment not found"));

        verify(assignmentService).deleteAssignmentById(id);
    }
}
