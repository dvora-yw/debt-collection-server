package com.debtcollection.repository;

import com.debtcollection.entity.PaymentCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentChargeRepository extends JpaRepository<PaymentCharge, Long> {
    List<PaymentCharge> findByRecurringPaymentId(Long recurringPaymentId);
    List<PaymentCharge> findByEndClient_IdAndStatus(Long endClientId, String status);

}