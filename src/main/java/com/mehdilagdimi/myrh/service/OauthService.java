package com.mehdilagdimi.myrh.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.mehdilagdimi.myrh.base.enums.OauthProvider;
import com.mehdilagdimi.myrh.base.enums.UserRole;
import com.mehdilagdimi.myrh.base.exception.UserAlreadyExistAuthenticationException;
import com.mehdilagdimi.myrh.model.entity.OauthUser;
import com.mehdilagdimi.myrh.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;


@Service
public class OauthService{

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OauthUserRepository oauthUserRepository;

    private String CLIENT_ID = "81862977324-aehpi5f820aevtjmcdscj657eqiameos.apps.googleusercontent.com";



    public OauthUser addOauthUser(String userId, OauthProvider provider, String email, String name, UserRole role) throws UserAlreadyExistAuthenticationException {
        return oauthUserRepository.save(
                new OauthUser(userId, provider, email, name,role)
        );
    }

    public boolean verifyIsOauthAccount(String email, String userId) throws UserAlreadyExistAuthenticationException{
        if(oauthUserRepository.findByOauthUserId(userId).isPresent()) return true;
        if(userRepository.findByEmail(email).isPresent())
            throw new UserAlreadyExistAuthenticationException("User already exist");
        return false;
    }


    public String googleOauth(String idTokenString, UserRole role) throws GeneralSecurityException, IOException {
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
//            String pictureUrl = (String) payload.get("picture");
//            String locale = (String) payload.get("locale");
//            String familyName = (String) payload.get("family_name");
//            String givenName = (String) payload.get("given_name");

            System.out.println(" name " + name);

            if(!verifyIsOauthAccount(email, userId)){
//                Optional<UserRole> role = Optional.of(UserRole.ROLE_GOOGLE_VISITOR);
                addOauthUser(userId, OauthProvider.GOOGLE, email, name, role);
            }

            return email;

        } else {
            System.out.println("Invalid ID token.");
        }

        return null;
    }


    public String facebookOauth(String accessToken, UserRole role){
        Facebook facebook = new FacebookTemplate(accessToken, "myhr-app");
//        Facebook facebook = new FacebookTemplate(accessToken);
        User profile = facebook.userOperations().getUserProfile();

        System.out.println(" facebook user ");
        System.out.println(profile);

//        if(!verifyIsOauthAccount(profile.getEmail(), profile.getId())){
//            addOauthUser(profile.getId(), OauthProvider.FACEBOOK, profile.getEmail(), profile.getName(), role);
//        }
        return profile.getEmail();
    }
}
