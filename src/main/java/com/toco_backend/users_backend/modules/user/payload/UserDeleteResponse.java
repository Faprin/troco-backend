package com.toco_backend.users_backend.modules.user.payload;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDeleteResponse {

    private Boolean isActive;
    private LocalDateTime deletionRequestedAt;
    
}
