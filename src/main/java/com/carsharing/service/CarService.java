package com.carsharing.service;

import com.carsharing.model.Car;
import com.carsharing.model.TrackerData;
import com.carsharing.model.Zone;
import com.carsharing.model.android.AndroidCar;

import java.util.List;

public interface CarService {

    List<Car> getAllByOnline(boolean online);

    List<Car> getAllByOpened(boolean opened);

    List<Car> getAllByRented(boolean rented);

    TrackerData getActualData(Car car);

    List<Car> getAllCars();

    List<Car> getAllByEnabled(boolean enabled);

    Car getCarById(int id);

    Car getCarByNumber(String number);

    AndroidCar getAndroidCarById(int id);

    void saveCar(Car car);

    void deleteCar(Car car);

    boolean openCar(Car car);
    boolean closeCar(Car car);

    boolean testCarOnZone(Car car, Zone zone);
}
