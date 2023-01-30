package com.mehdilagdimi.myrh.service;

import com.mehdilagdimi.myrh.base.enums.UserRole;
import com.mehdilagdimi.myrh.base.exception.UserAlreadyExistAuthenticationException;
import com.mehdilagdimi.myrh.model.SignupRequest;
import com.mehdilagdimi.myrh.model.entity.*;
import com.mehdilagdimi.myrh.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserService implements UserDetailsService{

    @Autowired
    UserRepository userRepository;
    @Autowired
    OauthUserRepository oauthUserRepository;
    @Autowired
    EmployerRepository employerRepository;
    @Autowired
    AgentRepository agentRepository;
    @Autowired
    VisitorRepository visitorRepository;


    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found......"));
    }

    public User addUser(SignupRequest signupReq) throws UserAlreadyExistAuthenticationException {
        if(userRepository.findByEmail(signupReq.getEmail()).isPresent())
            throw new UserAlreadyExistAuthenticationException("User already exist");

        //creating user object and setting its authorities
        User user = new User(
                signupReq.getEmail(), signupReq.getUsername(), signupReq.getAddress(), signupReq.getTele(), signupReq.getRole(),
                passwordEncoder.encode(signupReq.getPassword())
        );

        switch (user.getRole().toString()){
            case "ROLE_EMPLOYER":
                employerRepository.save(new Employer(user));
                break;
            case "ROLE_VISITOR":
                visitorRepository.save(new Visitor(user));
                break;
        }
        return user;
    }


    public User getUser(Long id){
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
    }
    public Employer getEmployer(Long id){
        return employerRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
    }
}
