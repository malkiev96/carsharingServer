package com.carsharing.service;

import com.carsharing.model.Tracker;

import java.util.List;

public interface TrackerService {

    void save(Tracker tracker);

    void delete(Tracker tracker);

    void testAllOnline();

    boolean isOnline(Tracker tracker);

    Tracker testAuth(int id, String imei);

    Tracker getById(int id);

    Tracker getByName(String name);

    Tracker getTrackerByImei(String imei);

    List<Tracker> getAllByEnabled(boolean enabled);
    List<Tracker> getAllByOnline(boolean online);

    List<Tracker> getAll();

    List<Tracker> getEmptyTrackers();

    List<Tracker> getNotEmptyTrackers();
}
