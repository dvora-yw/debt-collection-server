package com.debtcollection.repository;

import com.debtcollection.entity.ClientContacts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ClientContactRepository extends JpaRepository<ClientContacts, Long> {
        List<ClientContacts> findByClientId(Long clientId);

}
