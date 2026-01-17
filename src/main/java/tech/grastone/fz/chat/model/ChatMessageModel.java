package tech.grastone.fz.chat.model;

import lombok.Data;

@Data
public class ChatMessageModel {
	private String sender;
	private String recipient;
	private String content;
}
