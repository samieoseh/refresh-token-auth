package com.samuel.auth.repositories;

import com.samuel.auth.entities.RefreshTokenEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RefreshTokenRepo extends CrudRepository<RefreshTokenEntity, String> {

    boolean existsByTokenAndIsActive(String refreshToken, boolean isActive);

    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshTokenEntity r WHERE r.userId IN :ids")
    void deleteByUserIdIn(List<String> ids);
}
