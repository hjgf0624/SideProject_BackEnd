package com.github.hjgf0624.sideproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name="job_message")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "user_id", length = 60, nullable = false)
    private String userId;

    @Column(name = "title", length = 40, nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "meeting_date_time", nullable = false)
    private LocalDateTime meetingDateTime;

    @Column(name = "recruit_count")
    private Integer recruitCount;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "anonymous")
    private boolean anonymous;

    @Column(name = "views", nullable = false)
    private Integer views = 0;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MessageParticipantEntity> participants;
}

