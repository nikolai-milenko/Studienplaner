package com.training.studienplaner.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("FindByEmail: sollte Benutzer per E-Mail finden")
    void shouldFindUserByEmail() {
        // given
        User user = User.builder()
                .name("Nikolay")
                .surname("Milenko")
                .role(User.Role.STUDENT)
                .email("email@example.com")
                .password("securePassword")
                .coursesList(new ArrayList<>())
                .build();

        userRepository.save(user);

        // when
        Optional<User> found = userRepository.findByEmail(user.getEmail());

        // then
        assertTrue(found.isPresent());
        assertEquals(user.getEmail(), found.get().getEmail());
    }

    @Test
    @DisplayName("FindByEmail: Leeres Optional, wenn Benutzer fehlt und Datenbank NICHT leer ist.")
    void shouldNotFindUserByEmail() {
        //given
        String searchingEmail = "existing@mail.com";
        User user = User.builder()
                .name("Nikolay")
                .surname("Milenko")
                .role(User.Role.STUDENT)
                .email("not_existing@mail.com")
                .password("securePassword")
                .coursesList(new ArrayList<>())
                .build();

        userRepository.save(user);

        //when
        Optional<User> found = userRepository.findByEmail(searchingEmail);

        //then
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("FindByEmail: Leeres Optional, wenn Benutzer fehlt und Datenbank leer ist.")
    void shouldNotFindUserByEmailDBEmpty() {
        //given
        String searchingEmail = "existing@mail.com";

        //when
        Optional<User> found = userRepository.findByEmail(searchingEmail);

        //then
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("FindAllByRole: sollte alle Benutzer mit gegebener Rolle finden")
    void shouldFindAllUsersByRole() {
        // given
        User student1 = User.builder()
                .name("Student1")
                .surname("Test")
                .role(User.Role.STUDENT)
                .email("student1@example.com")
                .password("password")
                .coursesList(new ArrayList<>())
                .build();

        User student2 = User.builder()
                .name("Student2")
                .surname("Test")
                .role(User.Role.STUDENT)
                .email("student2@example.com")
                .password("password")
                .coursesList(new ArrayList<>())
                .build();

        User teacher = User.builder()
                .name("Teacher")
                .surname("Test")
                .role(User.Role.TEACHER)
                .email("teacher@example.com")
                .password("password")
                .coursesList(new ArrayList<>())
                .build();

        userRepository.save(student1);
        userRepository.save(student2);
        userRepository.save(teacher);

        // when
        List<User> students = userRepository.findAllByRole(User.Role.STUDENT);

        // then
        assertEquals(2, students.size());
        assertTrue(students.contains(student1));
        assertTrue(students.contains(student2));
    }

    @Test
    @DisplayName("FindAllByRole: sollte leere Liste zurückgeben, wenn keine Benutzer mit gegebener Rolle existieren")
    void shouldReturnEmptyListWhenNoUsersWithGivenRole() {
        // given
        User teacher = User.builder()
                .name("Teacher")
                .surname("Test")
                .role(User.Role.TEACHER)
                .email("teacher@example.com")
                .password("password")
                .coursesList(new ArrayList<>())
                .build();

        userRepository.save(teacher);

        // when
        List<User> students = userRepository.findAllByRole(User.Role.STUDENT);

        // then
        assertTrue(students.isEmpty());
    }

    @Test
    @DisplayName("FindAllByRole: sollte leere Liste zurückgeben, wenn Datenbank leer ist")
    void shouldReturnEmptyListWhenDatabaseIsEmpty() {
        // when
        List<User> students = userRepository.findAllByRole(User.Role.STUDENT);

        // then
        assertTrue(students.isEmpty());
    }

}

