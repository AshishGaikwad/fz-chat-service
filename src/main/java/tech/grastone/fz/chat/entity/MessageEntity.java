package tech.grastone.fz.chat.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import tech.grastone.fz.chat.entity.base.BaseEntity;
import tech.grastone.fz.chat.enums.DeliveryStatus;
import tech.grastone.fz.chat.enums.MsgContentType;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="messages")
@Getter
@Setter
public class MessageEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long senderId;
    private long receiverId;
    @Column(length = 1000)
    private String content;

    private LocalDateTime deliveredAt;
    private LocalDateTime readAt;

    @Enumerated(EnumType.ORDINAL)
    private MsgContentType contentType;

    @Enumerated(EnumType.ORDINAL)
    private DeliveryStatus status;

}
