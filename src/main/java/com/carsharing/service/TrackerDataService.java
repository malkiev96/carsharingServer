package com.carsharing.service;

import com.carsharing.model.Tracker;
import com.carsharing.model.TrackerData;

import java.util.List;

public interface TrackerDataService {

    List<TrackerData> getAllByTracker(Tracker tracker);
    List<TrackerData> getAll();
    TrackerData getLastDataByTracker(Tracker tracker);
    void save(TrackerData trackerData);
    void delete(TrackerData trackerData);
    void deleteAllByTracker(Tracker tracker);

}
