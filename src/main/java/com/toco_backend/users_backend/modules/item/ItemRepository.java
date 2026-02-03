package com.toco_backend.users_backend.modules.item;

import java.util.List;

import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.toco_backend.users_backend.modules.item.model.ItemEntity;
import com.toco_backend.users_backend.modules.item.model.ItemStatus;
import com.toco_backend.users_backend.modules.user.model.UserEntity;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

        @Query("SELECT i FROM ItemEntity i " +
                        "WHERE i.status = 'AVAILABLE' " +
                        "AND ST_DistanceSphere(i.owner.location, :userLocation) <= :radiusMeters " +
                        "AND (:category IS NULL OR i.category = :category) " +
                        "AND (:keyword IS NULL OR LOWER(i.title) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
                        "ORDER BY ST_DistanceSphere(i.owner.location, :userLocation) ASC")
        Page<ItemEntity> searchNearby(
                        @Param("userLocation") Point userLocation,
                        @Param("radiusMeters") double radiusMeters,
                        @Param("category") String category,
                        @Param("keyword") String keyword,
                        Pageable pageable);

        
        List<ItemEntity> findAllByOwnerOrderByCreatedAtDesc(UserEntity owner);
        
        List<ItemEntity> findAllByOwnerAndStatusOrderByCreatedAtDesc(UserEntity owner, ItemStatus status);

}
