package com.mehdilagdimi.myrh.model.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@DiscriminatorValue("1")
public class Agent extends User {

    @OneToMany(mappedBy = "agent", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    List<Offer> offerList;

    public Agent(){
    }

    public Agent(User parent) {
        super(parent.getEmail(), parent.getUsername(), parent.getAdress(), parent.getTele(), parent.getRole(), parent.getPassword());
    }
}
