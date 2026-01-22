package com.toco_backend.users_backend.modules.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {


    // TODO EL CONTROLLER COMPLETO
    private final UserService service;

    // GET profile
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestParam Long id) {
        return ResponseEntity.ok(service.getUserProfile(id));
    }
    

    // PATCH profile

    // DELETE profile

    // PATCH change pass

    // GET informacion publica del perfil (red social)

    // POST /{id}/rating

    // POST /verify-identity
}
