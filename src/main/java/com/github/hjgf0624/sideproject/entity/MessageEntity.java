package com.github.hjgf0624.sideproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="job_message")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    @Column(name = "title", length = 40, nullable = false)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "create_at")
    private LocalDateTime createdAt;

    @Column(name = "recruit_count")
    private int capacityMemberNum;  // 모집하는 총 인원 수

    @Formula("(SELECT COUNT(*) FROM job_participant jp WHERE jp.message_id = id)")
    private int currentMemberNum;   // 현재 참여한 인원 수

    //@ElementCollection
    //private List<String> memberList; // 참여 멤버 리스트
}