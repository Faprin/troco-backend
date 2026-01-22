package com.toco_backend.users_backend.modules.user;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.toco_backend.users_backend.modules.user.model.UserEntity;
import com.toco_backend.users_backend.modules.user.payload.UserProfileResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    public final UserRepository repository;

    /**
     * Método encargado de devolver toda la información relativa del usuario para que pueda consultarla sin problema
     * @param id identificador del usuario en la base de datos
     * @return un objeto con toda la información que debe devolver
     */
    public UserProfileResponse getUserProfile(Long id) {

        UserEntity user = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    
        return UserProfileResponse.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .birthdate(user.getBirthdate())
            .phoneNumber(user.getPhoneNumber()) 
            .profileImageUrl(user.getProfileImageUrl())
            .latitude(user.getLocation() != null ? user.getLocation().getY() : null)
            .longitude(user.getLocation() != null ? user.getLocation().getX() : null) 
            .preferences(user.getPreferences() != null ? user.getPreferences() : new HashMap<>())
            .rating(user.getRating())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .identityStatus(user.getIdentityStatus())
            .rejectionReason(user.getRejectionReason())
            .isIdentityVerified(user.getIsIdentityVerified())
            .build(); 
    }
}
