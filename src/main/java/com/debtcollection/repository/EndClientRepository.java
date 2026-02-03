package com.debtcollection.repository;
import com.debtcollection.entity.EndClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EndClientRepository extends JpaRepository<EndClient, Long>{
//        List<EndClient> findByClientId(Long clientId);
//        Optional<EndClient> findByNameAndClientId(String name, Long clientId);
        Optional<EndClient> findById(Long id);
         Optional<EndClient> findByUsers_Id(Long userId);
    // מחפש EndClients שמחוברים ל-Client עם id מסוים
    @Query("SELECT ec FROM EndClient ec JOIN ec.clients c WHERE c.id = :clientId")
    List<EndClient> findByClientId(@Param("clientId") Long clientId);

    // לדוגמה: find by name + client
    @Query("SELECT ec FROM EndClient ec JOIN ec.clients c WHERE ec.name = :name AND c.id = :clientId")
    Optional<EndClient> findByNameAndClientId(@Param("name") String name, @Param("clientId") Long clientId);

}

