package tech.grastone.fz.chat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.grastone.fz.chat.entity.MessageEntity;
import tech.grastone.fz.chat.enums.DeliveryStatus;
import tech.grastone.fz.chat.model.ChatMessageModel;
import tech.grastone.fz.chat.service.MessageService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api")
public class ChatController {

	private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private MessageService messageService;

	@MessageMapping("/private-message")
	public void sendPrivateMessage(@Payload ChatMessageModel message) {

		String sender = message.getSender();
		String recipient = message.getRecipient();

		if (recipient == null || recipient.isEmpty()) {
			logger.warn("Message recipient is missing. Sender: {}", sender);
			return;
		}

		logger.info("Private message from '{}' to '{}': {}", sender, recipient, message.getContent());

		try {
			messagingTemplate.convertAndSendToUser(recipient, "/messages", message);
			logger.info("Message successfully sent to user '{}'", recipient);
		} catch (Exception e) {
			logger.error("Failed to send message to user '{}'. Error: {}", recipient, e.getMessage(), e);
		}
	}


	@PostMapping("/message")
	public MessageEntity saveMessage(@RequestBody ChatMessageModel messageModel) {
		logger.debug("Received message to save: {}", messageModel);

		MessageEntity entity = new MessageEntity();
		entity.setSenderId(Long.parseLong(messageModel.getSender()));
		entity.setReceiverId(Long.parseLong(messageModel.getRecipient()));
		entity.setContent(messageModel.getContent());
		entity.setStatus(DeliveryStatus.PENDING);

		MessageEntity saved = messageService.saveMessage(entity);
		logger.info("Message saved with ID: {}", saved.getId());

		return saved;
	}

	@GetMapping("/messages/pending/{receiverId}")
	public List<MessageEntity> getPendingMessages(@PathVariable long receiverId) {
		logger.debug("Fetching pending messages for user: {}", receiverId);

		List<MessageEntity> messages = messageService.getPendingMessages(receiverId);
		logger.info("Found {} pending messages for user: {}", messages.size(), receiverId);

		if (!messages.isEmpty()) {
			List<Long> messageIds = messages.stream().map(MessageEntity::getId).toList();
			messageService.markMessagesAsDelivered(messageIds);
			logger.info("Marked {} messages as DELIVERED for user: {}", messageIds.size(), receiverId);
		}

		return messages;
	}

}
