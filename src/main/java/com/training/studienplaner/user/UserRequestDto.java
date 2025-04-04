package com.training.studienplaner.user;

public record UserRequestDto(
        String name,
        String surname,
        User.Role role,
        String email,
        String password
) {
}
