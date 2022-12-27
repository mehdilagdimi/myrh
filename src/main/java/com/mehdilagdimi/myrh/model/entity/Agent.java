package com.mehdilagdimi.myrh.model.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@DiscriminatorValue("1")
public class Agent extends User {

    @OneToMany(mappedBy = "agent", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    List<Offre> offreList;
}
