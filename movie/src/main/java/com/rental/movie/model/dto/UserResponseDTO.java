package com.rental.movie.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.rental.movie.common.AuthProvider;
import com.rental.movie.common.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserResponseDTO {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("FullName")
    private String fullName;
    @JsonProperty("Email")
    private String email;
    @JsonProperty("Password")
    private String password;
    @JsonProperty("AuthProvider")
    private AuthProvider authProvider;
    @JsonProperty("Role")
    private Role role;
}
