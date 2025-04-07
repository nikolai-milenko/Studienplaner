package com.training.studienplaner.user;

import jakarta.validation.constraints.*;

public record UserRequestDto(
        @NotBlank(message = "Name darf nicht leer sein")
        @Size(max = 30, message = "Name darf maximal 30 Zeichen lang sein")
        String name,

        @NotBlank(message = "Nachname darf nicht leer sein")
        @Size(max = 50, message = "Nachname darf maximal 50 Zeichen lang sein")
        String surname,

        @NotNull(message = "Rolle muss angegeben werden")
        User.Role role,

        @NotBlank(message = "E-Mail darf nicht leer sein")
        @Email(message = "Ungültige E-Mail Adresse")
        @UniqueEmail
        String email,

        @NotBlank(message = "Passwort darf nicht leer sein")
        @Size(min = 6, message = "Passwort muss mindestens 6 Zeichen lang sein")
        String password
) {
}
