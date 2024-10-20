package com.samuel.auth.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "refresh_tokens")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenEntity {
    @Id
    private String token;
    private String userId;
    private Long expirationTime;
    private  Boolean isActive;
}
