package com.mehdilagdimi.myrh.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mehdilagdimi.myrh.base.enums.Education;
import com.mehdilagdimi.myrh.base.enums.OfferStatus;
import com.mehdilagdimi.myrh.base.enums.OfferType;
import com.mehdilagdimi.myrh.base.enums.Profile;

import lombok.*;


@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OfferRequest {

    private final Long id;
    private final String title;
    private final OfferType offerType;
    private final OfferStatus offerStatus;
    private final Profile profile;
    private final String ville;
    private final Education education;
    private final String description;

    @JsonProperty(required = false)
    private final Float salary;



}