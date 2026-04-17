package com.springboot.transitx.strategies;

import com.springboot.transitx.entities.Payment;

public interface PaymentStrategy {
    Double PLATFORM_COMMISSION = 0.3;
    void processPayment(Payment payment);

}
