package com.carsharing.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class Marker {
    private int id;
    private Double lat;
    private Double lon;
    private String name;
    private String number;
    private String transmission;
    private String year;
    private String vin;
    private Boolean online;
    private Boolean enabled;//car
    private Boolean opened;
    private Boolean engineOn;
    private Boolean rented;
    private Date lastTime;
    private int speed;
    private int fuelLevel;
    private Double mileage;


}
