package com.carsharing.model.android;

import com.carsharing.model.Tariff;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AndroidCar {
    private int id;
    private Double lat;
    private Double lon;
    private String name;
    private String number;
    private String transmission;
    private String year;
    private int fuelLevel;
    private Tariff tariff;
}
