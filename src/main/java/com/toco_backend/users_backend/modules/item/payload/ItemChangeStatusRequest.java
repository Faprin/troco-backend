package com.toco_backend.users_backend.modules.item.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemChangeStatusRequest 
{
    private String status;
}
