package com.springboot.transitx.strategies;

import com.springboot.transitx.entities.Driver;
import com.springboot.transitx.entities.RideRequest;

import java.util.List;

public interface DriverMatchingStrategy {

    List<Driver> findMatchingDriver(RideRequest rideRequest);
}
