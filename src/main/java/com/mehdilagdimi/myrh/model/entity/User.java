package com.mehdilagdimi.myrh.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mehdilagdimi.myrh.base.enums.UserRole;
import com.mehdilagdimi.myrh.model.Image;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="user_role",
        discriminatorType = DiscriminatorType.STRING)
public class User implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String username;
    private String adress;
    private String tele;
    private UserRole role;

    @JsonIgnore
    private String password;

    private Timestamp createdAt = Timestamp.from(Instant.now());
    @JsonIgnore
    private boolean isEnabled = true;
    @JsonIgnore
    private boolean isAccountExpired = false;
    @JsonIgnore
    private boolean isCredentialsExpired = false;
    @JsonIgnore
    private boolean isLocked = false;

//    private String photoProfile = null;
    @JsonIgnore
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    ProfileImage image;

    @OneToOne(mappedBy = "user")
    @PrimaryKeyJoinColumn
    private OauthUser oauthUser;


    @JsonIgnore
    @Transient
    private List<SimpleGrantedAuthority> grantedAuthorityList;

    public User(){
    }

    public User(String email, String username, String adress, String tele, UserRole role, String password) {
        this.email = email;
        this.username = username;
        this.adress = adress;
        this.tele = tele;
        this.role = role;
        this.password = password;
        this.getAuthorities();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(role != null){
            grantedAuthorityList = new ArrayList<>();
            for(String role : role.toString().split(",")){
                this.grantedAuthorityList.add(new SimpleGrantedAuthority(role));
            }
        }
        return grantedAuthorityList;
    }

    public void setGrantedAuthorityList(List<SimpleGrantedAuthority> grantedAuthorityList) {
        this.grantedAuthorityList = grantedAuthorityList;
    }
//    public void setGrantedAuthorities() {
//        if(role != null){
//            grantedAuthorityList = new ArrayList<>();
//            for(String role : role.toString().split(",")){
//                this.grantedAuthorityList.add(new SimpleGrantedAuthority(role));
//            }
//        }
//    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !isAccountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !isCredentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getTele() {
        return tele;
    }

    public void setTele(String tele) {
        this.tele = tele;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public ProfileImage getImage() {
        return image;
    }

    public void setImage(ProfileImage image) {
        this.image = image;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public OauthUser getOauthUser() {
        return oauthUser;
    }

    public void setOauthUser(OauthUser oauthUser) {
        this.oauthUser = oauthUser;
    }
}
