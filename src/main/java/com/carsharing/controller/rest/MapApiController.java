package com.carsharing.controller.rest;

import com.carsharing.model.Zone;
import com.carsharing.model.android.AndroidCar;
import com.carsharing.repository.ZoneRepository;
import com.carsharing.service.MarkerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MapApiController {

    private final MarkerService markerService;
    private final ZoneRepository zoneRepository;

    public MapApiController(MarkerService markerService, ZoneRepository zoneRepository) {
        this.markerService = markerService;
        this.zoneRepository = zoneRepository;
    }

    @RequestMapping(value = "/api/carList", method = RequestMethod.POST)
    public List<AndroidCar> getCars() {
        return markerService.getAllForClients();
    }

    /*
    if (clientService.tokenAuthentication(token)){
            response.setStatus(HttpServletResponse.SC_OK);
            return markerService.getAllForClients();
        }else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
     */

    @RequestMapping(value = "/api/zones", method = RequestMethod.GET)
    public List<Zone> getZones() {
        return zoneRepository.findAll();
    }
}
