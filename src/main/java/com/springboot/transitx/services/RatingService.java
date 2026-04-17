package com.springboot.transitx.services;

import com.springboot.transitx.dto.DriverDto;
import com.springboot.transitx.dto.RiderDto;
import com.springboot.transitx.entities.Ride;

public interface RatingService {

    DriverDto rateDriver(Ride ride, Integer rating);
    RiderDto rateRider(Ride ride, Integer rating);

    void createNewRating(Ride ride);
}
