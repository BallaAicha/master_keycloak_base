package org.etutoria.keycloakauthservice.model;

public record NewUser(
        String username,
        String password,
        String firstName,
        String lastName
       ) {
}

