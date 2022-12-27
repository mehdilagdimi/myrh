package com.mehdilagdimi.myrh.model.entity;

import com.mehdilagdimi.myrh.model.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("visitor")
public class Visitor extends User {

}
