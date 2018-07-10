package com.carsharing.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "tracker_data", schema = "public")
@Getter
@Setter
public class TrackerData {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tracker_data_seq_gen")
    @SequenceGenerator(name = "tracker_data_seq_gen", sequenceName = "tracker_data_id_seq")
    @Column(name = "id")
    private int id;

    @Column(name = "latitude")
    private Double lat;

    @Column(name = "longitude")
    private Double lon;

    @Column(name = "fuel_level")
    private int fuelLevel;

    @Column(name = "speed")
    private int speed;

    @Column(name = "mileage")
    private Double mileage;

    @Column(name = "engine_on",columnDefinition = "boolean default false")
    private Boolean engineOn;

    @Column(name = "opened",columnDefinition = "boolean default false")
    private Boolean opened;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tracker", nullable = false)
    private Tracker tracker;
}
