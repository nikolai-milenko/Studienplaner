package com.training.studienplaner.course;

import com.training.studienplaner.assignment.AssignmentResponseDto;
import com.training.studienplaner.user.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<List<CourseResponseDto>> getAllCourses() {
        List<CourseResponseDto> response = courseService.getAllCourses();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDto> getCourseById(@PathVariable Long id) {
        CourseResponseDto response = courseService.getCourseById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CourseResponseDto> createCourse(@RequestBody CourseRequestDto course) {
        CourseResponseDto response = courseService.createCourse(course);
        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourseById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/assignments")
    public ResponseEntity<List<AssignmentResponseDto>> getAssignmentsByCourseId(@PathVariable Long id) {
        List<AssignmentResponseDto> response = courseService.getAssignmentsByCourseId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<List<UserResponseDto>> getStudentsByCourseId(@PathVariable Long id) {
        List<UserResponseDto> response = courseService.getStudentsByCourseId(id);
        return ResponseEntity.ok(response);
    }
}
