package com.toco_backend.users_backend.modules.item.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemResponse {

    private Long id;
    private String title;
    private String mainImageUrl;
    private Double distanceKm;
    private String ownerUsername;
}
