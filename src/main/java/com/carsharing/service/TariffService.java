package com.carsharing.service;

import com.carsharing.model.Tariff;

import java.util.List;

public interface TariffService {

    void save(Tariff tariff);
    void delete(Tariff tariff);
    Tariff getById(int id);
    List<Tariff> getAllTariffs();
    List<Tariff> getAllByEnabled(boolean enabled);

}
