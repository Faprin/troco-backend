package com.toco_backend.users_backend.modules.item.payload;

import java.util.Map;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemDetailResponse {
    private Long id;
    private String title;
    private Map<String, Object> imageUrls;
    private String ownerUsername;
    private String status;
    private Integer likes;
    private Boolean isLikeByMe;
}
