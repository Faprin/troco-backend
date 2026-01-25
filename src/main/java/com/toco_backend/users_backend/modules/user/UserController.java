package com.toco_backend.users_backend.modules.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toco_backend.users_backend.modules.user.payload.ChangePasswordRequest;
import com.toco_backend.users_backend.modules.user.payload.PublicUserProfile;
import com.toco_backend.users_backend.modules.user.payload.UpdateUserRequestAndResponse;
import com.toco_backend.users_backend.modules.user.payload.UserDeleteResponse;
import com.toco_backend.users_backend.modules.user.payload.UserProfileResponse;

import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {


    // TODO EL CONTROLLER COMPLETO
    private final UserService service;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(Principal user) {
        // si el usuario llega hasta aquí es que está log ya

        String usernameLog = user.getName(); // devuelve el username
        return ResponseEntity.ok(service.getUserProfile(usernameLog));
    }
    

    @PatchMapping("/profile")
    public ResponseEntity<UpdateUserRequestAndResponse> updateProfile(Principal user, @RequestBody UpdateUserRequestAndResponse request) {
        
        // si el usuario llega hasta aquí es que está log
        String usernameLog = user.getName();
        return ResponseEntity.ok(service.updateProfile(usernameLog, request));
    }

    /**
     * Encargado de poner la fecha de eliminacion de la cuenta que se quiere eliminar
     * @param user usuario que quiere eliminar la cuenta
     * @return Objeto con la fecha en la que se eliminara la cuenta y la inactivación lógica de la misma
     */
    @DeleteMapping("/profile")
    public ResponseEntity<UserDeleteResponse> deleteUser(Principal user) {

        String usernameLog = user.getName();
        return ResponseEntity.ok(service.deleteUser(usernameLog));
    }
    
    /**
     *  Si vuelve a iniciar sesión al entrar en esta ruta (/reactivate) entonces se cancela la desactivacion
     * @param user usuario que desea cancelar la eliminación de su cuenta
     * @return
     */
    @GetMapping("/reactivate")
    public ResponseEntity<String> cancelDeletion(Principal user) {

        String usernameLog = user.getName();
        return ResponseEntity.ok(service.cancelDeletion(usernameLog));
    }
    

    /**
     * Endpoint que procesa el cambio de contraseña de la cuenta asociada a un usuario
     * @param user usuario que solicita desde su perfil ya logueado el cambio de contraseña
     * @param changePasswordRequest objeto que recuperamos del cuerpo de la peticion y normalizamos
     * @return 
     */
    @PatchMapping("/password-update")
    public ResponseEntity<?> passwordUpdate(Principal user, @RequestBody ChangePasswordRequest changePasswordRequest) {

        String usernameLog = user.getName();
        service.updatePassword(usernameLog, changePasswordRequest);
        return ResponseEntity.ok(Collections.singletonMap("message", "Contraseña cambiada con exito"));
    }


    /**
     * Endpoint que devuelve la información pública de un usuario para que el resto puedan distinguirlo
     * @param username usuario del que se desea conocer más información
     * @return 
     */
    @GetMapping("/{username}")
    public ResponseEntity<PublicUserProfile> getPublicUserProfile(@PathVariable String username) {
        
        return ResponseEntity.ok((service.getPublicUserProfile(username)));
    } 
    
    
    // POST /verify-identity
}
