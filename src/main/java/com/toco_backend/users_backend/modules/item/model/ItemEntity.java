package com.toco_backend.users_backend.modules.item.model;

import java.time.LocalDateTime;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.type.SqlTypes;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;

import com.toco_backend.users_backend.modules.user.model.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "items")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // un item pertenece a un único usuario, un usuario ninguno o muchos items
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner")
    private UserEntity owner;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "category", nullable = false)
    private String category;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", name = "images_urls")
    private Map<String, Object> imageUrls;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", name = "attributes")
    private Map<String, Object> attributes;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_status", nullable = false)
    private ItemStatus status;
    
    @Column(name = "likes_count", nullable = false)
    private Integer likes;

    @Version
    private Long version; 

    // --- METADATOS -- 
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
