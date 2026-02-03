package com.debtcollection.service.outBound;

import com.debtcollection.entity.Message;
import com.debtcollection.entity.User;

public interface ChannelSender {
    boolean send(Message message, User user);
}
