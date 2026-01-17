package tech.grastone.fz.chat.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.grastone.fz.chat.dao.MessageDao;
import tech.grastone.fz.chat.entity.MessageEntity;
import tech.grastone.fz.chat.enums.DeliveryStatus;
import tech.grastone.fz.chat.service.MessageService;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageDao messageDao;

    @Override
    public MessageEntity saveMessage(MessageEntity messageEntity) {
        log.debug("Saving message: {}", messageEntity);
        MessageEntity savedMessage = messageDao.saveMessage(messageEntity);
        log.info("Message saved with ID: {}", savedMessage.getId());
        return savedMessage;
    }

    @Override
    public List<MessageEntity> getPendingMessages(long receiverId) {
        log.debug("Fetching pending messages for receiverId: {}", receiverId);
        List<MessageEntity> pendingMessages = messageDao.getMessagesByStatus((int) receiverId, DeliveryStatus.PENDING);
        log.info("Retrieved {} pending messages for receiverId: {}", pendingMessages.size(), receiverId);
        return pendingMessages;
    }

    @Override
    public void markMessagesAsDelivered(List<Long> messageIds) {
        messageDao.updateMessageStatus(messageIds, DeliveryStatus.DELIVERED);
    }
}
