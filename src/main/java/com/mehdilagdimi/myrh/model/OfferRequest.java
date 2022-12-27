package com.mehdilagdimi.myrh.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mehdilagdimi.myrh.base.enums.Education;
import com.mehdilagdimi.myrh.base.enums.OfferType;
import com.mehdilagdimi.myrh.base.enums.Profile;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OfferRequest {

    private final String title;
    private final OfferType offerType;
    private final Profile profile;
    private final String ville;
    private final Education education;
    private final String description;

    @JsonProperty(required = false)
    private final Float salary;

}