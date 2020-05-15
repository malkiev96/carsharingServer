package com.carsharing.service.impl;

import com.carsharing.model.Car;
import com.carsharing.model.Tracker;
import com.carsharing.model.TrackerData;
import com.carsharing.model.Zone;
import com.carsharing.model.android.AndroidCar;
import com.carsharing.repository.CarRepository;
import com.carsharing.service.CarService;
import com.carsharing.service.TrackerDataService;
import com.carsharing.service.TrackerService;
import com.carsharing.service.polygon.Determinant;
import com.carsharing.service.polygon.Points;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final TrackerService trackerService;
    private final TrackerDataService trackerDataService;

    public Car getCarById(int id) {
        return carRepository.getOne(id);
    }

    public AndroidCar getAndroidCarById(int id) {
        Car car = getCarById(id);
        AndroidCar androidCar = new AndroidCar();
        androidCar.setId(car.getId());
        androidCar.setNumber(car.getNumber());
        androidCar.setName(car.getBrand() + " " + car.getModel());
        androidCar.setTariff(car.getTariff());
        androidCar.setYear(car.getYear());
        TrackerData data = getCurrentState(car);
        if (data != null) {
            androidCar.setFuelLevel(data.getFuelLevel());
            androidCar.setLat(data.getLat());
            androidCar.setLon(data.getLon());
        }
        return androidCar;
    }

    public Car getCarByNumber(String number) {
        return carRepository.getCarByNumber(number);
    }

    public List<Car> getOnlineCars(boolean online) {
        return carRepository.getAllByOnline(online);
    }

    @Override
    public TrackerData getCurrentState(Car car) {
        return trackerDataService.getLastDataByTracker(car.getTracker());
    }

    public List<Car> getOpenedCars(boolean opened) {
        List<Tracker> trackers = trackerService.getNotEmptyTrackers();
        List<Car> cars = new ArrayList<>();
        for (Tracker tracker : trackers) {
            TrackerData trackerData = trackerDataService.getLastDataByTracker(tracker);
            if (trackerData != null) {
                if (trackerData.getOpened().equals(opened)) {
                    cars.add(tracker.getCar());
                }
            }
        }
        return cars;
    }

    @Override
    public boolean openCar(@NotNull Car car) {
        Tracker tracker = car.getTracker();

        if (tracker.getAction() == 0) {
            tracker.setAction(2);
            trackerService.save(tracker);
            TrackerData data = trackerDataService.getLastDataByTracker(tracker);
            data.setOpened(true);
            trackerDataService.save(data);
            return true;
        }

        return false;
    }

    @Override
    public boolean closeCar(@NotNull Car car) {
        Tracker tracker = car.getTracker();
        if (tracker.getAction() == 0) {
            tracker.setAction(1);
            trackerService.save(tracker);
            TrackerData data = trackerDataService.getLastDataByTracker(tracker);
            data.setOpened(false);
            trackerDataService.save(data);
            return true;
        }
        return false;
    }

    @Override
    public boolean testCarOnZone(Car car, Zone zone) {

        TrackerData data = getCurrentState(car);
        if (data != null && zone != null) {

            JSONArray array = new JSONArray(zone.getPolygon());

            Points points = new Points(array.length());
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                double lat = object.getDouble("lat");
                double lon = object.getDouble("lng");
                points.push(lat, lon);
            }
            return Determinant.determine(points, data.getLat(), data.getLon());
        }

        return false;
    }

    public List<Car> getRentedCars(boolean rented) {
        return carRepository.getAllByRented(rented);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public List<Car> getEnabledCars(boolean enabled) {
        return carRepository.getAllByEnabled(enabled);
    }

    public void save(Car car) {
        carRepository.save(car);
    }

    public void delete(Car car) {
        carRepository.delete(car);
    }
}
