package com.mehdilagdimi.myrh.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("2")
@Data
public class Employer extends User {

    private Long identifier;


    @JsonIgnore
    @OneToMany(mappedBy = "employer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<Offer> offerList;


    public Employer(){
    }

    public Employer(Long identifier, List<Offer> offerList) {
        this.identifier = identifier;
        this.offerList = offerList;
    }

    public Employer(User parent) {
        super(parent.getEmail(), parent.getUsername(), parent.getAdress(), parent.getTele(), parent.getRole(), parent.getPassword());
    }

    public Employer(User parent, Long identifier) {
        super(parent.getEmail(), parent.getUsername(), parent.getAdress(), parent.getTele(), parent.getRole(), parent.getPassword());
        this.identifier = identifier;
    }

    public void addOffer(Offer offre){
        if(this.offerList == null) offerList = new ArrayList<>();
        this.offerList.add(offre);
        offre.setEmployer(this);
    }
}
