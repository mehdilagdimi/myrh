package com.mehdilagdimi.myrh.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class OfferDetails {
    @Id @Column(name = "offre_id")
    private Long id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "offre_id")
    @MapsId
    Offer offer;

    private String description;

    public OfferDetails(){}
    public OfferDetails(Offer offer, String description) {
        this.offer = offer;
        this.description = description;
    }
}
