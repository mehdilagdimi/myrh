package com.mehdilagdimi.myrh.model.entity;

import com.mehdilagdimi.myrh.util.UIDGenerator;
import jakarta.persistence.*;

import java.util.List;

@Entity
@DiscriminatorValue("2")
public class Employer extends User {

    private Long identifier;

    @OneToMany(mappedBy = "employer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<Offre> offreList;


    public Employer(){
        this.identifier = UIDGenerator.getUID();
    }

}
