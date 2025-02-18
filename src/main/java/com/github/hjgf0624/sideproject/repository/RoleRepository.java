package com.github.hjgf0624.sideproject.repository;

import com.github.hjgf0624.sideproject.entity.RoleEntity;
import com.github.hjgf0624.sideproject.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByRoleName(String roleName);
}
