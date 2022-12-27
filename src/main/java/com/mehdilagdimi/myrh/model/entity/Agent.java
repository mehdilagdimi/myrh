package com.mehdilagdimi.myrh.model.entity;

import com.mehdilagdimi.myrh.model.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("agent")
public class Agent extends User {

}
