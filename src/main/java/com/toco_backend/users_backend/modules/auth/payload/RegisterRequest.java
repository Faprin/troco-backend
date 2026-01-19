package com.toco_backend.users_backend.modules.auth.payload;

import java.sql.Date;

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
    private String first_name;
    private String last_name;
    private Date birthdate;
    private String phone_number;
    private String profile_image_url;
    // TODO: manejar location
    // private Point location; 
}
