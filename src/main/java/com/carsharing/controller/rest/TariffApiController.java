package com.carsharing.controller.rest;

import com.carsharing.model.Tariff;
import com.carsharing.service.TariffService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class TariffApiController {

    private TariffService tariffService;

    @GetMapping("api/tariff/getAll")
    public List<Tariff> getTariffs() {
        return tariffService.getAllByEnabled(true);
    }

    @GetMapping("api/tariff/{id}")
    public Tariff getTariff(@PathVariable("id") int id) {
        return tariffService.getById(id);
    }

}
