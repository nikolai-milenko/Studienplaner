package com.training.studienplaner.assignment;

import com.training.studienplaner.course.CourseRepository;
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
    private CourseRepository courseRepository;

    @Mock
    private AssignmentMapper assignmentMapper;

    @Mock
    private SubmissionService submissionService;

    @InjectMocks
    private AssignmentService assignmentService;

    @Test
    @DisplayName("Soll alle Assignments zurückgeben")
    void getAllAssignments_shouldReturnAssignments() {
        Assignment assignment = mock(Assignment.class);
        AssignmentResponseDto responseDto = mock(AssignmentResponseDto.class);

        when(assignmentRepository.findAll()).thenReturn(List.of(assignment));
        when(assignmentMapper.toResponseDto(List.of(assignment))).thenReturn(List.of(responseDto));

        List<AssignmentResponseDto> result = assignmentService.getAllAssignments();

        assertEquals(1, result.size());
        verify(assignmentRepository).findAll();
        verify(assignmentMapper).toResponseDto(List.of(assignment));
    }

    @Test
    @DisplayName("Soll Assignment anhand der ID zurückgeben")
    void getAssignmentById_shouldReturnAssignment_whenExists() {
        long id = 1L;
        Assignment assignment = mock(Assignment.class);
        AssignmentResponseDto responseDto = mock(AssignmentResponseDto.class);

        when(assignmentRepository.findById(id)).thenReturn(Optional.of(assignment));
        when(assignmentMapper.toResponseDto(assignment)).thenReturn(responseDto);

        AssignmentResponseDto result = assignmentService.getAssignmentById(id);

        assertEquals(responseDto, result);
        verify(assignmentRepository).findById(id);
        verify(assignmentMapper).toResponseDto(assignment);
    }

    @Test
    @DisplayName("Soll Exception werfen, wenn Assignment nicht gefunden")
    void getAssignmentById_shouldThrowException_whenNotFound() {
        long id = 1L;
        when(assignmentRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            assignmentService.getAssignmentById(id);
        });

        assertEquals("Assignment not found", exception.getMessage());
        verify(assignmentRepository).findById(id);
    }

    @Test
    @DisplayName("Soll Assignment löschen")
    void deleteAssignmentById_shouldDeleteAssignment() {
        long id = 1L;
        Assignment assignment = mock(Assignment.class);

        when(assignmentRepository.findById(id)).thenReturn(Optional.of(assignment));

        assignmentService.deleteAssignmentById(id);

        verify(assignmentRepository).findById(id);
        verify(assignmentRepository).delete(assignment);
    }

    @Test
    @DisplayName("Soll Exception werfen, wenn Assignment zum Löschen nicht gefunden")
    void deleteAssignmentById_shouldThrowException_whenNotFound() {
        long id = 1L;
        when(assignmentRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            assignmentService.deleteAssignmentById(id);
        });

        assertEquals("Assignment not found", exception.getMessage());
        verify(assignmentRepository).findById(id);
    }

    @Test
    @DisplayName("Soll Assignment erstellen und Submissions generieren")
    void createAssignment_shouldCreateAssignmentAndGenerateSubmissions() {
        AssignmentRequestDto requestDto = mock(AssignmentRequestDto.class);
        Assignment assignment = mock(Assignment.class);
        Assignment savedAssignment = mock(Assignment.class);
        AssignmentResponseDto responseDto = mock(AssignmentResponseDto.class);

        when(assignmentMapper.toEntity(eq(requestDto), any())).thenReturn(assignment);
        when(assignmentRepository.save(assignment)).thenReturn(savedAssignment);
        when(assignmentMapper.toResponseDto(savedAssignment)).thenReturn(responseDto);

        AssignmentResponseDto result = assignmentService.createAssignment(requestDto);

        assertEquals(responseDto, result);
        verify(assignmentMapper).toEntity(eq(requestDto), any());
        verify(assignmentRepository).save(assignment);
        verify(submissionService).generateSubmissionsForAssignment(savedAssignment);
        verify(assignmentMapper).toResponseDto(savedAssignment);
    }
}
