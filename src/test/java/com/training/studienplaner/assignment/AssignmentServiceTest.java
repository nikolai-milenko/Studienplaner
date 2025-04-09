package com.training.studienplaner.assignment;

import com.training.studienplaner.submission.SubmissionService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssignmentServiceTest {
    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private SubmissionService submissionService;

    @InjectMocks
    private AssignmentService assignmentService;

    @Test
    @DisplayName("Soll alle Assignments zurückgeben, wenn Assignments existieren")
    void getAllAssignments_shouldReturnAssignments_whenAssignmentsExist() {
        // given
        Assignment assignment1 = mock(Assignment.class);
        Assignment assignment2 = mock(Assignment.class);
        when(assignmentRepository.findAll()).thenReturn(List.of(assignment1, assignment2));

        // when
        List<Assignment> result = assignmentService.getAllAssignments();

        // then
        assertEquals(2, result.size());
        assertEquals(assignment1, result.get(0));
        assertEquals(assignment2, result.get(1));
        verify(assignmentRepository).findAll();
    }

    @Test
    @DisplayName("Soll eine leere Liste zurückgeben, wenn keine Assignments existieren")
    void getAllAssignments_shouldReturnEmptyList_whenNoAssignmentsExist() {
        // given
        when(assignmentRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        List<Assignment> result = assignmentService.getAllAssignments();

        // then
        assertTrue(result.isEmpty());
        verify(assignmentRepository).findAll();
    }

    @Test
    @DisplayName("Soll Assignment anhand der ID zurückgeben, wenn Assignment existiert")
    void getAssignmentById_shouldReturnAssignment_whenAssignmentExists() {
        // given
        long id = 1L;
        Assignment assignment = mock(Assignment.class);
        when(assignmentRepository.findById(id)).thenReturn(Optional.of(assignment));

        // when
        Assignment result = assignmentService.getAssignmentById(id);

        // then
        verify(assignmentRepository).findById(id);
        assertEquals(assignment, result);
    }

    @Test
    @DisplayName("Soll eine Ausnahme werfen, wenn Assignment mit angegebener ID nicht gefunden wird")
    void getAssignmentById_shouldThrowException_whenAssignmentDoesNotExist() {
        // given
        long id = 1L;
        when(assignmentRepository.findById(id)).thenReturn(Optional.empty());

        // when + then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            assignmentService.getAssignmentById(id);
        });

        assertEquals("Assignment not found", exception.getMessage());
        verify(assignmentRepository).findById(id);
    }

    @Test
    @DisplayName("Soll Assignment löschen, wenn Assignment existiert")
    void deleteAssignmentById_shouldDeleteAssignment_whenAssignmentExists() {
        // given
        long id = 1L;
        Assignment assignment = mock(Assignment.class);
        when(assignmentRepository.findById(id)).thenReturn(Optional.of(assignment));

        // when
        assignmentService.deleteAssignmentById(id);

        // then
        verify(assignmentRepository).findById(id);
        verify(assignmentRepository).delete(assignment);
    }

    @Test
    @DisplayName("Soll eine Ausnahme werfen, wenn Assignment mit angegebener ID nicht gefunden wird")
    void deleteAssignmentById_shouldThrowException_whenAssignmentDoesNotExist() {
        // given
        long id = 1L;
        when(assignmentRepository.findById(id)).thenReturn(Optional.empty());

        // when + then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            assignmentService.deleteAssignmentById(id);
        });

        assertEquals("Assignment not found", exception.getMessage());
        verify(assignmentRepository).findById(id);
    }

    @Test
    @DisplayName("Soll ein Assignment erstellen und Submissions generieren")
    void createAssignment_shouldCreateAssignmentAndGenerateSubmissions() {
        // given
        Assignment assignment = mock(Assignment.class);
        Assignment savedAssignment = mock(Assignment.class);
        when(assignmentRepository.save(assignment)).thenReturn(savedAssignment);

        // when
        Assignment result = assignmentService.createAssignment(assignment);

        // then
        verify(assignmentRepository).save(assignment);
        verify(submissionService).generateSubmissionsForAssignment(savedAssignment);
        assertEquals(savedAssignment, result);
    }
}
