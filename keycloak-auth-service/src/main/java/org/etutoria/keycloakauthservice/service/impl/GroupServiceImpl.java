package org.etutoria.keycloakauthservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etutoria.keycloakauthservice.service.GroupService;
import org.etutoria.keycloakauthservice.service.UserService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service

public class GroupServiceImpl implements GroupService {
    private final UserService userService;
    private final Keycloak keycloak;
    @Override
    public void assignGroup(String userId, String groupId) {
        UserResource user = userService.getUser(userId);
        user.joinGroup(groupId);


    }

    @Override
    public void deleteGroupFromUser(String userId, String groupId) {
        UserResource user = userService.getUser(userId);
        user.leaveGroup(groupId);


    }
}
