package com.debtcollection.repository;


import com.debtcollection.entity.ClientNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientNoteRepository extends JpaRepository<ClientNote, Long> {
    List<ClientNote> findByClientId(Long clientId);
}