package com.training.studienplaner.submission;

import com.training.studienplaner.assignment.Assignment;
import com.training.studienplaner.course.Course;
import com.training.studienplaner.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubmissionServiceTest {
    @Mock
    private SubmissionRepository submissionRepository;

    @InjectMocks
    private SubmissionService submissionService;

    @Captor
    private ArgumentCaptor<Submission> submissionCaptor;

    @Test
    @DisplayName("Soll Submissions für alle Studenten eines Assignments generieren und speichern")
    void generateSubmissionsForAssignment_shouldCreateSubmissionsForAllStudents() {
        // given
        User student1 = mock(User.class);
        when(student1.getRole()).thenReturn(User.Role.STUDENT);

        User student2 = mock(User.class);
        when(student2.getRole()).thenReturn(User.Role.STUDENT);

        User teacher = mock(User.class);
        when(teacher.getRole()).thenReturn(User.Role.TEACHER);

        Course course = mock(Course.class);
        when(course.getStudents()).thenReturn(List.of(student1, student2, teacher));

        Assignment assignment = mock(Assignment.class);
        when(assignment.getCourse()).thenReturn(course);

        ArgumentCaptor<List<Submission>> captor = ArgumentCaptor.forClass(List.class);

        // when
        submissionService.generateSubmissionsForAssignment(assignment);

        // then
        verify(submissionRepository).saveAll(captor.capture());

        List<Submission> capturedSubmissions = captor.getValue();
        assertEquals(2, capturedSubmissions.size());

        for (Submission submission : capturedSubmissions) {
            assertEquals(assignment, submission.getAssignment());
            assertEquals(Submission.Status.NOT_SUBMITTED, submission.getStatus());
            assertTrue(submission.getStudent() == student1 || submission.getStudent() == student2);
        }
    }

    @Test
    @DisplayName("Soll saveAll mit leerer Liste aufrufen, wenn im Kurs keine Studenten vorhanden sind")
    void generateSubmissionsForAssignment_shouldCallSaveAllWithEmptyList_whenNoStudentsExist() {
        // given
        Course course = mock(Course.class);
        when(course.getStudents()).thenReturn(Collections.emptyList());

        Assignment assignment = mock(Assignment.class);
        when(assignment.getCourse()).thenReturn(course);

        ArgumentCaptor<List<Submission>> captor = ArgumentCaptor.forClass(List.class);

        // when
        submissionService.generateSubmissionsForAssignment(assignment);

        // then
        verify(submissionRepository).saveAll(captor.capture());
        List<Submission> capturedSubmissions = captor.getValue();
        assertTrue(capturedSubmissions.isEmpty());
    }

    @Test
    @DisplayName("Soll eine NullPointerException werfen, wenn getStudents null zurückgibt")
    void generateSubmissionsForAssignment_shouldThrowException_whenStudentsListIsNull() {
        // given
        Course course = mock(Course.class);
        when(course.getStudents()).thenReturn(null);

        Assignment assignment = mock(Assignment.class);
        when(assignment.getCourse()).thenReturn(course);

        // when + then
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            submissionService.generateSubmissionsForAssignment(assignment);
        });

        verify(submissionRepository, never()).saveAll(any());
    }

    @Test
    @DisplayName("Soll eine Submission erstellen und im Repository speichern")
    void saveSubmission_shouldCreateSubmission() {
        // given
        Submission submission = mock(Submission.class);
        when(submissionRepository.save(submission)).thenReturn(submission);

        //when
        Submission result = submissionService.saveSubmission(submission);

        //then
        verify(submissionRepository).save(submission);
        assertNotNull(result);
        assertEquals(submission, result);
    }

    @Test
    @DisplayName("Soll alle Submissions zurückgeben, wenn Submissions existieren")
    void getAllSubmissions_shouldReturnAllSubmissions_whenSubmissionsExist() {
        // given
        Submission submission1 = mock(Submission.class);
        Submission submission2 = mock(Submission.class);

        when(submissionRepository.findAll()).thenReturn(List.of(submission1, submission2));

        //when
        List<Submission> allSubmissions = submissionService.getAllSubmissions();
        assertEquals(2, allSubmissions.size());
        assertEquals(submission1, allSubmissions.get(0));
        assertEquals(submission2, allSubmissions.get(1));
    }

    @Test
    @DisplayName("Soll eine leere Liste zurückgeben, wenn keine Submissions existieren")
    void getAllSubmissions_shouldReturnEmptyList_whenNoSubmissionsExist() {
        // given
        when(submissionRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        List<Submission> submissions = submissionService.getAllSubmissions();

        // then
        assertTrue(submissions.isEmpty());
        verify(submissionRepository).findAll();
    }

    @Test
    @DisplayName("Soll Submission anhand der ID zurückgeben, wenn Submission existiert")
    void getSubmissionById_shouldReturnSubmission_whenSubmissionExist() {
        // given
        Submission submission1 = mock(Submission.class);
        long id = 1L;
        when(submissionRepository.findById(id)).thenReturn(Optional.of(submission1));

        //when
        Submission result = submissionService.getSubmissionById(id);

        //then
        verify(submissionRepository).findById(id);
        assertNotNull(result);
        assertEquals(submission1, result);
    }

    @Test
    @DisplayName("Soll eine Ausnahme werfen, wenn Submission mit angegebener ID nicht gefunden wird")
    void getSubmissionById_shouldThrowException_whenSubmissionDoesNotExist() {
        // given
        long id = 1L;
        when(submissionRepository.findById(id)).thenReturn(Optional.empty());

        // when + then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            submissionService.getSubmissionById(id);
        });

        assertEquals("Submission not found", exception.getMessage());
        verify(submissionRepository).findById(id);
    }

    @Test
    @DisplayName("Soll Submission löschen, wenn Submission existiert")
    void deleteSubmissionById_shouldDeleteSubmission_whenSubmissionExist() {
        // given
        Submission submission1 = mock(Submission.class);
        long id = 1L;
        when(submissionRepository.findById(id)).thenReturn(Optional.of(submission1));

        // when
        submissionService.deleteSubmissionById(id);

        // then
        verify(submissionRepository).findById(id);
        verify(submissionRepository).delete(submission1);
    }

    @Test
    @DisplayName("Soll eine Ausnahme werfen, wenn Submission mit angegebener ID nicht gefunden wird")
    void deleteSubmissionById_shouldThrowException_whenSubmissionDoesNotExist() {
        // given
        long id = 1L;
        when(submissionRepository.findById(id)).thenReturn(Optional.empty());

        // when + then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            submissionService.deleteSubmissionById(id);
        });

        assertEquals("Submission not found", exception.getMessage());
        verify(submissionRepository).findById(id);
        verify(submissionRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Soll alle Submissions anhand der Assignment-ID zurückgeben")
    void getSubmissionsByAssignmentId_shouldReturnSubmissions_whenTheyExist() {
        // given
        long assignmentId = 1L;
        Submission submission1 = mock(Submission.class);
        Submission submission2 = mock(Submission.class);
        when(submissionRepository.findByAssignmentAssignmentId(assignmentId))
                .thenReturn(List.of(submission1, submission2));

        // when
        List<Submission> result = submissionService.getSubmissionsByAssignmentId(assignmentId);

        // then
        assertEquals(2, result.size());
        assertEquals(submission1, result.get(0));
        assertEquals(submission2, result.get(1));
        verify(submissionRepository).findByAssignmentAssignmentId(assignmentId);
    }

    @Test
    @DisplayName("Soll alle Submissions anhand der Benutzer-ID zurückgeben")
    void getSubmissionsByUserId_shouldReturnSubmissions_whenTheyExist() {
        // given
        long userId = 1L;
        Submission submission1 = mock(Submission.class);
        Submission submission2 = mock(Submission.class);
        when(submissionRepository.findByStudentUserId(userId))
                .thenReturn(List.of(submission1, submission2));

        // when
        List<Submission> result = submissionService.getSubmissionsByUserId(userId);

        // then
        assertEquals(2, result.size());
        assertEquals(submission1, result.get(0));
        assertEquals(submission2, result.get(1));
        verify(submissionRepository).findByStudentUserId(userId);
    }

    @Test
    @DisplayName("Soll eine leere Liste zurückgeben, wenn keine Submissions für die Assignment-ID existieren")
    void getSubmissionsByAssignmentId_shouldReturnEmptyList_whenNoSubmissionsExist() {
        // given
        long assignmentId = 1L;
        when(submissionRepository.findByAssignmentAssignmentId(assignmentId))
                .thenReturn(Collections.emptyList());

        // when
        List<Submission> result = submissionService.getSubmissionsByAssignmentId(assignmentId);

        // then
        assertTrue(result.isEmpty());
        verify(submissionRepository).findByAssignmentAssignmentId(assignmentId);
    }

    @Test
    @DisplayName("Soll eine leere Liste zurückgeben, wenn keine Submissions für die Benutzer-ID existieren")
    void getSubmissionsByUserId_shouldReturnEmptyList_whenNoSubmissionsExist() {
        // given
        long userId = 1L;
        when(submissionRepository.findByStudentUserId(userId))
                .thenReturn(Collections.emptyList());

        // when
        List<Submission> result = submissionService.getSubmissionsByUserId(userId);

        // then
        assertTrue(result.isEmpty());
        verify(submissionRepository).findByStudentUserId(userId);
    }

    @Test
    @DisplayName("Soll den Status einer Submission aktualisieren, wenn Submission existiert")
    void updateSubmissionStatus_shouldUpdateStatus_whenSubmissionExists() {
        // given
        long id = 1L;
        Submission.Status newStatus = Submission.Status.SUBMITTED;

        Submission submission = mock(Submission.class);
        when(submissionRepository.findById(id)).thenReturn(Optional.of(submission));
        when(submissionRepository.save(submission)).thenReturn(submission);

        // when
        Submission result = submissionService.updateSubmissionStatus(id, newStatus);

        // then
        verify(submissionRepository).findById(id);
        verify(submission).setStatus(newStatus);
        verify(submissionRepository).save(submission);
        assertEquals(submission, result);
    }

    @Test
    @DisplayName("Soll eine Ausnahme werfen, wenn Submission nicht gefunden wird")
    void updateSubmissionStatus_shouldThrowException_whenSubmissionDoesNotExist() {
        // given
        long id = 1L;
        Submission.Status newStatus = Submission.Status.SUBMITTED;
        when(submissionRepository.findById(id)).thenReturn(Optional.empty());

        // when + then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            submissionService.updateSubmissionStatus(id, newStatus);
        });

        assertEquals("Submission not found", exception.getMessage());
        verify(submissionRepository).findById(id);
        verify(submissionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Soll die Bewertung einer Submission aktualisieren, wenn Submission existiert")
    void updateSubmissionGrade_shouldUpdateGrade_whenSubmissionExists() {
        // given
        long id = 1L;
        short grade = 1;

        Submission submission = mock(Submission.class);
        when(submissionRepository.findById(id)).thenReturn(Optional.of(submission));
        when(submissionRepository.save(submission)).thenReturn(submission);

        // when
        Submission result = submissionService.updateSubmissionGrade(id, grade);

        // then
        verify(submissionRepository).findById(id);
        verify(submission).setGrade(grade);
        verify(submissionRepository).save(submission);
        assertEquals(submission, result);
    }

    @Test
    @DisplayName("Soll eine Ausnahme werfen, wenn Submission nicht gefunden wird")
    void updateSubmissionGrade_shouldThrowException_whenSubmissionDoesNotExist() {
        // given
        long id = 1L;
        short grade = 1;
        when(submissionRepository.findById(id)).thenReturn(Optional.empty());

        // when + then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            submissionService.updateSubmissionGrade(id, grade);
        });

        assertEquals("Submission not found", exception.getMessage());
        verify(submissionRepository).findById(id);
        verify(submissionRepository, never()).save(any());
    }
}