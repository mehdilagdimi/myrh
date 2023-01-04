package com.mehdilagdimi.myrh.controller;



import com.mehdilagdimi.myrh.base.enums.UserRole;
import com.mehdilagdimi.myrh.base.exception.UserAlreadyExistAuthenticationException;
import com.mehdilagdimi.myrh.model.AuthenticationRequest;
import com.mehdilagdimi.myrh.model.Response;
import com.mehdilagdimi.myrh.model.SignupRequest;
import com.mehdilagdimi.myrh.model.entity.Agent;
import com.mehdilagdimi.myrh.model.entity.Employer;
import com.mehdilagdimi.myrh.model.entity.User;
import com.mehdilagdimi.myrh.service.UserService;
import com.mehdilagdimi.myrh.util.JwtHandler;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtHandler jwtHandler;

    public AuthController(@Lazy AuthenticationManager authenticationManager, UserService userService, JwtHandler jwtHandler) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtHandler = jwtHandler;
    }

    @PostMapping("/auth")
    public ResponseEntity<Response> authenticate(
            @RequestBody AuthenticationRequest authRequest
    ) {
        Response response = null;
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword())
            );

            final User user = (User) userService.loadUserByUsername(authRequest.getEmail());

            if(user != null){
                String jwt = jwtHandler.generateToken(user);
                response = new Response(
                        HttpStatus.OK,
                        "Successfully Logged in",
                        "data",
                        jwt
                );
            }

        } catch (AuthenticationException e) {
            e.printStackTrace();
            response = new Response(
                    HttpStatus.BAD_REQUEST,
                    "Failed to log in"
            );
        }
        return new ResponseEntity<>(response, response.getStatus());

    }


    @PostMapping("/signup")
    public ResponseEntity<Response> signup(
            @RequestBody SignupRequest signupRequest
    ){
        Response response = null;
        try{
            User user = userService.addUser(signupRequest);
            String jwt = jwtHandler.generateToken(user);

            response = new Response(
                    HttpStatus.CREATED,
                    "Successfully Signed up",
                    "data",
                    jwt
            );
        } catch (UserAlreadyExistAuthenticationException e){
            e.printStackTrace();
            response = new Response(
                    HttpStatus.BAD_REQUEST,
                    "Error signing up, user already exists"
            );
        } finally {
            return new ResponseEntity<>(response, response.getStatus());
        }

    }
}
