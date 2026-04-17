package com.springboot.transitx.services;

import com.springboot.transitx.dto.DriverDto;
import com.springboot.transitx.dto.RideDto;
import com.springboot.transitx.dto.RideRequestDto;
import com.springboot.transitx.dto.RiderDto;
import com.springboot.transitx.entities.Rider;
import com.springboot.transitx.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface RiderService {

    RideRequestDto requestRide(RideRequestDto rideRequestDto);

    RideDto cancelRide(Long rideId);

    DriverDto rateDriver(Long rideId, Integer rating);

    RiderDto getMyProfile();

    Page<RideDto> getAllMyRides(PageRequest pageRequest);

    Rider createNewRider(User user);

    Rider getCurrentRider();
}


