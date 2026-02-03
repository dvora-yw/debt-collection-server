package com.debtcollection.repository;


import com.debtcollection.entity.ClientNotificationPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ClientNotificationPolicyRepository extends JpaRepository<ClientNotificationPolicy, Long> {
    Optional<ClientNotificationPolicy> findByClientId(Long clientId);
}
