package com.mehdilagdimi.myrh.model.entity;

import com.mehdilagdimi.myrh.model.User;
import com.mehdilagdimi.myrh.util.UIDGenerator;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("employer")
public class Employer extends User {

    private Long identifier;

    public Employer(){
        this.identifier = UIDGenerator.getUID();
    }

}
