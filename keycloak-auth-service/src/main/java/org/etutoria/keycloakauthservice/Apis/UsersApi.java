package org.etutoria.keycloakauthservice.Apis;

import lombok.RequiredArgsConstructor;
import org.etutoria.keycloakauthservice.model.NewUser;
import org.etutoria.keycloakauthservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersApi {
    private final UserService userService;
    @PostMapping
    public ResponseEntity<?> createUser(
            @RequestBody NewUser newUser){
        userService.createUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}/send-verify-email")
    public ResponseEntity<?> sendVerifyEmailToUser(
            @PathVariable String id){
        userService.sendVerificationEmail(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable String id){
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestParam String username){
        userService.forgotPassword(username);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @GetMapping("/{id}/roles")
    public ResponseEntity<?> getUserRoles(@PathVariable String id) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserRoles(id));
    }
    @GetMapping("/{id}/groups")
    public ResponseEntity<?> getUserGroups(@PathVariable String id) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserGroups(id));
    }


}
