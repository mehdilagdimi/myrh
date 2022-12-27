package com.mehdilagdimi.myrh.model.entity;

import com.mehdilagdimi.myrh.base.enums.UserRole;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
    private String password;
    private boolean isEnabled = true;
    private boolean isAccountExpired = false;
    private boolean isCredentialsExpired = false;
    private boolean isLocked = false;

    private String photoProfile = null;

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
        this.setGrantedAuthorities();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public void setGrantedAuthorityList(List<SimpleGrantedAuthority> grantedAuthorityList) {
        this.grantedAuthorityList = grantedAuthorityList;
    }
    public void setGrantedAuthorities() {
        if(role != null){
            grantedAuthorityList = new ArrayList<>();
            for(String role : role.toString().split(",")){
                this.grantedAuthorityList.add(new SimpleGrantedAuthority(role));
            }
        }
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
}
