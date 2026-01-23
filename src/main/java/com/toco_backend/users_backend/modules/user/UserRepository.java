package com.toco_backend.users_backend.modules.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toco_backend.users_backend.modules.user.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    List<UserEntity> findAllByDeletionRequestedAtBefore(LocalDateTime dateTime);

    Optional<UserEntity> findByEmailOrUsername(String email, String username);    
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
