package com.debtcollection.repository;
import com.debtcollection.dto.payment.PaymentViewDto;
import com.debtcollection.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{


        @Query("""
    SELECT new com.debtcollection.dto.payment.PaymentViewDto(
        p.id,
        ec.id,
        ec.name,
        p.amount,
        p.date
    )
    FROM Payment p
    JOIN p.endClient ec
    ORDER BY p.date DESC
""")
        List<PaymentViewDto> findAllForView();

}

