package com.mehdilagdimi.myrh.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("3")
public class Visitor extends User {

}
