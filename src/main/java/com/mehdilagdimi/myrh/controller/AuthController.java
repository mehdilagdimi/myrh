package com.mehdilagdimi.myrh.controller;



import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
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
import jakarta.mail.SendFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtHandler jwtHandler;
    private final EmailService emailService;

    @Autowired
    public SimpleMailMessage emailTemplate;


    private String CLIENT_ID = "81862977324-aehpi5f820aevtjmcdscj657eqiameos.apps.googleusercontent.com";


    public AuthController(@Lazy AuthenticationManager authenticationManager, UserService userService, JwtHandler jwtHandler, EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtHandler = jwtHandler;
        this.emailService = emailService;
    }

    @PostMapping("/auth")
    public ResponseEntity<Response> authenticate(
            @RequestBody AuthenticationRequest authRequest,
            @RequestParam(name = "oauth", required = false) Optional<Boolean> isOauth
            ) {
        Response response = null;
        try{
            System.out.println(" is oath " + isOauth);
            User user;
            if(isOauth.orElse(false)) {
               String oauthEmail = oauthAuthenticate(authRequest.getIdToken());
               user = (User) userService.loadUserByUsername(oauthEmail);
            } else {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                authRequest.getEmail(),
                                authRequest.getPassword())
                );
                user = (User) userService.loadUserByUsername(authRequest.getEmail());
            }



            if(user != null){
                String jwt = jwtHandler.generateToken(user);
                response = new Response(
                        HttpStatus.OK,
                        "Successfully Logged in",
                        "data",
                        jwt
                );
            }

        } catch (UserAlreadyExistAuthenticationException e){
            e.printStackTrace();
            response = new Response(
                    HttpStatus.NOT_ACCEPTABLE,
                    "User already has an associated account, try to login with email and password"
            );
        } catch (AuthenticationException | IOException | GeneralSecurityException e) {
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


    public String oauthAuthenticate(String idTokenString) throws IOException, GeneralSecurityException, UserAlreadyExistAuthenticationException {
        System.out.println(" client id " + CLIENT_ID);
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(CLIENT_ID))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();

        // (Receive idTokenString by HTTPS POST)

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            // Get profile information from payload
            String email = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");

            // Use or store profile information
            // ...
            System.out.println(" name " + name);

            if(!userService.verifyIsOauthAccount(email, userId)){
                Optional<UserRole> role = Optional.of(UserRole.ROLE_GOOGLE_VISITOR);
                userService.addOauthUser(userId, email, name, role.orElse(UserRole.ROLE_EMPLOYER));
            }

            return email;
        } else {
            System.out.println("Invalid ID token.");
        }

        return null;
    }
//    @Autowired
//    private OAuth2AuthorizedClientService authorizedClientService;
//
//    @GetMapping("/loginSuccess")
//    public void getLoginInfo(OAuth2AuthenticationToken authentication) {
//        OAuth2AuthorizedClient client = authorizedClientService
//                .loadAuthorizedClient(
//                        authentication.getAuthorizedClientRegistrationId(),
//                        authentication.getName());
//        System.out.println(" name " + client.getPrincipalName());
////        return;
//    }
//
//    @GetMapping("/loginFailure")
//    public void getLoginInfo() {
//
//        System.out.println(" failed login oauth ");
//    }
}
