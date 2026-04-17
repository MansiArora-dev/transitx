package com.springboot.transitx.services;

import com.springboot.transitx.entities.Payment;
import com.springboot.transitx.entities.Ride;
import com.springboot.transitx.entities.enums.PaymentStatus;

public interface PaymentService {

    void processPayment(Ride ride);

    Payment createNewPayment(Ride ride);

    void updatePaymentStatus(Payment payment, PaymentStatus status);
}
