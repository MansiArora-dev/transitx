package com.springboot.transitx.strategies;

import com.springboot.transitx.entities.RideRequest;

public interface RideFareCalculationStrategy {

    double RIDE_FARE_MULTIPLIER = 10;

    double calculateFare(RideRequest rideRequest);

}
