package com.carsharing.service;

import com.carsharing.model.Car;
import com.carsharing.model.TrackerData;
import com.carsharing.model.Zone;
import com.carsharing.model.android.AndroidCar;

import java.util.List;

public interface CarService {

    List<Car> getOnlineCars(boolean online);

    List<Car> getOpenedCars(boolean opened);

    List<Car> getRentedCars(boolean rented);

    TrackerData getCurrentState(Car car);

    List<Car> getAllCars();

    List<Car> getEnabledCars(boolean enabled);

    Car getCarById(int id);

    Car getCarByNumber(String number);

    AndroidCar getAndroidCarById(int id);

    void save(Car car);

    void delete(Car car);

    boolean openCar(Car car);

    boolean closeCar(Car car);

    boolean testCarOnZone(Car car, Zone zone);
}
