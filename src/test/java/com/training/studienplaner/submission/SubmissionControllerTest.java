package com.training.studienplaner.submission;

import com.training.studienplaner.security.AuthorizationService;
import com.training.studienplaner.security.CustomUserDetails;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class SubmissionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubmissionService submissionService;

    @MockBean(name = "authz")
    AuthorizationService authz;

    @Test
    @DisplayName("ADMIN darf alle Abgaben sehen")
    @WithMockUser(username = "admin@system.de", roles = {"ADMIN"})
    void getAllSubmissions_shouldReturnList_whenAuthorized() throws Exception {
        SubmissionResponseDto dto = new SubmissionResponseDto(1L, null, null, Submission.Status.NOT_SUBMITTED, 5.0);

        when(authz.canAccessAny(any(), eq("TEACHER"))).thenReturn(true);
        when(submissionService.getAllSubmissions()).thenReturn(List.of(dto));

        mockMvc.perform(get("/submissions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].submissionId").value(1L));
    }

    @Test
    @DisplayName("STUDENT darf Abgaben nicht sehen")
    @WithMockUser(username = "student@uni.de", roles = {"STUDENT"})
    void getAllSubmissions_shouldReturnForbidden_whenStudent() throws Exception {
        mockMvc.perform(get("/submissions"))
                .andExpect(status().isForbidden());

        verify(submissionService, never()).getAllSubmissions();
    }

    @Test
    @DisplayName("ADMIN darf Abgabe anhand der ID sehen")
    @WithMockUser(username = "admin@system.de", roles = {"ADMIN"})
    void getSubmissionById_shouldReturnOne_whenAuthorized() throws Exception {
        SubmissionResponseDto dto = new SubmissionResponseDto(1L, null, null, Submission.Status.NOT_SUBMITTED, 5.0);
        when(authz.canAccessAny(any(), eq("TEACHER"))).thenReturn(true);
        when(submissionService.getSubmissionById(1L)).thenReturn(dto);

        mockMvc.perform(get("/submissions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.submissionId").value(1L));
    }

    @Test
    @DisplayName("STUDENT darf fremde Abgabe nicht sehen")
    @WithMockUser(username = "student@uni.de", roles = {"STUDENT"})
    void getSubmissionById_shouldReturnForbidden_whenStudent() throws Exception {
        mockMvc.perform(get("/submissions/1"))
                .andExpect(status().isForbidden());

        verify(submissionService, never()).getSubmissionById(anyLong());
    }

    @Test
    @DisplayName("STUDENT darf neue Abgabe erstellen")
    @WithMockUser(username = "student@uni.de", roles = {"STUDENT"})
    void createSubmission_shouldCreate_whenStudent() throws Exception {
        SubmissionResponseDto dto = new SubmissionResponseDto(1L, null, null, Submission.Status.NOT_SUBMITTED, 5.0);
        when(authz.canAccessAny(any(), eq("STUDENT"))).thenReturn(true);
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
    @DisplayName("TEACHER darf keine Abgabe erstellen")
    @WithMockUser(username = "teacher@schule.de", roles = {"TEACHER"})
    void createSubmission_shouldReturnForbidden_whenTeacher() throws Exception {
        mockMvc.perform(post("/submissions")
                        .contentType("application/json")
                        .content("""
                        {
                          "assignmentId": 1,
                          "content": "Test Content"
                        }
                    """))
                .andExpect(status().isForbidden());

        verify(submissionService, never()).saveSubmission(any());
    }

    @Test
    @DisplayName("ADMIN darf Abgabe löschen")
    @WithMockUser(username = "admin@system.de", roles = {"ADMIN"})
    void deleteSubmission_shouldReturnNoContent_whenAdmin() throws Exception {
        when(authz.canAccessAny(any(), eq("STUDENT"))).thenReturn(true);
        mockMvc.perform(delete("/submissions/1"))
                .andExpect(status().isNoContent());

        verify(submissionService).deleteSubmissionById(1L);
    }

    @Test
    @DisplayName("TEACHER darf keine Abgabe löschen")
    @WithMockUser(username = "teacher@schule.de", roles = {"TEACHER"})
    void deleteSubmission_shouldReturnForbidden_whenTeacher() throws Exception {
        mockMvc.perform(delete("/submissions/1"))
                .andExpect(status().isForbidden());

        verify(submissionService, never()).deleteSubmissionById(any());
    }

    @Test
    @DisplayName("TEACHER darf alle Abgaben für ein Assignment sehen")
    @WithMockUser(username = "teacher@schule.de", roles = {"TEACHER"})
    void getByAssignmentId_shouldReturnList_whenAuthorized() throws Exception {
        SubmissionResponseDto dto = new SubmissionResponseDto(1L, null, null, Submission.Status.NOT_SUBMITTED, 5.0);
        when(authz.canAccessAny(any(), eq("TEACHER"))).thenReturn(true);
        when(submissionService.getSubmissionsByAssignmentId(1L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/submissions/assignment/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].submissionId").value(1L));
    }

    @Test
    @DisplayName("STUDENT darf keine Abgaben für Assignment sehen")
    @WithMockUser(username = "student@uni.de", roles = {"STUDENT"})
    void getByAssignmentId_shouldReturnForbidden_whenStudent() throws Exception {
        mockMvc.perform(get("/submissions/assignment/1"))
                .andExpect(status().isForbidden());

        verify(submissionService, never()).getSubmissionsByAssignmentId(any());
    }

    @Test
    @DisplayName("TEACHER darf Abgaben eines Users sehen")
    @WithMockUser(username = "teacher@schule.de", roles = {"TEACHER"})
    void getByUserId_shouldReturnList_whenTeacher() throws Exception {
        SubmissionResponseDto dto = new SubmissionResponseDto(1L, null, null, Submission.Status.NOT_SUBMITTED, 5.0);
        when(authz.canAccessAny(any(), eq("TEACHER"))).thenReturn(true);
        when(submissionService.getSubmissionsByUserId(1L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/submissions/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].submissionId").value(1L));
    }

    @Test
    @DisplayName("STUDENT darf keine fremden Abgaben sehen")
    void getByUserId_shouldReturnForbidden_whenStudentAccessesOtherUser() throws Exception {
        com.training.studienplaner.user.User mockUser = com.training.studienplaner.user.User.builder()
                .userId(2L) // ≠ 1
                .email("student@uni.de")
                .password("dummy")
                .role(com.training.studienplaner.user.User.Role.STUDENT)
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(mockUser);

        mockMvc.perform(get("/submissions/user/1")
                        .with(user(userDetails)))
                .andExpect(status().isForbidden());

        verify(submissionService, never()).getSubmissionsByUserId(any());
    }

    @Test
    @DisplayName("TEACHER darf Status der Abgabe aktualisieren")
    @WithMockUser(username = "teacher@schule.de", roles = {"TEACHER"})
    void updateStatus_shouldWork_whenAuthorized() throws Exception {
        SubmissionResponseDto dto = new SubmissionResponseDto(1L, null, null, Submission.Status.SUBMITTED, 5.0);

        when(authz.canAccessAny(any(), eq("TEACHER"))).thenReturn(true);
        when(submissionService.updateSubmissionStatus(eq(1L), eq(Submission.Status.SUBMITTED))).thenReturn(dto);

        mockMvc.perform(put("/submissions/1/status")
                        .contentType("application/json")
                        .content("\"SUBMITTED\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUBMITTED"));
    }

    @Test
    @DisplayName("STUDENT darf Status nicht ändern")
    @WithMockUser(username = "student@uni.de", roles = {"STUDENT"})
    void updateStatus_shouldReturnForbidden_whenStudent() throws Exception {
        mockMvc.perform(put("/submissions/1/status")
                        .contentType("application/json")
                        .content("\"SUBMITTED\""))
                .andExpect(status().isForbidden());

        verify(submissionService, never()).updateSubmissionStatus(any(), any());
    }


    @Test
    @DisplayName("TEACHER darf Note der Abgabe aktualisieren")
    @WithMockUser(username = "teacher@schule.de", roles = {"TEACHER"})
    void updateGrade_shouldWork_whenAuthorized() throws Exception {
        SubmissionResponseDto dto = new SubmissionResponseDto(1L, null, null, Submission.Status.NOT_SUBMITTED, 5.0);
        when(authz.canAccessAny(any(), eq("TEACHER"))).thenReturn(true);
        when(submissionService.updateSubmissionGrade(eq(1L), eq(1.0))).thenReturn(dto);

        mockMvc.perform(put("/submissions/1/grade")
                        .contentType("application/json")
                        .content("1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grade").value(5.0)); // или .value(1.0) если dto меняется
    }

    @Test
    @DisplayName("STUDENT darf Note nicht ändern")
    @WithMockUser(username = "student@uni.de", roles = {"STUDENT"})
    void updateGrade_shouldReturnForbidden_whenStudent() throws Exception {
        mockMvc.perform(put("/submissions/1/grade")
                        .contentType("application/json")
                        .content("1"))
                .andExpect(status().isForbidden());

        verify(submissionService, never()).updateSubmissionGrade(any(), any());
    }
}
