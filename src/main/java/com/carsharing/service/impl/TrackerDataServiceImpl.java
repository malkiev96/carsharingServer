package com.carsharing.service.impl;

import com.carsharing.model.Tracker;
import com.carsharing.model.TrackerData;
import com.carsharing.repository.TrackerDataRepository;
import com.carsharing.service.TrackerDataService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackerDataServiceImpl implements TrackerDataService {

    private final TrackerDataRepository trackerDataRepository;

    public TrackerDataServiceImpl(TrackerDataRepository trackerDataRepository) {
        this.trackerDataRepository = trackerDataRepository;
    }


    public TrackerData getLastDataByTracker(Tracker tracker) {
        return trackerDataRepository.getLastDataByTracker(tracker);
    }


    public List<TrackerData> getAll() {
        return trackerDataRepository.findAll();
    }

    public List<TrackerData> getAllByTracker(Tracker tracker) {
        return trackerDataRepository.getAllByTracker(tracker);
    }

    public void save(TrackerData trackerData) {
        trackerDataRepository.save(trackerData);
    }

    public void delete(TrackerData trackerData) {
        trackerDataRepository.delete(trackerData);
    }

    @Override
    public void deleteAllByTracker(Tracker tracker) {
        trackerDataRepository.deleteAllByTracker(tracker);
    }
}
