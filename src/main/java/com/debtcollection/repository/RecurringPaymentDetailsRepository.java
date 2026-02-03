package com.debtcollection.repository;


import com.debtcollection.entity.RecurringPaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecurringPaymentDetailsRepository extends JpaRepository<RecurringPaymentDetails, Long> {
    List<RecurringPaymentDetails> findByEndClientId(Long endClientId);
}