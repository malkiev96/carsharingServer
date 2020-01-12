package com.carsharing.service.impl;

import com.carsharing.model.Tracker;
import com.carsharing.model.TrackerData;
import com.carsharing.repository.TrackerRepository;
import com.carsharing.service.TrackerDataService;
import com.carsharing.service.TrackerService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TrackerServiceImpl implements TrackerService {

    private final TrackerRepository trackerRepository;
    private final TrackerDataService trackerDataService;

    public TrackerServiceImpl(TrackerRepository trackerRepository, TrackerDataService trackerDataService) {
        this.trackerRepository = trackerRepository;
        this.trackerDataService = trackerDataService;
    }

    public Tracker getById(int id) {
        return trackerRepository.getOne(id);
    }

    public Tracker getByName(String name) {
        return trackerRepository.getTrackerByName(name);
    }

    public Tracker getTrackerByImei(String imei) {
        return trackerRepository.getTrackerByImei(imei);
    }

    public List<Tracker> getAllByEnabled(boolean enabled) {
        return trackerRepository.getAllByEnabled(enabled);
    }

    @Override
    public List<Tracker> getAllByOnline(boolean online) {
        return trackerRepository.getAllByOnline(online);
    }

    public List<Tracker> getAll() {
        return trackerRepository.findAll();
    }

    @Override
    public List<Tracker> getNotEmptyTrackers() {
        return trackerRepository.getAllByCarIsNotNull();
    }

    public List<Tracker> getEmptyTrackers() {
        return trackerRepository.getAllByCarIsNull();
    }

    public void save(Tracker tracker) {
        trackerRepository.save(tracker);
    }

    public void delete(Tracker tracker) {
        if (tracker != null && tracker.getCar() == null) {
            trackerDataService.deleteAllByTracker(tracker);
            trackerRepository.delete(tracker);
        }
    }

    public void testAllOnline() {
        List<Tracker> trackers = getAll();
        for (Tracker tracker : trackers) {
            if (!isOnline(tracker)) {
                tracker.setOnline(false);
            } else tracker.setOnline(true);
            save(tracker);
        }
    }

    public boolean isOnline(Tracker tracker) {
        TrackerData trackerData = trackerDataService.getLastDataByTracker(tracker);
        if (trackerData != null) {
            Timestamp current = new Timestamp(System.currentTimeMillis());
            Long delta = current.getTime() - trackerData.getTimestamp().getTime();
            delta /= 1000;//sec 100
            return (delta - 100) < 0;
        } else return false;
    }


    public Tracker testAuth(int id, String imei) {
        Tracker tracker = trackerRepository.getTrackerByIdentifyNumber(String.valueOf(id));
        if (tracker != null && tracker.getImei().equals(imei)) {
            tracker.setOnline(true);
            save(tracker);
            return tracker;
        }
        return null;
    }
}
