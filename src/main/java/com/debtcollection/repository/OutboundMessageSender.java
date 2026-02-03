package com.debtcollection.repository;

import com.debtcollection.entity.Message;

public interface OutboundMessageSender {
    void sendNow(Message message) throws Exception;
}
