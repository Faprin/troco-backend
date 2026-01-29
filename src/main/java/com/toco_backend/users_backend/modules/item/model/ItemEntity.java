package com.toco_backend.users_backend.modules.item.model;

import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.toco_backend.users_backend.modules.user.model.UserEntity;

import jakarta.persistence.Column;

public class ItemEntity {

    private Long id;
    private UserEntity owner;
    private String title;
    private String description;
    private String category;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb" name = "images_urls")
    private Map<String, Object> images_urls;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb" name = "attributes")
    private Map<String, Object> attributes;

    private ItemStatus status;
    
    private Integer likes;

    private Version version; 
}
