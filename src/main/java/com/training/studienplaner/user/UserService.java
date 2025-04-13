package com.training.studienplaner.user;

import com.training.studienplaner.course.Course;
import com.training.studienplaner.course.CourseMapper;
import com.training.studienplaner.course.CourseResponseDto;
import com.training.studienplaner.course.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final UserMapper userMapper;
    private final CourseMapper courseMapper;

    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User user = userMapper.toEntity(userRequestDto);
        User saved = userRepository.save(user);
        return userMapper.toResponseDto(saved);
    }

    public List<UserResponseDto> getAll() {
        return userMapper.toResponseDto(userRepository.findAll());
    }

    public UserResponseDto getById(Long id) {
        return userMapper.toResponseDto(findUserById(id));
    }

    public void deleteById(Long id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }

    public UserResponseDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found by email"));
        return userMapper.toResponseDto(user);
    }

    public List<UserResponseDto> findUsersByRole(User.Role role) {
        List<User> users = userRepository.findAllByRole(role);
        if (users.isEmpty()) {
            throw new EntityNotFoundException("There are no users with role " + role);
        }
        return userMapper.toResponseDto(users);
    }

    public List<CourseResponseDto> getAllCoursesForUser(Long userId) {
        User user = findUserById(userId);
        return courseMapper.toResponseDto(user.getCoursesList());
    }

    public void enrollUserToCourse(Long userId, Long courseId) {
        User user = findUserById(userId);
        Course course = findCourseById(courseId);
        user.getCoursesList().add(course);
        userRepository.save(user);
    }

    // === internal helpers ===

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    private Course findCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
    }
}
