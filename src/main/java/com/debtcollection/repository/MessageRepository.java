package com.debtcollection.repository;
import com.debtcollection.dto.message.MessageDto;
import com.debtcollection.dto.message.MessageViewDto;
import com.debtcollection.entity.Message;
import com.debtcollection.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>{

    @Query("""
SELECT new com.debtcollection.dto.message.MessageViewDto(
    m.id,
    m.content,
    CASE
        WHEN p.id IS NOT NULL THEN CONCAT(p.firstName, ' ', p.lastName)
        WHEN cc.id IS NOT NULL THEN CONCAT(cc.firstName, ' ', cc.lastName)
        ELSE 'מערכת'
    END,
    m.source,
    m.createdAt
)
FROM Message m
LEFT JOIN m.person p
LEFT JOIN m.clientContact cc
ORDER BY m.createdAt DESC
""")
    List<MessageViewDto> findAllForView();
}
