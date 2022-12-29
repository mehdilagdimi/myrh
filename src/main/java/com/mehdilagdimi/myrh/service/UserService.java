package com.mehdilagdimi.myrh.service;

import com.mehdilagdimi.myrh.base.exception.UserAlreadyExistAuthenticationException;
import com.mehdilagdimi.myrh.model.SignupRequest;
import com.mehdilagdimi.myrh.model.entity.Agent;
import com.mehdilagdimi.myrh.model.entity.Employer;
import com.mehdilagdimi.myrh.model.entity.User;
import com.mehdilagdimi.myrh.repository.AgentRepository;
import com.mehdilagdimi.myrh.repository.EmployerRepository;
import com.mehdilagdimi.myrh.repository.UserRepository;
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
    EmployerRepository employerRepository;
    @Autowired
    AgentRepository agentRepository;


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
            case "ROLE_AGENT":
                agentRepository.save(new Agent(user));
                break;
        }
        return user;
    }


    public Employer getEmployer(Long id){
        return employerRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
    }
}
