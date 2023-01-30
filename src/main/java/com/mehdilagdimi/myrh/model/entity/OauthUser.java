package com.mehdilagdimi.myrh.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mehdilagdimi.myrh.base.enums.OauthProvider;
import com.mehdilagdimi.myrh.base.enums.UserRole;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Entity
public class OauthUser {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String oauthUserId;
    private OauthProvider provider;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @MapsId
    private User user;

//    private String photoProfile = null;

    public OauthUser(){
    }

    public OauthUser(String oauthUserId, OauthProvider provider, String email, String username, UserRole role) {
        this.oauthUserId = oauthUserId;
        this.provider = provider;
        if(user == null) user = new User();
        this.user.setEmail(email);
        this.user.setUsername(username);
        this.user.setRole(role);
    }

    public Collection<? extends GrantedAuthority> getAuthorities(){
        return this.user.getAuthorities();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOauthUserId() {
        return oauthUserId;
    }

    public void setOauthUserId(String oauthUserId) {
        this.oauthUserId = oauthUserId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
