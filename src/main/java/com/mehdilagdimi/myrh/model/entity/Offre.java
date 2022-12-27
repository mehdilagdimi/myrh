package com.mehdilagdimi.myrh.model.entity;

import com.mehdilagdimi.myrh.base.enums.Education;
import com.mehdilagdimi.myrh.base.enums.OfferType;
import com.mehdilagdimi.myrh.base.enums.Profile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;


@Entity
@Getter
@Setter
public class Offre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @Basic(optional = false)
    private String title;


    @Column(nullable = false)
    @Basic(optional = false)
    private Timestamp publicationDate = Timestamp.from(Instant.now());

    @Column(nullable = false)
    @Basic(optional = false)
    private Boolean isExpired = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Basic(optional = false)
    private OfferType offreType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Basic(optional = false)
    private Profile profile;


    @Column(nullable = false)
    @Basic(optional = false)
    private String ville;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    @Basic(optional = true)
    private Education education;

    @Column(nullable = true)
    @Basic(optional = true)
    private Float salary;

}
