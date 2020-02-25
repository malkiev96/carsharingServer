package com.carsharing.model.android;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PayInfo {

    private int orderId;
    private String token;
    private float price;

}
