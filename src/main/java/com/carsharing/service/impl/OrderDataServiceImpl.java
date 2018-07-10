package com.carsharing.service.impl;

import com.carsharing.model.Order;
import com.carsharing.model.OrderData;
import com.carsharing.repository.OrderDataRepository;
import com.carsharing.service.OrderDataService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDataServiceImpl implements OrderDataService {

    private OrderDataRepository orderDataRepository;

    public OrderDataServiceImpl(OrderDataRepository orderDataRepository) {
        this.orderDataRepository = orderDataRepository;
    }

    public void save(OrderData orderData) {
        orderDataRepository.save(orderData);
    }

    public List<OrderData> getAllByOrder(Order order) {
        return orderDataRepository.findAllByOrder(order);
    }

    @Override
    public OrderData getByOrderAndEnded(Order order, boolean ended) {
        return orderDataRepository.findOrderDataByOrderAndEnded(order, ended);
    }

}
