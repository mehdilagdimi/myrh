package com.mehdilagdimi.myrh.model;


import com.mehdilagdimi.myrh.base.enums.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class SignupRequest {

    private final String email;
    private final String username;
    private final String address;
    private final String tele;
    private final String password;
    private final UserRole role;

}