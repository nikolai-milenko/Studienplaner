package com.training.studienplaner.course;

import com.training.studienplaner.assignment.Assignment;
import com.training.studienplaner.assignment.AssignmentMapper;
import com.training.studienplaner.assignment.AssignmentResponseDto;
import com.training.studienplaner.user.User;
import com.training.studienplaner.user.UserMapper;
import com.training.studienplaner.user.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {
    private final CourseMapper courseMapper;
    private final CourseService courseService;
    private final AssignmentMapper assignmentMapper;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<CourseResponseDto>> getAllCourses(){
        return ResponseEntity.ok(courseMapper.toResponseDto(courseService.getAllCourses()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDto> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseMapper.toResponseDto(courseService.getCourseById(id)));
    }

    @PostMapping
    public ResponseEntity<CourseResponseDto> createCourse(@RequestBody CourseRequestDto course) {
        Course createdCourse = courseService.createCourse(courseMapper.toEntity(course));
        return ResponseEntity.status(201).body(courseMapper.toResponseDto(createdCourse));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourseById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/assignments")
    public ResponseEntity<List<AssignmentResponseDto>> getAssignmentsByCourseId(@PathVariable Long id) {
        List<Assignment> assignments = courseService.getAssignmentsByCourseId(id);
        List<AssignmentResponseDto> response = assignmentMapper.toResponseDto(assignments);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<List<UserResponseDto>> getStudentsByCourseId(@PathVariable Long id) {
        List<User> students = courseService.getStudentsByCourseId(id);
        List<UserResponseDto> response = userMapper.toResponseDto(students);
        return ResponseEntity.ok(response);
    }


}
