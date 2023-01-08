//package com.mehdilagdimi.myrh.service;
//
//
//import com.mehdilagdimi.myrh.model.entity.User;
//import com.mehdilagdimi.myrh.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.social.connect.Connection;
//import org.springframework.social.connect.ConnectionSignUp;
//import org.springframework.stereotype.Service;
//
//import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
//
//@Service
//public class FacebookConnectionSignup implements ConnectionSignUp {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public String execute(Connection<?> connection) {
//        User user = new User();
//        user.setUsername(connection.getDisplayName());
//        user.setPassword(randomAlphabetic(8));
//        userRepository.save(user);
//        return user.getUsername();
//    }
//}