package com.training.studienplaner.course;

import com.training.studienplaner.assignment.AssignmentResponseDto;
import com.training.studienplaner.user.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    @PreAuthorize("@authz.canAccessAny(principal, {'TEACHER', 'STUDENT'})")
    public ResponseEntity<List<CourseResponseDto>> getAllCourses() {
        List<CourseResponseDto> response = courseService.getAllCourses();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@authz.canAccessAny(principal, {'TEACHER', 'STUDENT'})")
    public ResponseEntity<CourseResponseDto> getCourseById(@PathVariable Long id) {
        CourseResponseDto response = courseService.getCourseById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("@authz.canAccessAny(principal, 'TEACHER')")
    public ResponseEntity<CourseResponseDto> createCourse(@RequestBody CourseRequestDto course) {
        CourseResponseDto response = courseService.createCourse(course);
        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authz.canAccessAny(principal)")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourseById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/assignments")
    @PreAuthorize("@authz.canAccessAny(principal, {'TEACHER', 'STUDENT'})")
    public ResponseEntity<List<AssignmentResponseDto>> getAssignmentsByCourseId(@PathVariable Long id) {
        List<AssignmentResponseDto> response = courseService.getAssignmentsByCourseId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/students")
    @PreAuthorize("@authz.canAccessAny(principal, {'TEACHER', 'STUDENT'})")
    public ResponseEntity<List<UserResponseDto>> getStudentsByCourseId(@PathVariable Long id) {
        List<UserResponseDto> response = courseService.getStudentsByCourseId(id);
        return ResponseEntity.ok(response);
    }
}
