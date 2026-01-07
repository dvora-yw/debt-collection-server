package com.debtcollection.repository;
import com.debtcollection.entity.EndClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EndClientRepository extends JpaRepository<EndClient, Long>{
        List<EndClient> findByClientId(Long clientId);

}

