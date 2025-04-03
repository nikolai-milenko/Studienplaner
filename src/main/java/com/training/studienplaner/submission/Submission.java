package com.training.studienplaner.submission;
import com.training.studienplaner.assignment.Assignment;
import com.training.studienplaner.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "submission")
public class Submission {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long submissionId;

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Column(name = "submittedAt")
    private LocalDateTime submittedAt;

    @Column(columnDefinition = "TEXT")
    private String content;

    public enum Status {
        NOT_SUBMITTED,
        SUBMITTED,
        REVIEWED,
        GRADED
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column
    private Short grade;

    @PrePersist
    protected void onCreate() {
        if (this.submittedAt == null) {
            this.submittedAt = LocalDateTime.now();
        }
    }
}
