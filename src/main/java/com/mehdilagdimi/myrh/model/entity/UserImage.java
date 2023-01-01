package com.mehdilagdimi.myrh.model.entity;

import com.mehdilagdimi.myrh.model.Image;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

//@Entity
//@DiscriminatorValue(value = "profile")
@Getter
@Setter
public class UserImage extends Image {
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @MapsId
    User user;
}