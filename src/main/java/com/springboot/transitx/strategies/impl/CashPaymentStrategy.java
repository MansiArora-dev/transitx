package com.springboot.transitx.strategies.impl;

//Rider -> 100
//Driver -> 70 Deduct 30Rs from Driver's wallet

import com.springboot.transitx.entities.Driver;
import com.springboot.transitx.entities.Payment;
import com.springboot.transitx.entities.enums.PaymentStatus;
import com.springboot.transitx.entities.enums.TransactionMethod;
import com.springboot.transitx.repositories.PaymentRepository;
import com.springboot.transitx.services.WalletService;
import com.springboot.transitx.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashPaymentStrategy implements PaymentStrategy {

    private final WalletService walletService;
    private final PaymentRepository paymentRepository;

    @Override
    public void processPayment(Payment payment) {
        Driver driver = payment.getRide().getDriver();

        double platformCommission = payment.getAmount() * PLATFORM_COMMISSION;

        walletService.deductMoneyFromWallet(driver.getUser(), platformCommission, null,
                payment.getRide(), TransactionMethod.RIDE);

        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);
    }
}

//10 ratingsCount -> 4.0
//new rating 4.6
//updated rating
//new rating 44.6/11 -> 4.05