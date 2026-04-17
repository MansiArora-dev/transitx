package com.springboot.transitx.repositories;

import com.springboot.transitx.entities.Driver;
import com.springboot.transitx.entities.Rating;
import com.springboot.transitx.entities.Ride;
import com.springboot.transitx.entities.Rider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByRider(Rider rider);
    List<Rating> findByDriver(Driver driver);

    Optional<Rating> findByRide(Ride ride);
}
