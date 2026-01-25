package com.toco_backend.users_backend.modules.user;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.toco_backend.users_backend.modules.user.model.IdentityStatus;
import com.toco_backend.users_backend.modules.user.model.UserEntity;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserCleanupService {

    private final UserRepository repository;

    /**
     * Metodo encargado todos los días a las 4 de la mañana de eliminar aquellas cuentas que hace 30 días solicitaron 
     * que se eliminaran
     */
    @Scheduled(cron = "0 0 4 * * *")
    @Transactional
    public void deleteExpiredAccounts() {
        LocalDateTime expiredTime = LocalDateTime.now().minusDays(30);
    
        List<UserEntity> usersToDelete = repository.findAllByDeletionRequestedAtBefore(expiredTime);
        
        usersToDelete.forEach(user -> anonymizeUser(user));
    }

    /**
     * En lugar de eliminarlo del todo, decido anonimizar al usuario para mantener el historial de interecambio del 
     * resto del usuarios
     * @param user usuario que va a ser anonimizado
     */
    public void anonymizeUser(UserEntity user) {
        user.setFirstName("Usuario");
        user.setLastName("Eliminado");
        user.setEmail("deleted_" + user.getId() + "@troco.app"); // mantenemos el unique constraint
        user.setUsername("deleted_" + user.getId());
        user.setPassword("");
        user.setLocation(null);
        user.setProfileImageUrl(null);
        user.setDeletionRequestedAt(null);
        user.setIsIdentityVerified(false);
        user.setIdentityStatus(IdentityStatus.UNVERIFIED);
        user.setRejectionReason("");
        user.setRating(0.0f);
        user.setRatingCount(0);
        repository.save(user);
    }
}
