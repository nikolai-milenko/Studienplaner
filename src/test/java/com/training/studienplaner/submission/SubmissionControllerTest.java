package com.training.studienplaner.submission;

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
@WebMvcTest(controllers = SubmissionController.class)
public class SubmissionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubmissionService submissionService;

    @MockBean
    private SubmissionMapper submissionMapper;

    @Test
    @DisplayName("Soll alle Submissions zurückgeben, wenn vorhanden")
    void getSubmissions_shouldReturnAllSubmissions_whenExist() throws Exception {
        Submission submission = mock(Submission.class);
        SubmissionResponseDto responseDto = mock(SubmissionResponseDto.class);

        when(submissionService.getAllSubmissions()).thenReturn(List.of(submission));
        when(submissionMapper.toResponseDto(List.of(submission))).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/submissions"))
                .andExpect(status().isOk());

        verify(submissionService).getAllSubmissions();
        verify(submissionMapper).toResponseDto(List.of(submission));
    }

    @Test
    @DisplayName("Soll 404 zurückgeben, wenn keine Submissions vorhanden sind")
    void getSubmissions_shouldReturnNotFound_whenNoSubmissionsExist() throws Exception {
        when(submissionService.getAllSubmissions())
                .thenThrow(new EntityNotFoundException("No submissions found"));

        mockMvc.perform(get("/submissions"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No submissions found"));

        verify(submissionService).getAllSubmissions();
    }

    @Test
    @DisplayName("Soll Submission anhand der ID zurückgeben, wenn vorhanden")
    void getSubmissionById_shouldReturnSubmission_whenExists() throws Exception {
        Submission submission = mock(Submission.class);
        SubmissionResponseDto responseDto = mock(SubmissionResponseDto.class);

        when(submissionService.getSubmissionById(1L)).thenReturn(submission);
        when(submissionMapper.toResponseDto(submission)).thenReturn(responseDto);

        mockMvc.perform(get("/submissions/1"))
                .andExpect(status().isOk());

        verify(submissionService).getSubmissionById(1L);
        verify(submissionMapper).toResponseDto(submission);
    }

    @Test
    @DisplayName("Soll 404 zurückgeben, wenn Submission nicht gefunden wird")
    void getSubmissionById_shouldReturnNotFound_whenDoesNotExist() throws Exception {
        when(submissionService.getSubmissionById(1L))
                .thenThrow(new EntityNotFoundException("Submission not found"));

        mockMvc.perform(get("/submissions/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Submission not found"));

        verify(submissionService).getSubmissionById(1L);
    }

    @Test
    @DisplayName("Soll Submission erstellen und 201 zurückgeben")
    void createSubmission_shouldCreateSubmission_andReturnCreated() throws Exception {
        SubmissionRequestDto requestDto = mock(SubmissionRequestDto.class);
        Submission submission = mock(Submission.class);
        SubmissionResponseDto responseDto = mock(SubmissionResponseDto.class);

        when(submissionMapper.toEntity(any())).thenReturn(submission);
        when(submissionService.saveSubmission(submission)).thenReturn(submission);
        when(submissionMapper.toResponseDto(submission)).thenReturn(responseDto);

        mockMvc.perform(post("/submissions")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isCreated());

        verify(submissionMapper).toEntity(any());
        verify(submissionService).saveSubmission(submission);
        verify(submissionMapper).toResponseDto(submission);
    }

    @Test
    @DisplayName("Soll 404 zurückgeben, wenn Submission nicht erstellt werden kann")
    void createSubmission_shouldReturnNotFound_whenServiceFails() throws Exception {
        Submission submission = mock(Submission.class);

        when(submissionMapper.toEntity(any())).thenReturn(submission);
        when(submissionService.saveSubmission(submission))
                .thenThrow(new EntityNotFoundException("Submission creation failed"));

        mockMvc.perform(post("/submissions")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Submission creation failed"));

        verify(submissionMapper).toEntity(any());
        verify(submissionService).saveSubmission(submission);
    }

    @Test
    @DisplayName("Soll Submission löschen und 204 zurückgeben")
    void deleteSubmission_shouldDeleteSubmission_andReturnNoContent() throws Exception {
        mockMvc.perform(delete("/submissions/1"))
                .andExpect(status().isNoContent());

        verify(submissionService).deleteSubmissionById(1L);
    }

    @Test
    @DisplayName("Soll 404 zurückgeben, wenn Submission zum Löschen nicht gefunden wird")
    void deleteSubmission_shouldReturnNotFound_whenDoesNotExist() throws Exception {
        doThrow(new EntityNotFoundException("Submission not found"))
                .when(submissionService).deleteSubmissionById(1L);

        mockMvc.perform(delete("/submissions/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Submission not found"));

        verify(submissionService).deleteSubmissionById(1L);
    }

    @Test
    @DisplayName("Soll alle Submissions für Assignment zurückgeben, wenn vorhanden")
    void getSubmissionsByAssignmentId_shouldReturnSubmissions_whenExist() throws Exception {
        Submission submission = mock(Submission.class);
        SubmissionResponseDto responseDto = mock(SubmissionResponseDto.class);

        when(submissionService.getSubmissionsByAssignmentId(1L)).thenReturn(List.of(submission));
        when(submissionMapper.toResponseDto(List.of(submission))).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/submissions/assignment/1"))
                .andExpect(status().isOk());

        verify(submissionService).getSubmissionsByAssignmentId(1L);
        verify(submissionMapper).toResponseDto(List.of(submission));
    }

    @Test
    @DisplayName("Soll 404 zurückgeben, wenn keine Submissions für Assignment vorhanden sind")
    void getSubmissionsByAssignmentId_shouldReturnNotFound_whenNoSubmissionsExist() throws Exception {
        when(submissionService.getSubmissionsByAssignmentId(1L))
                .thenThrow(new EntityNotFoundException("No submissions found"));

        mockMvc.perform(get("/submissions/assignment/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No submissions found"));

        verify(submissionService).getSubmissionsByAssignmentId(1L);
    }

    @Test
    @DisplayName("Soll alle Submissions für User zurückgeben, wenn vorhanden")
    void getSubmissionsByUserId_shouldReturnSubmissions_whenExist() throws Exception {
        Submission submission = mock(Submission.class);
        SubmissionResponseDto responseDto = mock(SubmissionResponseDto.class);

        when(submissionService.getSubmissionsByUserId(1L)).thenReturn(List.of(submission));
        when(submissionMapper.toResponseDto(List.of(submission))).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/submissions/user/1"))
                .andExpect(status().isOk());

        verify(submissionService).getSubmissionsByUserId(1L);
        verify(submissionMapper).toResponseDto(List.of(submission));
    }

    @Test
    @DisplayName("Soll 404 zurückgeben, wenn keine Submissions für User vorhanden sind")
    void getSubmissionsByUserId_shouldReturnNotFound_whenNoSubmissionsExist() throws Exception {
        when(submissionService.getSubmissionsByUserId(1L))
                .thenThrow(new EntityNotFoundException("No submissions found"));

        mockMvc.perform(get("/submissions/user/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No submissions found"));

        verify(submissionService).getSubmissionsByUserId(1L);
    }

    @Test
    @DisplayName("Soll Status der Submission aktualisieren und 200 zurückgeben")
    void updateSubmissionStatus_shouldUpdateStatus_andReturnOk() throws Exception {
        Submission submission = mock(Submission.class);
        SubmissionResponseDto responseDto = mock(SubmissionResponseDto.class);

        when(submissionService.updateSubmissionStatus(eq(1L), any())).thenReturn(submission);
        when(submissionMapper.toResponseDto(submission)).thenReturn(responseDto);

        mockMvc.perform(put("/submissions/1/status")
                        .contentType("application/json")
                        .content("\"NOT_SUBMITTED\""))
                .andExpect(status().isOk());

        verify(submissionService).updateSubmissionStatus(eq(1L), any());
        verify(submissionMapper).toResponseDto(submission);
    }

    @Test
    @DisplayName("Soll Note der Submission aktualisieren und 200 zurückgeben")
    void updateSubmissionGrade_shouldUpdateGrade_andReturnOk() throws Exception {
        Submission submission = mock(Submission.class);
        SubmissionResponseDto responseDto = mock(SubmissionResponseDto.class);

        when(submissionService.updateSubmissionGrade(eq(1L), any())).thenReturn(submission);
        when(submissionMapper.toResponseDto(submission)).thenReturn(responseDto);

        mockMvc.perform(put("/submissions/1/grade")
                        .contentType("application/json")
                        .content("1"))
                .andExpect(status().isOk());

        verify(submissionService).updateSubmissionGrade(eq(1L), any());
        verify(submissionMapper).toResponseDto(submission);
    }

}
