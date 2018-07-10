package com.carsharing.service.impl;

import com.carsharing.model.Tariff;
import com.carsharing.repository.TariffRepository;
import com.carsharing.service.TariffService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TariffServiceImpl implements TariffService {

    private final TariffRepository tariffRepository;

    public TariffServiceImpl(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    public void save(Tariff tariff) {
        tariffRepository.save(tariff);
    }

    public void delete(Tariff tariff) {
        tariffRepository.delete(tariff);
    }

    public Tariff getById(int id) {
        return tariffRepository.getOne(id);
    }

    public List<Tariff> getAllTariffs() {
        return tariffRepository.findAll();
    }

    @Override
    public List<Tariff> getAllByEnabled(boolean enabled) {
        return tariffRepository.getAllByEnabled(enabled);
    }
}
