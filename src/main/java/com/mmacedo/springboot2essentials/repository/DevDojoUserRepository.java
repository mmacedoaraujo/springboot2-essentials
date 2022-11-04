package com.mmacedo.springboot2essentials.repository;

import com.mmacedo.springboot2essentials.domain.DevDojoUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DevDojoUserRepository extends JpaRepository<DevDojoUser, Long> {

    Optional<DevDojoUser> findByUsername(String username);
}
