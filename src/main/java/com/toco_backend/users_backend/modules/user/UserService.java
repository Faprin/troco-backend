package com.toco_backend.users_backend.modules.user;

import java.time.LocalDateTime;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.toco_backend.users_backend.common.utils.GeometryUtil;
import com.toco_backend.users_backend.modules.user.model.UserEntity;
import com.toco_backend.users_backend.modules.user.payload.UpdateUserRequestAndResponse;
import com.toco_backend.users_backend.modules.user.payload.UserDeleteResponse;
import com.toco_backend.users_backend.modules.user.payload.UserProfileResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    /**
     * Método encargado de devolver toda la información relativa del usuario para
     * que pueda consultarla sin problema
     * 
     * @param id identificador del usuario en la base de datos
     * @return un objeto con toda la información que debe devolver
     */
    public UserProfileResponse getUserProfile(String username) {

        UserEntity user = getSafeUserByUsername(username);

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

    /**
     * Encargado de actualizar la informacion personal de un usuario de la base de datos
     * @param username usuario que se ha autenticado y que se debe de actualizar en la base de datos
     * @param request parametros que quiere cambiar el usuario
     * @return Objeto encargado de normalizar tanto la respuesta como la peticion de este patch
     */
    public UpdateUserRequestAndResponse updateProfile(String username, UpdateUserRequestAndResponse request) {
        
        UserEntity user = getSafeUserByUsername(username);

        if (request.getUsername() != null)
            user.setUsername(request.getUsername());

        if (request.getFirstName() != null)
            user.setFirstName(request.getFirstName());

        if (request.getLastName() != null)
            user.setLastName(request.getLastName());

        if (request.getBirthdate() != null)
            user.setBirthdate(request.getBirthdate());

        if (request.getPhoneNumber() != null)
            user.setPhoneNumber(request.getPhoneNumber());

        if (request.getProfileImageUrl() != null)
            user.setProfileImageUrl(request.getProfileImageUrl());

        if (request.getLatitude() != null || request.getLongitude() != null)
            user.setLocation(GeometryUtil.createPoint(request.getLatitude(), request.getLongitude()));
        
        if(request.getPreferences() != null) 
            user.setPreferences(request.getPreferences());

        repository.save(user);

        return mapToUpdateUserRequestAndResponse(user);
    }

    /**
     * Metodo que da de baja lógica del sistema a un usuario. También inicializa el periodo de gracia que hay que cumplir segun RGPD
     * @param username nombre del usuario que desea la baja
     * @return objeto que normaliza la respuesta al cliente
     */
    public UserDeleteResponse deleteUser(String username) {

        UserEntity user = getSafeUserByUsername(username);

        // borrado lógico siempre 
        user.setDeletionRequestedAt(LocalDateTime.now());
        user.setIsActive(false);
        repository.save(user);

        return UserDeleteResponse.builder()
            .deletionRequestedAt(user.getDeletionRequestedAt())
            .isActive(false)
            .build();
    }

    public String cancelDeletion(String username) {
        UserEntity user = getSafeUserByUsername(username);

        user.setDeletionRequestedAt(null);;

        repository.save(user);

        return user.getUsername();
    }

    /**
     * Normalizacion de los datos para un UserRequestAndResponse
     * @param user usuario que desea ser normalizado
     * @return 
     */
    private UpdateUserRequestAndResponse mapToUpdateUserRequestAndResponse(UserEntity user) {
        return UpdateUserRequestAndResponse.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthdate(user.getBirthdate())
                .phoneNumber(user.getPhoneNumber())
                .profileImageUrl(user.getProfileImageUrl())
                // Lógica de coordenadas segura
                .latitude(user.getLocation() != null ? user.getLocation().getY() : null)
                .longitude(user.getLocation() != null ? user.getLocation().getX() : null)
                .preferences(user.getPreferences() != null ? user.getPreferences() : new HashMap<>())
                .build();
    }

    /**
     * Si el usuario existe, lo devuelve. En caso contrario lanza un RuntimeException diciendo que el usuario 
     * no fue encontrado
     * @param username Nombre de usuario a buscar
     * @return Usuario en caso de que haya sido encontrado
     */
    public UserEntity getSafeUserByUsername(String username) {
        return repository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
