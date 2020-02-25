package com.carsharing.repository;

import com.carsharing.model.Car;
import com.carsharing.model.Tracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {

    Car getCarByNumber(String number);
    Car getCarByTracker(Tracker tracker);
    List<Car> getAllByEnabled(boolean enabled);
    List<Car> getAllByRented(boolean rented);
    @Query("SELECT c from Car c where c.tracker.online=:online")
    List<Car> getAllByOnline(@Param("online") boolean online);
}
