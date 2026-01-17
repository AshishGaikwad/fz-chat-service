package tech.grastone.fz.chat.dao;

import tech.grastone.fz.chat.entity.MessageEntity;
import tech.grastone.fz.chat.enums.DeliveryStatus;

import java.util.List;

public interface MessageDao {
    MessageEntity saveMessage(MessageEntity message);
    List<MessageEntity> saveMessage(List<MessageEntity> message);
    List<MessageEntity> getMessagesByStatus(long receiverId, DeliveryStatus status);
    void updateMessageStatus(List<Long> messageIds, DeliveryStatus status);

}
