package com.mehdilagdimi.myrh.model;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AuthenticationRequest {
    private final String email;
    private final String password;
}