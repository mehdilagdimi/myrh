package com.mehdilagdimi.myrh.controller;



import com.mehdilagdimi.myrh.base.exception.UserAlreadyExistAuthenticationException;
import com.mehdilagdimi.myrh.model.AuthenticationRequest;
import com.mehdilagdimi.myrh.model.SignupRequest;
import com.mehdilagdimi.myrh.model.entity.User;
import com.mehdilagdimi.myrh.service.UserService;
import com.mehdilagdimi.myrh.util.JwtHandler;
import org.springframework.context.annotation.Lazy;
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
    public ResponseEntity<String> authenticate(
            @RequestBody AuthenticationRequest authRequest
    ) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword())
            );

            final User user = (User) userService.loadUserByUsername(authRequest.getEmail());

            if(user != null){
                return ResponseEntity.ok(jwtHandler.generateToken(user));
            }

        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(400).body("Error authenticating user");
    }


    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @RequestBody SignupRequest signupRequest
    ){
        String repsonseBody = "";
        try{
            User user = userService.addUser(signupRequest);
            repsonseBody = jwtHandler.generateToken(user);
        } catch (UserAlreadyExistAuthenticationException e){
            e.printStackTrace();
            repsonseBody = "Error authenticating user";
        } finally {
            return ResponseEntity.status(400).body(repsonseBody);
        }

    }
}
