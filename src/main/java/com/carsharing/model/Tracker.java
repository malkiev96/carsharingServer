package com.carsharing.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tracker", schema = "public")
@Getter
@Setter
public class Tracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phonefirst", nullable = false)
    private String phoneFirst;

    @Column(name = "phonesecond")
    private String phoneSecond;

    @Column(name = "enabled", columnDefinition = "boolean default true")
    private Boolean enabled;

    @Column(name = "online", columnDefinition = "boolean default false")
    private Boolean online;

    @Column(name = "imei", nullable = false)
    private String imei;

    @Column(name = "protocol", nullable = false)
    private String protocol;

    @Column(name = "identify_number", nullable = false)
    private String identifyNumber;

    @Column(name = "action", columnDefinition = "default 0")
    private int action;

    @OneToOne(mappedBy = "tracker", fetch = FetchType.LAZY)
    private Car car;

}
