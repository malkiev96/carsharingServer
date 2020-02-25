package com.carsharing.repository;

import com.carsharing.model.Order;
import com.carsharing.model.OrderData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDataRepository extends JpaRepository<OrderData, Integer> {

    List<OrderData> findAllByOrder(Order order);
    OrderData findOrderDataByOrderAndEnded(Order order, boolean ended);
}
