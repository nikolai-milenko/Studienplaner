package com.training.studienplaner.submission;

import com.training.studienplaner.assignment.Assignment;
import com.training.studienplaner.assignment.AssignmentRepository;
import com.training.studienplaner.user.User;
import com.training.studienplaner.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubmissionServiceTest {

    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubmissionMapper submissionMapper;

    @InjectMocks
    private SubmissionService submissionService;

    @Test
    @DisplayName("Alle Abgaben sollen zurückgegeben werden")
    void getAllSubmissions_shouldReturnDtos() {
        Submission entity = new Submission();
        SubmissionResponseDto dto = new SubmissionResponseDto(1L, null, null, Submission.Status.NOT_SUBMITTED, 5.0);

        when(submissionRepository.findAll()).thenReturn(List.of(entity));
        when(submissionMapper.toResponseDto(List.of(entity))).thenReturn(List.of(dto));

        List<SubmissionResponseDto> result = submissionService.getAllSubmissions();

        assertEquals(1, result.size());
        verify(submissionRepository).findAll();
    }

    @Test
    @DisplayName("Abgabe anhand der ID soll gefunden werden")
    void getSubmissionById_shouldReturnDto() {
        Submission entity = new Submission();
        SubmissionResponseDto dto = new SubmissionResponseDto(1L, null, null, Submission.Status.NOT_SUBMITTED, 5.0);

        when(submissionRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(submissionMapper.toResponseDto(entity)).thenReturn(dto);

        SubmissionResponseDto result = submissionService.getSubmissionById(1L);

        assertNotNull(result);
        verify(submissionRepository).findById(1L);
    }

    @Test
    @DisplayName("Neue Abgabe soll gespeichert werden")
    void saveSubmission_shouldConvertAndSave() {
        SubmissionRequestDto dto = new SubmissionRequestDto(1L, "text");
        Submission entity = new Submission();
        Submission saved = new Submission();
        SubmissionResponseDto resultDto = new SubmissionResponseDto(1L, null, null, Submission.Status.NOT_SUBMITTED, 5.0);

        Assignment assignment = new Assignment();
        User user = new User();

        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user)); // hardcoded userId = 1L

        when(submissionRepository.save(any(Submission.class))).thenReturn(saved);
        when(submissionMapper.toResponseDto(saved)).thenReturn(resultDto);

        SubmissionResponseDto result = submissionService.saveSubmission(dto);

        assertNotNull(result);
        verify(submissionRepository).save(any(Submission.class));
    }

    @Test
    @DisplayName("Abgabe soll gelöscht werden")
    void deleteSubmission_shouldRemove() {
        Submission entity = new Submission();
        when(submissionRepository.findById(1L)).thenReturn(Optional.of(entity));

        submissionService.deleteSubmissionById(1L);

        verify(submissionRepository).delete(entity);
    }

    @Test
    @DisplayName("Status der Abgabe soll aktualisiert werden")
    void updateStatus_shouldWork() {
        Submission entity = spy(new Submission());
        SubmissionResponseDto dto = new SubmissionResponseDto(1L, null, null, Submission.Status.SUBMITTED, 5.0);

        when(submissionRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(submissionRepository.save(entity)).thenReturn(entity);
        when(submissionMapper.toResponseDto(entity)).thenReturn(dto);

        SubmissionResponseDto result = submissionService.updateSubmissionStatus(1L, Submission.Status.SUBMITTED);

        assertNotNull(result);
        verify(entity).setStatus(Submission.Status.SUBMITTED);
    }

    @Test
    @DisplayName("Note der Abgabe soll aktualisiert werden")
    void updateGrade_shouldWork() {
        Submission entity = spy(new Submission());
        SubmissionResponseDto dto = new SubmissionResponseDto(1L, null, null, Submission.Status.NOT_SUBMITTED, 5.0);

        when(submissionRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(submissionRepository.save(entity)).thenReturn(entity);
        when(submissionMapper.toResponseDto(entity)).thenReturn(dto);

        SubmissionResponseDto result = submissionService.updateSubmissionGrade(1L, (Double) 2.0);

        assertNotNull(result);
        verify(entity).setGrade((Double) 2.0);
    }

    @Test
    @DisplayName("Alle Abgaben für ein Assignment sollen gefunden werden")
    void getByAssignmentId_shouldReturnList() {
        Submission s = new Submission();
        SubmissionResponseDto dto = new SubmissionResponseDto(1L, null, null, Submission.Status.NOT_SUBMITTED, 5.0);

        when(submissionRepository.findByAssignmentAssignmentId(1L)).thenReturn(List.of(s));
        when(submissionMapper.toResponseDto(List.of(s))).thenReturn(List.of(dto));

        var result = submissionService.getSubmissionsByAssignmentId(1L);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Alle Abgaben eines Users sollen gefunden werden")
    void getByUserId_shouldReturnList() {
        Submission s = new Submission();
        SubmissionResponseDto dto = new SubmissionResponseDto(1L, null, null, Submission.Status.NOT_SUBMITTED, 5.0);

        when(submissionRepository.findByStudentUserId(1L)).thenReturn(List.of(s));
        when(submissionMapper.toResponseDto(List.of(s))).thenReturn(List.of(dto));

        var result = submissionService.getSubmissionsByUserId(1L);
        assertEquals(1, result.size());
    }
}
