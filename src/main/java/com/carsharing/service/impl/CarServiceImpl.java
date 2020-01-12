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
import com.carsharing.service.polygon.CDeterminant;
import com.carsharing.service.polygon.CPoints;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    private CarRepository carRepository;
    private TrackerService trackerService;
    private TrackerDataService trackerDataService;

    public CarServiceImpl(CarRepository carRepository, TrackerService trackerService, TrackerDataService trackerDataService) {
        this.carRepository = carRepository;
        this.trackerService = trackerService;
        this.trackerDataService = trackerDataService;
    }

    public Car getCarById(int id) {
        return carRepository.getOne(id);
    }

    public AndroidCar getAndroidCarById(int id) {
        Car car = getCarById(id);
        AndroidCar androidCar = new AndroidCar();
        androidCar.setId(car.getId());
        androidCar.setTransmission(car.getTransmission());
        androidCar.setNumber(car.getNumber());
        androidCar.setName(car.getBrand() + " " + car.getModel());
        androidCar.setTariff(car.getTariff());
        androidCar.setYear(car.getYear());
        TrackerData data = getActualData(car);
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

    public List<Car> getAllByOnline(boolean online) {
        return carRepository.getAllByOnline(online);
    }

    @Override
    public TrackerData getActualData(Car car) {
        TrackerData data = trackerDataService.getLastDataByTracker(car.getTracker());
        return data;
    }

    public List<Car> getAllByOpened(boolean opened) {
        List<Tracker> trackers = trackerService.getNotEmptyTrackers();
        List<Car> cars = new ArrayList<>();
        for (Tracker tracker : trackers) {
            TrackerData trackerData = trackerDataService.getLastDataByTracker(tracker);
            if (trackerData != null) {
                if (trackerData.getOpened().equals(opened))
                    cars.add(tracker.getCar());
            }
        }
        return cars;
    }

    @Override
    public boolean openCar(Car car) {
        if (car != null) {
            Tracker tracker = car.getTracker();

            if (tracker.getAction() == 0) {
                tracker.setAction(2);
                trackerService.save(tracker);
                TrackerData data = trackerDataService.getLastDataByTracker(tracker);
                data.setOpened(true);
                trackerDataService.save(data);
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean closeCar(Car car) {
        if (car != null) {
            Tracker tracker = car.getTracker();
            if (tracker.getAction() == 0) {
                tracker.setAction(1);
                trackerService.save(tracker);
                TrackerData data = trackerDataService.getLastDataByTracker(tracker);
                data.setOpened(false);
                trackerDataService.save(data);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean testCarOnZone(Car car, Zone zone) {

        TrackerData data = getActualData(car);
        if (data != null && zone != null) {

            JSONArray array = new JSONArray(zone.getPolygon());

            CPoints points = new CPoints(array.length());
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                double lat = object.getDouble("lat");
                double lon = object.getDouble("lng");
                points.push(lat, lon);
            }
            return CDeterminant.determine(points, data.getLat(), data.getLon());
        }

        return false;
    }

    public List<Car> getAllByRented(boolean rented) {
        return carRepository.getAllByRented(rented);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public List<Car> getAllByEnabled(boolean enabled) {
        return carRepository.getAllByEnabled(enabled);
    }

    public void saveCar(Car car) {
        carRepository.save(car);
    }

    public void deleteCar(Car car) {
        carRepository.delete(car);
    }
}
