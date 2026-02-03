package com.toco_backend.users_backend.modules.item.payload;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateItemRequest {

    // owner y location se heredan

    private String title;
    private String description;
    private String category;
    private Map<String, Object> attributes;
    private Map<String, Object> imageUrls;
     
}
