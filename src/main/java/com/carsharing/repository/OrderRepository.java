package com.carsharing.repository;

import com.carsharing.model.Car;
import com.carsharing.model.Client;
import com.carsharing.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findAllByEnded(boolean ended);
    List<Order> findAllByCar(Car car);
    List<Order> findAllByClient(Client client);
    Order findByClientAndEnded(Client client, boolean ended);
    List<Order> findAllByPaymentIsNullAndEnded(boolean ended);
    List<Order> findAllByPaymentIsNotNull();
    Order findByPaymentIsNullAndClientAndEndedIsTrue(Client client);
}
