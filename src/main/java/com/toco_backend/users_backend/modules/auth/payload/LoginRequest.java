package com.toco_backend.users_backend.modules.auth.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String username; // Aquí el usuario puede escribir su Email O su Username
    private String password;
}