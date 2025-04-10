package com.training.studienplaner.course;

import com.training.studienplaner.assignment.Assignment;
import com.training.studienplaner.assignment.AssignmentMapper;
import com.training.studienplaner.assignment.AssignmentResponseDto;
import com.training.studienplaner.user.User;
import com.training.studienplaner.user.UserMapper;
import com.training.studienplaner.user.UserResponseDto;
import com.training.studienplaner.user.UserShortDto;
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
@WebMvcTest(CourseController.class)
public class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private CourseMapper courseMapper;

    @MockBean
    private AssignmentMapper assignmentMapper;

    @MockBean
    private UserMapper userMapper;

    @Test
    @DisplayName("Soll alle Kurse zurückgeben, wenn Kurse existieren")
    void getAllCourses_shouldReturnListOfCourses_whenCoursesExist() throws Exception {
        // given
        long id = 1L;
        Course course = new Course();
        course.setCourseId(id);
        course.setTitle("Test Course");
        course.setDescription("Test Description");
        course.setEcts((short) 5);

        CourseResponseDto courseResponseDto = new CourseResponseDto(
                id,
                "Test Course",
                "Test Description",
                null,
                (short) 5,
                null,
                null
        );

        when(courseService.getAllCourses()).thenReturn(List.of(course));
        when(courseMapper.toResponseDto(List.of(course))).thenReturn(List.of(courseResponseDto));

        // when + then
        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseId").value(id))
                .andExpect(jsonPath("$[0].title").value("Test Course"))
                .andExpect(jsonPath("$[0].description").value("Test Description"));

        verify(courseService).getAllCourses();
        verify(courseMapper).toResponseDto(List.of(course));
    }

    @Test
    @DisplayName("Soll 404 zurückgeben, wenn keine Kurse vorhanden sind")
    void getAllCourses_shouldReturnNotFound_whenNoCoursesExist() throws Exception {
        // given
        when(courseService.getAllCourses()).thenThrow(new EntityNotFoundException("No courses found"));

        // when + then
        mockMvc.perform(get("/courses"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No courses found"));

        verify(courseService).getAllCourses();
    }

    @Test
    @DisplayName("Soll Kurs anhand der ID zurückgeben, wenn Kurs existiert")
    void getCourseById_shouldReturnCourse_whenCourseExists() throws Exception {
        // given
        long id = 1L;
        Course course = new Course();
        course.setCourseId(id);
        course.setTitle("Test Course");
        course.setDescription("Test Description");
        course.setEcts((short) 5);

        CourseResponseDto responseDto = new CourseResponseDto(
                id,
                "Test Course",
                "Test Description",
                null,
                (short) 5,
                null,
                null
        );

        when(courseService.getCourseById(id)).thenReturn(course);
        when(courseMapper.toResponseDto(course)).thenReturn(responseDto);

        // when + then
        mockMvc.perform(get("/courses/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId").value(id))
                .andExpect(jsonPath("$.title").value("Test Course"))
                .andExpect(jsonPath("$.description").value("Test Description"));

        verify(courseService).getCourseById(id);
        verify(courseMapper).toResponseDto(course);
    }

    @Test
    @DisplayName("Soll 404 zurückgeben, wenn Kurs mit angegebener ID nicht gefunden wird")
    void getCourseById_shouldReturnNotFound_whenCourseDoesNotExist() throws Exception {
        // given
        long id = 1L;
        when(courseService.getCourseById(id)).thenThrow(new EntityNotFoundException("Course not found"));

        // when + then
        mockMvc.perform(get("/courses/" + id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Course not found"));

        verify(courseService).getCourseById(id);
    }

    @Test
    @DisplayName("Soll Kurs erstellen und 201 zurückgeben")
    void createCourse_shouldCreateCourse_andReturnCreated() throws Exception {
        // given
        CourseRequestDto requestDto = new CourseRequestDto(
                "Test Course",
                "Test Description",
                null,
                (short) 5
        );

        Course course = new Course();
        course.setCourseId(1L);
        course.setTitle("Test Course");
        course.setDescription("Test Description");
        course.setEcts((short) 5);

        CourseResponseDto responseDto = new CourseResponseDto(
                1L,
                "Test Course",
                "Test Description",
                null,
                (short) 5,
                null,
                null
        );

        when(courseMapper.toEntity(requestDto)).thenReturn(course);
        when(courseService.createCourse(course)).thenReturn(course);
        when(courseMapper.toResponseDto(course)).thenReturn(responseDto);

        // when + then
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
                .andExpect(jsonPath("$.title").value("Test Course"))
                .andExpect(jsonPath("$.description").value("Test Description"));

        verify(courseMapper).toEntity(requestDto);
        verify(courseService).createCourse(course);
        verify(courseMapper).toResponseDto(course);
    }

    @Test
    @DisplayName("Soll 404 zurückgeben, wenn beim Erstellen des Kurses ein Fehler auftritt")
    void createCourse_shouldReturnNotFound_whenServiceFails() throws Exception {
        // given
        CourseRequestDto requestDto = new CourseRequestDto(
                "Test Course",
                "Test Description",
                null,
                (short) 5
        );

        Course course = new Course();
        course.setTitle("Test Course");
        course.setDescription("Test Description");
        course.setEcts((short) 5);

        when(courseMapper.toEntity(requestDto)).thenReturn(course);
        when(courseService.createCourse(course)).thenThrow(new EntityNotFoundException("Tutor not found"));

        // when + then
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
                .andExpect(status().isNotFound())
                .andExpect(content().string("Tutor not found"));

        verify(courseMapper).toEntity(requestDto);
        verify(courseService).createCourse(course);
    }

    @Test
    @DisplayName("Soll Kurs löschen und 204 zurückgeben")
    void deleteCourse_shouldDeleteCourse_andReturnNoContent() throws Exception {
        // given
        long id = 1L;

        // when + then
        mockMvc.perform(delete("/courses/" + id))
                .andExpect(status().isNoContent());

        verify(courseService).deleteCourseById(id);
    }

    @Test
    @DisplayName("Soll 404 zurückgeben, wenn Kurs zum Löschen nicht gefunden wird")
    void deleteCourse_shouldReturnNotFound_whenCourseDoesNotExist() throws Exception {
        // given
        long id = 1L;
        doThrow(new EntityNotFoundException("Course not found"))
                .when(courseService).deleteCourseById(id);

        // when + then
        mockMvc.perform(delete("/courses/" + id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Course not found"));

        verify(courseService).deleteCourseById(id);
    }

    @Test
    @DisplayName("Soll alle Assignments für Kurs zurückgeben, wenn vorhanden")
    void getAssignmentsByCourseId_shouldReturnAssignments_whenAssignmentsExist() throws Exception {
        // given
        long courseId = 1L;

        Assignment assignment = mock(Assignment.class);
        when(assignment.getAssignmentId()).thenReturn(1L);
        when(assignment.getTitle()).thenReturn("Test Assignment");

        AssignmentResponseDto responseDto = new AssignmentResponseDto(
                1L,
                "Test Assignment",
                "Test Description",
                Assignment.AssignmentType.HOMEWORK,
                LocalDateTime.now(),
                null
        );

        when(courseService.getAssignmentsByCourseId(courseId)).thenReturn(List.of(assignment));
        when(assignmentMapper.toResponseDto(List.of(assignment))).thenReturn(List.of(responseDto));

        // when + then
        mockMvc.perform(get("/courses/" + courseId + "/assignments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].assignmentId").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test Assignment"))
                .andExpect(jsonPath("$[0].description").value("Test Description"));

        verify(courseService).getAssignmentsByCourseId(courseId);
        verify(assignmentMapper).toResponseDto(List.of(assignment));
    }

    @Test
    @DisplayName("Soll 404 zurückgeben, wenn keine Assignments für Kurs vorhanden sind")
    void getAssignmentsByCourseId_shouldReturnNotFound_whenNoAssignmentsExist() throws Exception {
        // given
        long courseId = 1L;

        when(courseService.getAssignmentsByCourseId(courseId))
                .thenThrow(new EntityNotFoundException("No assignments found"));

        // when + then
        mockMvc.perform(get("/courses/" + courseId + "/assignments"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No assignments found"));

        verify(courseService).getAssignmentsByCourseId(courseId);
    }

    @Test
    @DisplayName("Soll alle Studenten für Kurs zurückgeben, wenn vorhanden")
    void getStudentsByCourseId_shouldReturnStudents_whenStudentsExist() throws Exception {
        // given
        long courseId = 1L;

        User user = mock(User.class);
        when(user.getUserId()).thenReturn(1L);
        when(user.getName()).thenReturn("John Doe");

        UserResponseDto responseDto = new UserResponseDto(
                1L,
                "John",
                "Doe",
                User.Role.STUDENT,
                "john@example.com",
                null
        );

        when(courseService.getStudentsByCourseId(courseId)).thenReturn(List.of(user));
        when(userMapper.toResponseDto(List.of(user))).thenReturn(List.of(responseDto));

        // when + then
        mockMvc.perform(get("/courses/" + courseId + "/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[0].role").value("STUDENT"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"));

        verify(courseService).getStudentsByCourseId(courseId);
        verify(userMapper).toResponseDto(List.of(user));
    }


}
