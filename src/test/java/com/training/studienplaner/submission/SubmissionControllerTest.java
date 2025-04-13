package com.training.studienplaner.submission;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubmissionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SubmissionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubmissionService submissionService;

    @Test
    @DisplayName("Alle Abgaben sollen zurückgegeben werden")
    void getAllSubmissions_shouldReturnList() throws Exception {
        SubmissionResponseDto dto = new SubmissionResponseDto(1L, null, null, Submission.Status.NOT_SUBMITTED, 5.0);
        when(submissionService.getAllSubmissions()).thenReturn(List.of(dto));

        mockMvc.perform(get("/submissions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].submissionId").value(1L));
    }

    @Test
    @DisplayName("Abgabe anhand der ID soll gefunden werden")
    void getSubmissionById_shouldReturnOne() throws Exception {
        SubmissionResponseDto dto = new SubmissionResponseDto(1L, null, null, Submission.Status.NOT_SUBMITTED, 5.0);
        when(submissionService.getSubmissionById(1L)).thenReturn(dto);

        mockMvc.perform(get("/submissions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.submissionId").value(1L));
    }

    @Test
    @DisplayName("Neue Abgabe soll erstellt werden")
    void createSubmission_shouldCreate() throws Exception {
        SubmissionResponseDto dto = new SubmissionResponseDto(1L, null, null, Submission.Status.NOT_SUBMITTED, 5.0);
        when(submissionService.saveSubmission(any())).thenReturn(dto);

        mockMvc.perform(post("/submissions")
                        .contentType("application/json")
                        .content("""
                            {
                              "assignmentId": 1,
                              "content": "Test Content"
                            }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.submissionId").value(1L));
    }

    @Test
    @DisplayName("Abgabe soll gelöscht werden")
    void deleteSubmission_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/submissions/1"))
                .andExpect(status().isNoContent());

        verify(submissionService).deleteSubmissionById(1L);
    }

    @Test
    @DisplayName("Alle Abgaben für ein Assignment sollen gefunden werden")
    void getByAssignmentId_shouldReturnList() throws Exception {
        SubmissionResponseDto dto = new SubmissionResponseDto(1L, null, null, Submission.Status.NOT_SUBMITTED, 5.0);
        when(submissionService.getSubmissionsByAssignmentId(1L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/submissions/assignment/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].submissionId").value(1L));
    }

    @Test
    @DisplayName("Alle Abgaben eines Users sollen gefunden werden")
    void getByUserId_shouldReturnList() throws Exception {
        SubmissionResponseDto dto = new SubmissionResponseDto(1L, null, null, Submission.Status.NOT_SUBMITTED, 5.0);
        when(submissionService.getSubmissionsByUserId(1L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/submissions/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].submissionId").value(1L));
    }

    @Test
    @DisplayName("Status der Abgabe soll aktualisiert werden")
    void updateStatus_shouldWork() throws Exception {
        SubmissionResponseDto dto = new SubmissionResponseDto(1L, null, null, Submission.Status.SUBMITTED, 5.0);
        when(submissionService.updateSubmissionStatus(eq(1L), eq(Submission.Status.SUBMITTED))).thenReturn(dto);

        mockMvc.perform(put("/submissions/1/status")
                        .contentType("application/json")
                        .content("\"SUBMITTED\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUBMITTED"));
    }

    @Test
    @DisplayName("Note der Abgabe soll aktualisiert werden")
    void updateGrade_shouldWork() throws Exception {
        SubmissionResponseDto dto = new SubmissionResponseDto(1L, null, null, Submission.Status.NOT_SUBMITTED, 5.0);
        when(submissionService.updateSubmissionGrade(eq(1L), eq((Double) 1.0))).thenReturn(dto);

        mockMvc.perform(put("/submissions/1/grade")
                        .contentType("application/json")
                        .content("1"))
                .andExpect(status().isOk());
    }
}
