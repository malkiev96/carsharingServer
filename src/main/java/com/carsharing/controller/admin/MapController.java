package com.carsharing.controller.admin;

import com.carsharing.model.Marker;
import com.carsharing.model.MarkerList;
import com.carsharing.model.Zone;
import com.carsharing.repository.ZoneRepository;
import com.carsharing.service.CarService;
import com.carsharing.service.ClientService;
import com.carsharing.service.MarkerService;
import com.carsharing.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;

@Controller
@AllArgsConstructor
public class MapController {

    private MarkerService markerService;
    private CarService carService;
    private ClientService clientService;
    private ZoneRepository zoneRepository;
    private OrderService orderService;

    @ModelAttribute
    public void carCount(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("carOffline", carService.getAllByOnline(false).size());
        model.addAttribute("clientNew", clientService.getAllByActivatedAndEnabled(false, true).size());
        model.addAttribute("orderNotPaid", orderService.getAllNotPaid().size());
    }

    @RequestMapping(value = "/admin/map", method = RequestMethod.GET)
    public String map() {
        return "admin/map/index";
    }

    @RequestMapping(value = "/admin/zones", method = RequestMethod.GET)
    public String zone() {
        return "admin/map/zones";
    }


    @RequestMapping(value = "/admin/map/api/marker", method = RequestMethod.GET)
    @ResponseBody
    public MarkerList getMarkers() {
        List<Marker> markers = markerService.getAll();
        MarkerList markerList = new MarkerList();
        markerList.setMarkerList(markers);
        return markerList;
    }

    @RequestMapping(value = "/admin/map/api/zones", method = RequestMethod.GET)
    @ResponseBody
    public List<Zone> getZones() {
        return zoneRepository.findAll();
    }

    @RequestMapping(value = "/admin/map/api/deleteZone", method = RequestMethod.POST)
    @ResponseBody
    public String deleteZone(@RequestParam("id") int id,
                             HttpServletResponse response) {
        try {
            zoneRepository.deleteById(id);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return "";
    }

    @RequestMapping(value = "/admin/map/api/savePolygon", method = RequestMethod.POST)
    @ResponseBody
    public String savePolygon(
            @RequestParam("id") int id,
            @RequestParam("polygon") String polygon,
            @RequestParam("type") int type,
            HttpServletResponse response) {

        Zone zone = id == 0 ? new Zone() : zoneRepository.getOne(id);
        zone.setPolygon(polygon);
        zone.setType(type);

        zoneRepository.save(zone);
        response.setStatus(HttpServletResponse.SC_OK);
        return "";
    }
}
