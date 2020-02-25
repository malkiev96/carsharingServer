package com.carsharing.service;

import com.carsharing.model.Order;
import com.carsharing.model.OrderData;

import java.util.List;

public interface OrderDataService {
    void save(OrderData orderData);
    List<OrderData> getAllByOrder(Order order);
    OrderData getByOrderAndEnded(Order order, boolean ended);
}
