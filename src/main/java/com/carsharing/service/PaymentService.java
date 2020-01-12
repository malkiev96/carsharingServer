package com.carsharing.service;

import com.carsharing.model.Payment;

public interface PaymentService {
    void save(Payment payment);

    void delete(Payment payment);

    Payment getById(int id);
}
