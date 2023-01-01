package com.mehdilagdimi.myrh.model.entity;

import com.mehdilagdimi.myrh.model.Image;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ProfileImage extends Image {

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @MapsId
    User user;
}