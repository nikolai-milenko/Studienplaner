package com.training.studienplaner.user;

import com.training.studienplaner.course.Course;
import com.training.studienplaner.course.CourseMapper;
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
    private final UserMapper userMapper;
    private final CourseMapper courseMapper;


    @PostMapping("/")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        User user = userMapper.toEntity(userRequestDto);
        User savedUser = userService.createUser(user);
        UserResponseDto responseDto = userMapper.toResponseDto(savedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<User> usersList = userService.getAll();
        List<UserResponseDto> dtoList = userMapper.toResponseDto(usersList);
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        User user = userService.getById(id);
        UserResponseDto responseDto = userMapper.toResponseDto(user);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/students")
    public ResponseEntity<List<UserResponseDto>> getStudents() {
        List<User> students = userService.findUsersByRole(User.Role.STUDENT);
        List<UserResponseDto> response = userMapper.toResponseDto(students);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        UserResponseDto responseDto = userMapper.toResponseDto(user);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<List<CourseResponseDto>> getUserCourses(@PathVariable Long id) {
        List<Course> coursesList = userService.getAllCoursesForUser(id);
        return ResponseEntity.ok(courseMapper.toResponseDto(coursesList));
    }

    @PostMapping("/{userId}/courses/{courseId}")
    public ResponseEntity<Void> createUserCourse(@PathVariable Long userId, @PathVariable Long courseId) {
        userService.enrollUserToCourse(userId, courseId);
        return ResponseEntity.ok().build();
    }

}
