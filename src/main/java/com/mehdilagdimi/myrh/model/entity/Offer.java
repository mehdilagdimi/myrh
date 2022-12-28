package com.mehdilagdimi.myrh.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mehdilagdimi.myrh.base.enums.Education;
import com.mehdilagdimi.myrh.base.enums.OfferType;
import com.mehdilagdimi.myrh.base.enums.Profile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;


@Entity
@Getter
@Setter
public class Offer implements Serializable {
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


    @JsonIgnore
    @OneToOne(mappedBy = "offer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @PrimaryKeyJoinColumn
    OfferDetails offerDetails;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "employer_id", referencedColumnName = "id")
    Employer employer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    Agent agent;

    public Offer(){
    }

    public Offer(String title, OfferType offreType, Profile profile, String ville, Education education, Float salary) {
        this.title = title;
        this.offreType = offreType;
        this.profile = profile;
        this.ville = ville;
        this.education = education;
        this.salary = salary;
    }
    public Offer(Employer employer, String title, OfferType offreType, Profile profile, String ville, Education education, Float salary) {
        this.employer = employer;
        this.title = title;
        this.offreType = offreType;
        this.profile = profile;
        this.ville = ville;
        this.education = education;
        this.salary = salary;
    }


    //    @JsonManagedReference
//    public Offer getOffer(){
//        return this;
//    }
}
