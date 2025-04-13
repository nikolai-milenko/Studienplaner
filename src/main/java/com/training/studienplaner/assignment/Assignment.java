package com.training.studienplaner.assignment;

import com.training.studienplaner.course.Course;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
@NoArgsConstructor
@Entity
@Table(name = "assignment")
public class Assignment {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long assignmentId;

    @Column(
            name = "title",
            length = 50,
            nullable = false
    )
    private String title;

    @Column(
            name = "description"
    )
    private String description;

    public enum AssignmentType {
        HOMEWORK,
        PROJECT,
        EXAM,
        TEST,
        PRESENTATION,
        ESSAY,
        LAB
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssignmentType type;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

}
