package com.carsharing.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Time;
import java.util.List;

@Entity
@Table(name = "tariff", schema = "public")
@Getter
@Setter
public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tariff_seq_gen")
    @SequenceGenerator(name = "tariff_seq_gen", sequenceName = "tariff_id_seq")
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "pay_rent", nullable = false)
    private Float payRent;

    @Column(name = "pay_waiting", nullable = false)
    private Float payWaiting;

    @Column(name = "pay_booking", nullable = false)
    private Float payBooking;

    @Column(name = "free_booking_min", nullable = false)
    private int freeBookingMin;

    @Column(name = "enabled",columnDefinition = "boolean default true")
    private Boolean enabled;

    @Column(name = "pay_crash", nullable = false)
    private Float payCrash;

    @Column(name = "free_waiting_start")
    private Time freeWaitingStart;

    @Column(name = "free_waiting_end")
    private Time freeWaitingEnd;

}
