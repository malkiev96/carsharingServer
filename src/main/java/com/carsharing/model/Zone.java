package com.carsharing.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "zone", schema = "public")
@Getter
@Setter
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "polygon", nullable = false)
    private String polygon;

    @Column(name = "type", nullable = false)
    private int type;


}
