package com.carsharing.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tracker", schema = "public")

public class Tracker {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tracker_seq_gen")
    @SequenceGenerator(name = "tracker_seq_gen", sequenceName = "tracker_id_seq")
    @Column(name = "id")
    @Getter
    private int id;

    @Column(name = "name", nullable = false)@Getter
    @Setter
    private String name;

    @Column(name = "phonefirst", nullable = false)@Getter
    @Setter
    private String phoneFirst;

    @Column(name = "phonesecond")@Getter
    @Setter
    private String phoneSecond;

    @Column(name = "enabled",columnDefinition = "boolean default true")@Getter
    @Setter
    private Boolean enabled;

    @Column(name = "online",columnDefinition = "boolean default false")@Getter
    @Setter
    private Boolean online;

    @Column(name = "imei", nullable = false)@Getter
    @Setter
    private String imei;

    @Column(name = "protocol",nullable = false)@Getter
    @Setter
    private String protocol;

    @Column(name = "identify_number",nullable = false)@Getter
    @Setter
    private String identifyNumber;

    @Column(name = "action",columnDefinition = "default 0")@Getter
    @Setter
    private int action;

    @OneToOne(mappedBy = "tracker",fetch = FetchType.LAZY)@Getter
    @Setter
    private Car car;

}
