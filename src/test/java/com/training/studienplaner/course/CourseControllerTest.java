package com.training.studienplaner.course;

import com.training.studienplaner.assignment.AssignmentResponseDto;
import com.training.studienplaner.user.UserResponseDto;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(CourseController.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Test
    @DisplayName("Soll alle Kurse zurückgeben")
    void getAllCourses_shouldReturnCourses() throws Exception {
        CourseResponseDto responseDto = new CourseResponseDto(
                1L, "Test Course", "Test Description", null, (short) 5, null, null
        );

        when(courseService.getAllCourses()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseId").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test Course"));

        verify(courseService).getAllCourses();
    }

    @Test
    @DisplayName("Soll Kurs anhand der ID zurückgeben")
    void getCourseById_shouldReturnCourse() throws Exception {
        CourseResponseDto responseDto = new CourseResponseDto(
                1L, "Test Course", "Test Description", null, (short) 5, null, null
        );

        when(courseService.getCourseById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId").value(1L))
                .andExpect(jsonPath("$.title").value("Test Course"));

        verify(courseService).getCourseById(1L);
    }

    @Test
    @DisplayName("Soll 404 zurückgeben, wenn Kurs nicht existiert")
    void getCourseById_shouldReturnNotFound() throws Exception {
        when(courseService.getCourseById(1L))
                .thenThrow(new EntityNotFoundException("Course not found"));

        mockMvc.perform(get("/courses/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Course not found"));

        verify(courseService).getCourseById(1L);
    }

    @Test
    @DisplayName("Soll Kurs erstellen und 201 zurückgeben")
    void createCourse_shouldCreateCourse() throws Exception {
        CourseRequestDto requestDto = new CourseRequestDto(
                "Test Course", "Test Description", null, (short) 5
        );

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
    @DisplayName("Soll Kurs löschen und 204 zurückgeben")
    void deleteCourse_shouldDeleteCourse() throws Exception {
        mockMvc.perform(delete("/courses/1"))
                .andExpect(status().isNoContent());

        verify(courseService).deleteCourseById(1L);
    }

    @Test
    @DisplayName("Soll Assignments für Kurs zurückgeben")
    void getAssignmentsByCourseId_shouldReturnAssignments() throws Exception {
        AssignmentResponseDto responseDto = new AssignmentResponseDto(
                1L, "Assignment Title", "Assignment Description", null, null, null
        );

        when(courseService.getAssignmentsByCourseId(1L)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/courses/1/assignments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].assignmentId").value(1L))
                .andExpect(jsonPath("$[0].title").value("Assignment Title"));

        verify(courseService).getAssignmentsByCourseId(1L);
    }

    @Test
    @DisplayName("Soll Studenten für Kurs zurückgeben")
    void getStudentsByCourseId_shouldReturnStudents() throws Exception {
        UserResponseDto responseDto = new UserResponseDto(
                1L, "John", "Doe", null, "john@example.com", null
        );

        when(courseService.getStudentsByCourseId(1L)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/courses/1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"));

        verify(courseService).getStudentsByCourseId(1L);
    }
}
