package com.debtcollection.repository;

import com.debtcollection.entity.AccountBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountBalanceRepository extends JpaRepository<AccountBalance, Long> {
    Optional<AccountBalance> findByEndClientId(Long endClientId);
    List<AccountBalance> findAllByEndClientId(Long endClientId);
}