package com.github.hjgf0624.sideproject.config.security.domain;

import com.github.hjgf0624.sideproject.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
public class CustomUser extends User {
    private UserEntity user;

    public CustomUser(String userId, String password, Collection<? extends GrantedAuthority> authorities) {
        super(userId, password, authorities);
    }

    public CustomUser(UserEntity user) {
        super(user.getUserId(), user.getUserPw(), user.getAuthorities());
        this.user = user;
    }

    public String getUserId() {
        return user.getUserId();
    }

    public String getNickName() {
        return user.getNickname();
    }
}
