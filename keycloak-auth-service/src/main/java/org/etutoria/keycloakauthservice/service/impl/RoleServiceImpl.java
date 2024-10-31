package org.etutoria.keycloakauthservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.etutoria.keycloakauthservice.constantes.KeycloakConstantes;
import org.etutoria.keycloakauthservice.service.RoleService;
import org.etutoria.keycloakauthservice.service.UserService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final UserService userService;
    private final Keycloak keycloak;

    @Override
    public void assignRole(String userId, String roleName) {
        // Get the UserResource for the specified userId
        UserResource userResource = userService.getUser(userId);

        // Obtain the RoleRepresentation for the roleName
        RoleRepresentation roleToAdd = keycloak.realm(KeycloakConstantes.REALM)
                .roles().get(roleName).toRepresentation();

        // Assign the role to the user
        userResource.roles().realmLevel().add(Collections.singletonList(roleToAdd));
    }



    @Override
    public void deleteRoleFromUser(String userId, String roleName) {
        // Get the UserResource for the specified userId
        UserResource userResource = userService.getUser(userId);

        // Obtain the RoleRepresentation for the roleName
        RoleRepresentation roleToRemove = keycloak.realm(KeycloakConstantes.REALM)
                .roles().get(roleName).toRepresentation();

        // Remove the role from the user
        userResource.roles().realmLevel().remove(Collections.singletonList(roleToRemove));

    }


}
