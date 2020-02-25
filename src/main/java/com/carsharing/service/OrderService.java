package com.carsharing.service;

import com.carsharing.model.Car;
import com.carsharing.model.Client;
import com.carsharing.model.Order;
import com.carsharing.model.android.AndroidOrder;
import com.carsharing.model.android.OrderPay;

import java.util.List;

public interface OrderService {

    void saveOrder(Order order);
    Order getOrderById(int id);
    List<Order> getAllOrders();
    List<Order> getAllByEnded(boolean ended);
    List<Order> getAllByCar(Car car);
    List<Order> getAllByClient(Client client);
    boolean testBooking(int clientId, String carNumber);
    boolean testBooking(int clientId, int carId);
    AndroidOrder startAction(int clientId, int act);
    AndroidOrder finishOrder(int clientId);
    AndroidOrder stopBooking(int clientId);
    AndroidOrder getActual(int clientId);
    Order getCurrentOrderByClientID(int clientId);
    AndroidOrder startBooking(int clientId, int carId);
    List<Order> getAllPaid();
    List<Order> getAllNotPaid();
    OrderPay clientGetPay(int clientId);

}
