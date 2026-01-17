package tech.grastone.fz.chat.service;

import tech.grastone.fz.chat.entity.MessageEntity;

import java.util.List;

public interface MessageService {

    MessageEntity saveMessage(MessageEntity messageEntity);
    List<MessageEntity> getPendingMessages(long receiverId);
    void markMessagesAsDelivered(List<Long> messageIds);

}
