package com.training.studienplaner.user;

import com.training.studienplaner.course.CourseResponseDto;
import com.training.studienplaner.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @PreAuthorize("@authz.canAccessAny(authentication)")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> dtoList = userService.getAll();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@authz.canAccessAny(principal, 'TEACHER')")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        UserResponseDto responseDto = userService.getById(id);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authz.canAccessAny(principal)")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/students")
    @PreAuthorize("@authz.canAccessAny(principal, 'TEACHER')")
    public ResponseEntity<List<UserResponseDto>> getStudents() {
        List<UserResponseDto> response = userService.findUsersByRole(User.Role.STUDENT);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("@authz.canAccessAny(principal, 'TEACHER')")
    public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email) {
        UserResponseDto responseDto = userService.findByEmail(email);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}/courses")
    @PreAuthorize("#userDetails != null and (#id == #userDetails.user.getUserId() or hasAnyRole('TEACHER', 'ADMIN'))")
    public ResponseEntity<List<CourseResponseDto>> getUserCourses(@PathVariable Long id,
                                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<CourseResponseDto> courses = userService.getAllCoursesForUser(id);
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/{userId}/courses/{courseId}")
    @PreAuthorize("#userDetails != null and (#userId == #userDetails.user.getUserId() or hasAnyRole('TEACHER', 'ADMIN'))")
    public ResponseEntity<Void> createUserCourse(
            @PathVariable Long userId,
            @PathVariable Long courseId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        userService.enrollUserToCourse(userId, courseId);
        return ResponseEntity.ok().build();
    }
}
