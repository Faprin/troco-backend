package com.toco_backend.users_backend.modules.auth.payload;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private String phoneNumber;
    private String profileImageUrl;
    private Double latitude;
    private Double longitude; 
}
