package com.github.hjgf0624.sideproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageParticipantId implements Serializable {

    @Column(name = "user_id")
    private String userId;

    @Column(name = "message_id")
    private Long messageId;
}