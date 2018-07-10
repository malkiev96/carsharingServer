package com.carsharing.model.android;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderPay {

    private int orderId;
    private int carId;
    private int clientId;
    private String carName;
    private String carNumber;
    private String time;
    private float price;

}
