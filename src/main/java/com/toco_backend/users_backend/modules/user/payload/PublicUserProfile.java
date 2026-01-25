package com.toco_backend.users_backend.modules.user.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublicUserProfile {

    private String username;
    private String firstName;
    private String lastName;
    private float rating;
    private Boolean isIdentityVerified;
    private String profileImageUrl;
}
