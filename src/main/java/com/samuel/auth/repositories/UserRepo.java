package com.samuel.auth.repositories;

import com.samuel.auth.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepo extends CrudRepository<UserEntity, String> {
    Optional<UserEntity> findByUsername(String username);
}
