package com.carsharing.service;

import com.carsharing.model.Marker;
import com.carsharing.model.android.AndroidCar;

import java.util.List;

public interface MarkerService {
    List<Marker> getAll();

    List<AndroidCar> getAllForClients();
}
