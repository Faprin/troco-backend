package com.toco_backend.users_backend.modules.user.payload;

import java.time.LocalDate;
import java.util.Map;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequestAndResponse {

    private String username;
    // private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private String phoneNumber;
    private String profileImageUrl;
    private Double latitude;
    private Double longitude;
    private Map<String, Object> preferences;
    
}
