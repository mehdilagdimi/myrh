package com.mehdilagdimi.myrh.model;


import com.mehdilagdimi.myrh.base.enums.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AuthenticationRequest {
    private final String password;
    private final String email;
    private final String idToken;
    private final UserRole role;
}