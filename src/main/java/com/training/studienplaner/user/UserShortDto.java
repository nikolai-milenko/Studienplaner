package com.training.studienplaner.user;

public record UserShortDto(
        String name,
        String surname,
        User.Role role
) {
}
