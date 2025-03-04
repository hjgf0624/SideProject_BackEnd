package com.github.hjgf0624.sideproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user")
public class UserEntity {

    @Id
    @Column(name = "user_id", length = 20, nullable = false)
    private String userId; // 기본키, VARCHAR(20)

    @Column(name = "user_pw", length = 24, nullable = false)
    private String userPw; // 비밀번호, VARCHAR(24)

    @Column(name = "name", length = 100, nullable = false)
    private String name; // 이름, VARCHAR(100)

//    @Column(name = "age")
//    private Integer age; // 나이, INT

    @Column(name = "birthdate")
    private String birthdate;

//    @Column(name = "height")
//    private Double height; // 키, DOUBLE

//    @Column(name = "interests", columnDefinition = "TEXT")
//    private String interests; // 관심사, TEXT

    @Column(name = "latitude", nullable = true)
    private Double latitude = 0.0; // 위도, FLOAT

    @Column(name = "longitude", nullable = true)
    private Double longitude = 0.0; // 경도, FLOAT

    @Column(name = "nickname", length = 60)
    private String nickname; // 닉네임, VARCHAR(60)

    @Column(name = "status", columnDefinition = "CHAR(1)")
    private String status; // 상태, CHAR(1)

    @Column(name = "credit")
    private Integer credit = 0; // 크레딧, INT

    @Column(name = "rating")
    private Integer rating = 0; // 평가 수치, INT

    @Column(name = "phone_number", length = 20)
    private String phoneNumber; // 전화번호, VARCHAR(20)

    @Column(name = "profile_image_url", length = 255)
    private String profileImageUrl;

    @Column(name = "firebase_token", length = 255)
    private String firebaseToken;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt; // 생성시간, TIMESTAMP DEFAULT CURRENT_TIMESTAMP

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt; // 수정시간, TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<MessageParticipantEntity> participants;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )

    private List<RoleEntity> roles = new ArrayList<>();

    public Collection<SimpleGrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
    }

    public void addRole(RoleEntity role) {
        this.roles.add(role);
    }
}
