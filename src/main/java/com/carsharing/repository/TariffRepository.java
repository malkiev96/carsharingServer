package com.carsharing.repository;

import com.carsharing.model.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Integer> {
    List<Tariff> getAllByEnabled(boolean enabled);
}
