package com.debtcollection.repository;

import com.debtcollection.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
    Optional<User> findByEmail(String email);
    Optional<User> findByIdentificationNumber(String nationalId);
    Optional<User> findFirstByEndClient_IdAndEmailIsNotNull(Long endClientId);

    Optional<User> findByEmailVerificationCode(String emailVerificationCode);
    boolean existsByEmailAndEndClientId(String email, Long endClientId);
    boolean existsByEmailAndClientId(String email, Long clientId);
    Optional<User> findFirstByEndClientIdAndEnabledTrueOrderByIdAsc(Long endClientId);
    List<User> findByEndClient_IdAndEnabledTrue(Long endClientId);

}
