package com.training.studienplaner.user;

import com.training.studienplaner.course.CourseResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto responseDto = userService.createUser(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> dtoList = userService.getAll();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        UserResponseDto responseDto = userService.getById(id);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/students")
    public ResponseEntity<List<UserResponseDto>> getStudents() {
        List<UserResponseDto> response = userService.findUsersByRole(User.Role.STUDENT);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email) {
        UserResponseDto responseDto = userService.findByEmail(email);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<List<CourseResponseDto>> getUserCourses(@PathVariable Long id) {
        List<CourseResponseDto> courses = userService.getAllCoursesForUser(id);
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/{userId}/courses/{courseId}")
    public ResponseEntity<Void> createUserCourse(@PathVariable Long userId, @PathVariable Long courseId) {
        userService.enrollUserToCourse(userId, courseId);
        return ResponseEntity.ok().build();
    }
}
