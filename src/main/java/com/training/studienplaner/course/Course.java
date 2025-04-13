package com.training.studienplaner.course;


import com.training.studienplaner.assignment.Assignment;
import com.training.studienplaner.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
@NoArgsConstructor
@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long courseId;

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

    @ManyToOne
    @JoinColumn(name = "tutor_user_id")
    private User tutor;

    @Column(name = "ects")
    private short ects;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Assignment> assignments;

    @ManyToMany(mappedBy = "coursesList")
    private List<User> students;
}
