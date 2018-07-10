package com.carsharing.service.impl;

import com.carsharing.model.*;
import com.carsharing.model.android.AndroidCar;
import com.carsharing.service.CarService;
import com.carsharing.service.MarkerService;
import com.carsharing.service.TrackerDataService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MarkerServiceImpl implements MarkerService {

    private CarService carService;
    private TrackerDataService trackerDataService;

    public MarkerServiceImpl(CarService carService, TrackerDataService trackerDataService) {
        this.carService = carService;
        this.trackerDataService = trackerDataService;
    }

    @Override
    public List<Marker> getAll() {
        List<Marker> list = new ArrayList<>();
        List<Car> cars = carService.getAllCars();
        TrackerData trackerData;

        for (Car car : cars) {
            Marker marker = new Marker();

            marker.setId(car.getId());
            marker.setName(car.getBrand()+" "+car.getModel());
            marker.setVin(car.getVin());
            marker.setNumber(car.getNumber());
            marker.setTransmission(car.getTransmission());
            marker.setYear(car.getYear());
            marker.setEnabled(car.getEnabled());
            marker.setRented(car.getRented());

            Tracker tracker = car.getTracker();
            marker.setOnline(tracker.getOnline());

            trackerData = trackerDataService.getLastDataByTracker(car.getTracker());

            if (trackerData != null) {
                marker.setLat(trackerData.getLat());
                marker.setLon(trackerData.getLon());
                marker.setSpeed(trackerData.getSpeed());
                marker.setMileage(trackerData.getMileage());
                marker.setEngineOn(trackerData.getEngineOn());
                marker.setFuelLevel(trackerData.getFuelLevel());
                marker.setOpened(trackerData.getOpened());
                marker.setLastTime(trackerData.getTimestamp());
            }

            list.add(marker);
        }
        return list;
    }

    @Override
    public List<AndroidCar> getAllForClients() {

        List<AndroidCar> list = new ArrayList<>();
        List<Car> cars = carService.getAllCars();

        for (Car car : cars) {

            Tracker tracker = car.getTracker();
            Tariff tariff = car.getTariff();

            if (car.getEnabled() && !car.getRented() && tracker.getOnline() && tariff.getEnabled()) {

                TrackerData data = trackerDataService.getLastDataByTracker(tracker);

                if (data!=null){

                    AndroidCar androidCar = new AndroidCar();

                    androidCar.setId(car.getId());
                    androidCar.setName(car.getBrand()+" "+car.getModel());
                    androidCar.setNumber(car.getNumber());
                    androidCar.setTransmission(car.getTransmission());
                    androidCar.setYear(car.getYear());
                    androidCar.setFuelLevel(data.getFuelLevel());
                    androidCar.setLat(data.getLat());
                    androidCar.setLon(data.getLon());
                    androidCar.setTariff(car.getTariff());
                    list.add(androidCar);
                }
            }
        }
        return list;
    }
}
