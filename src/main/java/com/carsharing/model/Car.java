package com.carsharing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "car", schema = "public")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "car_seq_gen")
    @SequenceGenerator(name = "car_seq_gen", sequenceName = "car_id_seq")
    @Column(name = "id")
    private int id;

    @Column(name = "number")
    private String number;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "comment")
    private String comment;

    @Column(name = "rented")
    private Boolean rented;

    @Column(name = "transmission")
    private String transmission;

    @Column(name = "vin")
    private String vin;

    @Column(name = "year")
    private String year;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @OneToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "id_tracker", nullable = false)
    private Tracker tracker;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tariff", nullable = false)
    private Tariff tariff;

    @Override
    public String toString() {
        return brand + " " + model;
    }
}
