package com.toco_backend.users_backend.modules.like;

import com.toco_backend.users_backend.modules.item.model.ItemEntity;
import com.toco_backend.users_backend.modules.like.model.LikeEntity;
import com.toco_backend.users_backend.modules.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    Optional<LikeEntity> findByUserAndItem(UserEntity user, ItemEntity item);
    boolean existsByUserAndItem(UserEntity user, ItemEntity item);
}
