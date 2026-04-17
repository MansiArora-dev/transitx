package com.springboot.transitx.strategies.impl;

import com.springboot.transitx.entities.Driver;
import com.springboot.transitx.entities.RideRequest;
import com.springboot.transitx.repositories.DriverRepository;
import com.springboot.transitx.strategies.DriverMatchingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DriverMatchingNearestDriverStrategy implements DriverMatchingStrategy {

    private final DriverRepository driverRepository;

    @Override
    public List<Driver> findMatchingDriver(RideRequest rideRequest) {
        return driverRepository.findTenNearestDrivers(rideRequest.getPickupLocation());
    }
}
