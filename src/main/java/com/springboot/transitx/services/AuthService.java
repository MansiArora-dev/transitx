package com.springboot.transitx.services;

import com.springboot.transitx.dto.DriverDto;
import com.springboot.transitx.dto.SignupDto;
import com.springboot.transitx.dto.UserDto;

public interface AuthService {

    String[] login(String email, String password);

    UserDto signup(SignupDto signupDto);

    DriverDto onboardNewDriver(Long userId, String vehicleId);

    String refreshToken(String refreshToken);
}
