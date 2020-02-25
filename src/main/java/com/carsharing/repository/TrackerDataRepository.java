package com.carsharing.repository;

import com.carsharing.model.Tracker;
import com.carsharing.model.TrackerData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackerDataRepository extends JpaRepository<TrackerData, Integer> {

    List<TrackerData> getAllByTracker(Tracker tracker);
    @Query("SELECT td from TrackerData td WHERE td.timestamp=(SELECT max(td.timestamp) FROM TrackerData td WHERE td.tracker=:tracker) and td.tracker=:tracker")
    TrackerData getLastDataByTracker(@Param("tracker") Tracker tracker);
    void deleteAllByTracker(Tracker tracker);
}
