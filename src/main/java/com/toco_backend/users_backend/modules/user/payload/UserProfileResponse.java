package com.toco_backend.users_backend.modules.user.payload;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import com.toco_backend.users_backend.modules.user.model.IdentityStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {

    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private String phoneNumber;
    private String profileImageUrl;
    private Double latitude;
    private Double longitude;
    private Map<String, Object> preferences;
    private float rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private IdentityStatus identityStatus;
    private String rejectionReason;
    private Boolean isIdentityVerified;
}
