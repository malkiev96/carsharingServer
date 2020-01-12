package com.carsharing.model.android;

import com.carsharing.model.Client;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AndroidOrder {
    private int currentAction;
    private float price;
    private String time;
    private Client client;
    private AndroidCar androidCar;
}
