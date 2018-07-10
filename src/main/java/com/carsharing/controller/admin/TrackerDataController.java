package com.carsharing.controller.admin;

import com.carsharing.model.Tracker;
import com.carsharing.model.TrackerData;
import com.carsharing.service.CarService;
import com.carsharing.service.TrackerDataService;
import com.carsharing.service.TrackerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

@Controller
public class TrackerDataController {


    private final TrackerService trackerService;
    private final TrackerDataService trackerDataService;
    private final CarService carService;

    public TrackerDataController(TrackerService trackerService, TrackerDataService trackerDataService, CarService carService) {
        this.trackerService = trackerService;
        this.trackerDataService = trackerDataService;
        this.carService = carService;
    }

    @RequestMapping(value = "/tracker/data", method = RequestMethod.POST)
    @ResponseBody
    public String trackerData(HttpServletRequest request) {

        String imei = request.getParameter("imei");

        Tracker tracker = trackerService.getTrackerByImei(imei);
        if (tracker != null && tracker.getEnabled()) {

            Double lat = Double.valueOf(request.getParameter("lat"));
            Double lon = Double.valueOf(request.getParameter("lon"));
            boolean engineOn = Boolean.parseBoolean(request.getParameter("engineOn"));
            boolean opened = Boolean.parseBoolean(request.getParameter("opened"));
            Timestamp timestamp = Timestamp.valueOf(request.getParameter("timestamp"));
            int fuelLevel = Integer.parseInt(request.getParameter("fuelLevel"));

            TrackerData data = new TrackerData();
            data.setLat(lat);
            data.setLon(lon);
            data.setEngineOn(engineOn);
            data.setOpened(opened);
            data.setTimestamp(timestamp);
            data.setFuelLevel(fuelLevel);
            if (engineOn) {
                int carSpeed = Integer.parseInt(request.getParameter("carSpeed"));
                data.setSpeed(carSpeed);
            }

            data.setTracker(tracker);
            trackerDataService.save(data);


            if (!tracker.getOnline()) {
                tracker.setOnline(true);
                trackerService.save(tracker);
            }

            return "OK";

        } else return "TrackerNotFound";
    }
}
