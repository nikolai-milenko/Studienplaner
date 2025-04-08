package com.training.studienplaner.submission;

import com.training.studienplaner.assignment.Assignment;
import com.training.studienplaner.assignment.AssignmentRepository;
import com.training.studienplaner.course.Course;
import com.training.studienplaner.course.CourseRepository;
import com.training.studienplaner.user.User;
import com.training.studienplaner.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SubmissionRepositoryTest {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Course prepareCourse() {
        Course course = Course.builder()
                .title("Test Course")
                .description("Course Description")
                .ects((short) 5)
                .build();
        return courseRepository.save(course);
    }

    private Assignment prepareAssignment(Course course) {
        Assignment assignment = Assignment.builder()
                .title("Test Assignment")
                .description("Assignment Description")
                .type(Assignment.AssignmentType.HOMEWORK)
                .deadline(LocalDateTime.now().plusDays(7))
                .course(course)
                .build();
        return assignmentRepository.save(assignment);
    }

    private User prepareStudent(Course course) {
        User user = User.builder()
                .name("Test")
                .surname("Student")
                .role(User.Role.STUDENT)
                .email("student@example.com")
                .password("password")
                .coursesList(List.of(course))
                .build();
        return userRepository.save(user);
    }

    private Submission prepareSubmission(Assignment assignment, User student) {
        Submission submission = Submission.builder()
                .assignment(assignment)
                .student(student)
                .content("Submission content")
                .status(Submission.Status.SUBMITTED)
                .build();
        return submissionRepository.save(submission);
    }

    @Test
    @DisplayName("findByAssignmentAssignmentId: sollte alle Abgaben für eine bestimmte Aufgabe finden")
    void shouldFindSubmissionsByAssignmentId() {
        Course course = prepareCourse();
        Assignment assignment = prepareAssignment(course);
        User student = prepareStudent(course);
        prepareSubmission(assignment, student);

        List<Submission> submissions = submissionRepository.findByAssignmentAssignmentId(assignment.getAssignmentId());

        assertThat(submissions).isNotEmpty();
        assertThat(submissions.get(0).getAssignment().getAssignmentId()).isEqualTo(assignment.getAssignmentId());
    }

    @Test
    @DisplayName("findByAssignmentAssignmentId: sollte leere Liste zurückgeben, wenn keine Abgaben existieren")
    void shouldNotFindSubmissionsByAssignmentId() {
        List<Submission> submissions = submissionRepository.findByAssignmentAssignmentId(999L);

        assertThat(submissions).isEmpty();
    }

    @Test
    @DisplayName("findByStudentUserId: sollte alle Abgaben für einen bestimmten Studenten finden")
    void shouldFindSubmissionsByStudentId() {
        Course course = prepareCourse();
        Assignment assignment = prepareAssignment(course);
        User student = prepareStudent(course);
        prepareSubmission(assignment, student);

        List<Submission> submissions = submissionRepository.findByStudentUserId(student.getUserId());

        assertThat(submissions).isNotEmpty();
        assertThat(submissions.get(0).getStudent().getUserId()).isEqualTo(student.getUserId());
    }

    @Test
    @DisplayName("findByStudentUserId: sollte leere Liste zurückgeben, wenn keine Abgaben existieren")
    void shouldNotFindSubmissionsByStudentId() {
        List<Submission> submissions = submissionRepository.findByStudentUserId(999L);

        assertThat(submissions).isEmpty();
    }
}
