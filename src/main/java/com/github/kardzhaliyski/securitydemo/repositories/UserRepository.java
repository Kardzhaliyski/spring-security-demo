package com.github.kardzhaliyski.securitydemo.repositories;

import com.github.kardzhaliyski.securitydemo.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> getReferenceByEmail(String email);
}
