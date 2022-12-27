package com.mehdilagdimi.myrh.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class OffreDetails {
    @Id @Column(name = "offre_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "offre_id")
    @MapsId
    Offre offre;

    private String description;
}
