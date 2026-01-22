package com.toco_backend.users_backend.modules.auth;

import java.util.HashMap;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.toco_backend.users_backend.common.utils.GeometryUtil;
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

    public AuthResponse register(RegisterRequest request) {

        if(userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Usuario existente");
        }

        // NO EXISTE: Construiri nuevo usuario
        UserEntity user =  UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .birthdate(request.getBirthdate())
                .phoneNumber(request.getPhoneNumber())
                .profileImageUrl(request.getProfileImageUrl())
                .preferences(new HashMap<>()) // TODO: Podría tomarse en la página de inicio
                .rating((float) 0.0)
                .isIdentityVerified(false)
                .identityStatus(IdentityStatus.UNVERIFIED)
                .rejectionReason("")
                .role(Role.USER)
                .location(GeometryUtil.createPoint(request.getLatitude(), request.getLongitude()))
                .build();

        userRepository.save(user);

        return AuthResponse.builder()
                .username(user.getUsername())
                .token(jwtService.generateToken(user))
                .build();
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
        UserEntity user = userRepository.findByEmailOrUsername(request.getUsername(), request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado (Error inesperado tras auth)"));

        // 3. Generar Token
        var jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .username(user.getUsername())
                .token(jwtToken)
                .build();
    }
}
