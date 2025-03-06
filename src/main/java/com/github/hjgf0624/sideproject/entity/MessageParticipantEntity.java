package com.github.hjgf0624.sideproject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "job_participant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageParticipantEntity {

    @EmbeddedId
    private MessageParticipantId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity user;

    @ManyToOne
    @MapsId("messageId")
    @JoinColumn(name = "message_id", referencedColumnName = "message_id", insertable = false, updatable = false)
    private MessageEntity message;
}