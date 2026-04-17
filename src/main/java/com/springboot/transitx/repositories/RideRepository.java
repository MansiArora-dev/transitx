package com.springboot.transitx.repositories;

import com.springboot.transitx.entities.Driver;
import com.springboot.transitx.entities.Ride;
import com.springboot.transitx.entities.Rider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    Page<Ride> findByRider(Rider rider, Pageable pageRequest);

    Page<Ride> findByDriver(Driver driver, Pageable pageRequest);

}