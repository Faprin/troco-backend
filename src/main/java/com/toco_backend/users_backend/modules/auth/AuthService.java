package com.toco_backend.users_backend.modules.auth;

import java.util.HashMap;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.toco_backend.users_backend.modules.auth.payload.AuthResponse;
import com.toco_backend.users_backend.modules.auth.payload.LoginRequest;
import com.toco_backend.users_backend.modules.auth.payload.RegisterRequest;
import com.toco_backend.users_backend.modules.user.UserRepository;
import com.toco_backend.users_backend.modules.user.model.IdentityStatus;
import com.toco_backend.users_backend.modules.user.model.Role;
import com.toco_backend.users_backend.modules.user.model.UserEntity;
import com.toco_backend.users_backend.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public void Register(RegisterRequest request) {
        // Lógica de registro de usuario (omitted for brevity)
        UserEntity user = new UserEntity().builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .first_name(request.getFirst_name())
                .last_name(request.getLast_name())
                .birthdate(request.getBirthdate())
                .phone_number(request.getPhone_number())
                .profile_image_url(request.getProfile_image_url())
                .preferences(new HashMap<>())
                .rating((float) 0.0)
                .is_identity_verified(false)
                .identity_status(IdentityStatus.UNVERIFIED)
                .rejection_reason("")
                .role(Role.USER)
                .build();

        userRepository.save(user);
}

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), 
                        request.getPassword()
                )
        );

        // Si pasa la autenticación, buscamos al usuario para generar el token
        // Usamos el mismo input para buscar en ambos campos
        var user = userRepository.findByEmailOrUsername(request.getUsername(), request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado (Error inesperado tras auth)"));

        // 3. Generar Token
        var jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}
