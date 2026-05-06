package com.weatherwise.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private String username;
    private String email;

    public AuthResponse(String token, String username, String email) {
        this.token = token;
        this.tokenType = "Bearer";
        this.username = username;
        this.email = email;
    }
}
