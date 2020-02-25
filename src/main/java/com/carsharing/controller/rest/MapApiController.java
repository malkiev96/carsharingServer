package com.carsharing.controller.rest;

import com.carsharing.model.Zone;
import com.carsharing.model.android.AndroidCar;
import com.carsharing.repository.ZoneRepository;
import com.carsharing.service.MarkerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class MapApiController {

    private MarkerService markerService;
    private ZoneRepository zoneRepository;

    @RequestMapping(value = "/api/carList", method = RequestMethod.POST)
    public List<AndroidCar> getCars() {
        return markerService.getAllForClients();
    }

    @RequestMapping(value = "/api/zones", method = RequestMethod.GET)
    public List<Zone> getZones() {
        return zoneRepository.findAll();
    }
}
