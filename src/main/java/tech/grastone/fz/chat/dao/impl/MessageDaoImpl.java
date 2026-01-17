package tech.grastone.fz.chat.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import tech.grastone.fz.chat.dao.MessageDao;
import tech.grastone.fz.chat.entity.MessageEntity;
import tech.grastone.fz.chat.enums.DeliveryStatus;
import tech.grastone.fz.chat.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@AllArgsConstructor
@Slf4j
public class MessageDaoImpl implements MessageDao {

    private final MessageRepository messageRepository;

    @Override
    public MessageEntity saveMessage(MessageEntity message) {
        log.debug("Saving single message: {}", message);
        MessageEntity savedMessage = messageRepository.saveAndFlush(message);
        log.info("Message saved with ID: {}", savedMessage.getId());
        return savedMessage;
    }

    @Override
    public List<MessageEntity> saveMessage(List<MessageEntity> messages) {
        log.debug("Saving list of {} messages", messages.size());
        List<MessageEntity> savedMessages = messageRepository.saveAllAndFlush(messages);
        log.info("Saved {} messages", savedMessages.size());
        return savedMessages;
    }

    @Override
    public List<MessageEntity> getMessagesByStatus(long receiverId, DeliveryStatus status) {
        log.debug("Fetching messages for receiverId: {}, with status: {}", receiverId, status);
        List<MessageEntity> messages = messageRepository.findByReceiverIdAndStatus(receiverId, status);
        log.info("Found {} messages for receiverId: {} with status: {}", messages.size(), receiverId, status);
        return messages;
    }

    @Override
    public void updateMessageStatus(List<Long> messageIds, DeliveryStatus status) {
        List<MessageEntity> messages = messageRepository.findAllById(messageIds);
        for (MessageEntity msg : messages) {
            msg.setStatus(status);
            msg.setDeliveredAt(LocalDateTime.now());
        }
        messageRepository.saveAll(messages);
        messageRepository.flush();
    }
}
