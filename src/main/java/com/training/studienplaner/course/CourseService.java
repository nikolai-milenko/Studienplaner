package com.training.studienplaner.course;

import com.training.studienplaner.assignment.Assignment;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.training.studienplaner.user.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
    }

    public void deleteCourseById(Long id) {
        Course course = getCourseById(id);
        courseRepository.delete(course);
    }

    public List<Assignment> getAssignmentsByCourseId(Long courseId) {
        Course course = getCourseById(courseId);
        return course.getAssignments();
    }

    public List<User> getStudentsByCourseId(Long courseId) {
        Course course = getCourseById(courseId);
        return course.getStudents();
    }

}
