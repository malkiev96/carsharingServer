package com.carsharing.repository;

import com.carsharing.model.Car;
import com.carsharing.model.Tracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackerRepository extends JpaRepository<Tracker, Integer> {
    Tracker getTrackerByCar(Car car);

    Tracker getTrackerByName(String name);

    Tracker getTrackerByImei(String imei);

    Tracker getTrackerByPhoneFirst(String phoneFirst);
    Tracker getTrackerByIdentifyNumber(String identifyNumber);

    Tracker getTrackerByPhoneSecond(String phoneSecond);

    List<Tracker> getAllByEnabled(boolean enabled);
    List<Tracker> getAllByOnline(boolean online);
    List<Tracker> getAllByCarIsNull();
    List<Tracker> getAllByCarIsNotNull();
}
