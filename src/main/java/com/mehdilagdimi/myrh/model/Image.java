package com.mehdilagdimi.myrh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class Image {

    @Id
    @GeneratedValue
    Long id;

    @Lob
    byte[] content;

    String name;


}