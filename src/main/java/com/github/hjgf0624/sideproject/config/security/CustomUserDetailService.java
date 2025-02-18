package com.github.hjgf0624.sideproject.config.security;

import com.github.hjgf0624.sideproject.config.security.domain.CustomUser;
import com.github.hjgf0624.sideproject.entity.UserEntity;
import com.github.hjgf0624.sideproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserEntity userInfo = userRepository.findByUserId(userId);

        if (userInfo == null) {
            throw new UsernameNotFoundException(userId);
        }

        return new CustomUser(userInfo);
    }
}
