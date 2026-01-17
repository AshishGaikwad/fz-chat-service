package tech.grastone.fz.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.grastone.fz.chat.entity.MessageEntity;
import tech.grastone.fz.chat.enums.DeliveryStatus;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity,Long> {
    List<MessageEntity> findByReceiverIdAndStatus(long receiverId, DeliveryStatus status);
}
