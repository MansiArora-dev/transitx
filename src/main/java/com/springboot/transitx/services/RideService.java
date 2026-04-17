package com.springboot.transitx.services;

import com.springboot.transitx.dto.RideRequestDto;
import com.springboot.transitx.entities.Driver;
import com.springboot.transitx.entities.Ride;
import com.springboot.transitx.entities.RideRequest;
import com.springboot.transitx.entities.Rider;
import com.springboot.transitx.entities.enums.RideStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RideService {

    Ride getRideById(Long rideId);

    Ride createNewRide(RideRequest rideRequest, Driver driver);

    Ride updateRideStatus(Ride ride, RideStatus rideStatus);

    Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest);

    Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest);
}
