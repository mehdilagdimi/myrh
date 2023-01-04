package com.mehdilagdimi.myrh.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("3")
public class Visitor extends User {

    public Visitor(){

    }
    public Visitor(User parent) {
        super(parent.getEmail(), parent.getUsername(), parent.getAdress(), parent.getTele(), parent.getRole(), parent.getPassword());
    }
}
