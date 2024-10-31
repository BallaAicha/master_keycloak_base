package org.etutoria.keycloakauthservice.service.impl;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.etutoria.keycloakauthservice.constantes.KeycloakConstantes;
import org.etutoria.keycloakauthservice.model.NewUser;
import org.etutoria.keycloakauthservice.service.UserService;
import org.keycloak.admin.client.Keycloak;

import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImp implements UserService {
    private final Keycloak keycloak;

    @Override
    public void createUser(NewUser newUser) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEnabled(true);//pour activer le compte de l'utilisateur par défaut
        userRepresentation.setFirstName(newUser.firstName());
        userRepresentation.setLastName(newUser.lastName());
        userRepresentation.setUsername(newUser.username());
        userRepresentation.setEmail(newUser.username());
        userRepresentation.setEmailVerified(false);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(newUser.password());
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        userRepresentation.setCredentials(List.of(credentialRepresentation));//pour ajouter le mot de passe à l'utilisateur lors de la création
        UsersResource usersResource = getUsersResource();// on récupère la ressource UsersResource cad l'ensemble des utilisateurs pour pouvoir créer un utilisateur car on ne peut pas créer un utilisateur sans passer par UsersResource
        Response response= usersResource.create(userRepresentation);
        log.info("Status code"+response.getStatus());
        if(!Objects.equals(201,response.getStatus())){

            throw  new RuntimeException("Status code "+response.getStatus());
        }
        log.info("New User has been created");
        //on recherche l'utilisateur par son username pour récupérer son id afin de pouvoir envoyer un email de vérification,le paramètre true signifie qu'on veut une recherche exacte
       List<UserRepresentation> userRepresentations= usersResource.searchByUsername(newUser.username(),true);
         if(userRepresentations.isEmpty()){
              throw new RuntimeException("User not found");
         }
        //on récupère le premier utilisateur trouvé cad le seul utilisateur trouvé
        UserRepresentation userRepresentation1 = userRepresentations.get(0);
        log.info("User id: "+userRepresentation1.getId());
        sendVerificationEmail(userRepresentation1.getId());


    }

    @Override
    public void sendVerificationEmail(String userId) {
        UsersResource usersResource = getUsersResource();
        usersResource.get(userId).sendVerifyEmail();

    }



    @Override
    public void deleteUser(String userId) {
        UsersResource usersResource = getUsersResource();
        Response response = usersResource.delete(userId);
        log.info("Status code: "+response.getStatus());
        if(!Objects.equals(204,response.getStatus())){
            throw new RuntimeException("Status code "+response.getStatus());
        }
        log.info("User has been deleted");

    }

    @Override
    public void forgotPassword(String username) {
        UsersResource usersResource = getUsersResource();
        List<UserRepresentation> userRepresentations = usersResource.searchByUsername(username, true);
        if(userRepresentations.isEmpty()){
            throw new RuntimeException("User not found");
        }
        UserRepresentation userRepresentation = userRepresentations.get(0);
        usersResource.get(userRepresentation.getId()).executeActionsEmail(List.of("UPDATE_PASSWORD"));

    }



    @Override
    public UserResource getUser(String userId) {
        UsersResource usersResource = getUsersResource();
        return usersResource.get(userId);
    }



    private UsersResource getUsersResource(){

      return   keycloak.realm(KeycloakConstantes.REALM).users();

    }


    @Override
    public List<RoleRepresentation> getUserRoles(String userId) {


        return getUser(userId).roles().realmLevel().listAll();
    }
    @Override
    public List<GroupRepresentation> getUserGroups(String userId) {


        return getUser(userId).groups();
    }


}
