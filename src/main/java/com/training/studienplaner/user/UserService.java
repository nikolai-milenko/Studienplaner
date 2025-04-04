package com.training.studienplaner.user;

import com.training.studienplaner.course.Course;
import com.training.studienplaner.course.CourseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService{
    private final UserRepository userRepository;
    private final CourseService courseService;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) throws EntityNotFoundException{
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public void deleteById(Long id) throws EntityNotFoundException{
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found by email"));
    }

    public List<User> getAllStudents(User.Role role) {
        List<User> students = userRepository.findAllByRole(role);
        if (students.isEmpty()) {
            throw new EntityNotFoundException("There are no students");
        }
        return students;
    }

    public List<Course> getAllCoursesForUser(Long userId){
        return this.getById(userId).getCoursesList();
    }

    public void enrollUserToCourse(Long userId, Long courseId){
        User user = getById(userId);
        Course course = courseService.getCourseById(courseId);
        user.getCoursesList().add(course);
        userRepository.save(user);
    }
}
