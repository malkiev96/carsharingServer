package com.carsharing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "car", schema = "public")
@Getter
@Setter
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "number")
    @NotEmpty
    private String number;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "comment")
    private String comment;

    @Column(name = "rented")
    private Boolean rented;

    @Column(name = "vin")
    @NotEmpty
    private String vin;

    @Column(name = "year")
    @NotEmpty
    private String year;

    @Column(name = "brand")
    @NotEmpty
    private String brand;

    @Column(name = "model")
    @NotEmpty
    private String model;

    @OneToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @NotNull
    @JoinColumn(name = "id_tracker")
    private Tracker tracker;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tariff")
    @NotNull
    private Tariff tariff;

    @Override
    public String toString() {
        return brand + " " + model;
    }
}
