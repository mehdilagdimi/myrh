package com.mehdilagdimi.myrh.controller;



import com.mehdilagdimi.myrh.base.enums.UserRole;
import com.mehdilagdimi.myrh.base.exception.UserAlreadyExistAuthenticationException;
import com.mehdilagdimi.myrh.model.AuthenticationRequest;
import com.mehdilagdimi.myrh.model.Response;
import com.mehdilagdimi.myrh.model.SignupRequest;
import com.mehdilagdimi.myrh.model.entity.Agent;
import com.mehdilagdimi.myrh.model.entity.Employer;
import com.mehdilagdimi.myrh.model.entity.User;
import com.mehdilagdimi.myrh.service.EmailService;
import com.mehdilagdimi.myrh.service.UserService;
import com.mehdilagdimi.myrh.util.JwtHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtHandler jwtHandler;
    private final EmailService emailService;

    @Autowired
    public SimpleMailMessage emailTemplate;


    public AuthController(@Lazy AuthenticationManager authenticationManager, UserService userService, JwtHandler jwtHandler, EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtHandler = jwtHandler;
        this.emailService = emailService;
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
            if(signupRequest.getRole().toString().equals("ROLE_AGENT")) throw new IllegalArgumentException();
            User user = userService.addUser(signupRequest);
            String jwt = jwtHandler.generateToken(user);

            String msg = String.format(emailTemplate.getText(), user.getEmail());
            emailService.sendSimpleMessage(user.getEmail(), "myHR Account Creation", msg);

            response = new Response(
                    HttpStatus.CREATED,
                    "Successfully Signed up",
                    "data",
                    jwt
            );
        } catch (UserAlreadyExistAuthenticationException | IllegalArgumentException e){
            e.printStackTrace();
            response = new Response(
                    HttpStatus.BAD_REQUEST,
                    "Error signing up, user already exists or invalid request"
            );
        } finally {
            return new ResponseEntity<>(response, response.getStatus());
        }

    }

//    @GetMapping("/get-user")
//    public Map<String, Object> getUser(@AuthenticationPrincipal OAuth2User principal) {
////        System.out.println(" path var auth " + regID);
//        System.out.println(" inside get auth user ");
//        return Collections.singletonMap("name", principal.getAttribute("name"));
//    }
}
