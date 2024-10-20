package com.samuel.auth.services;

import com.samuel.auth.entities.UserEntity;
import com.samuel.auth.entities.UserPrincipal;
import com.samuel.auth.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional< UserEntity> userEntity = userRepo.findByUsername(username);

        if (userEntity.get() != null) {
            return new UserPrincipal(userEntity.get());
        }
        throw new UsernameNotFoundException("User not found");
    }

    public UserDetails loadUserById(String id) {
        Optional<UserEntity> userEntity = userRepo.findById(id);

        if (userEntity.get() != null) {
            return new UserPrincipal(userEntity.get());
        }
        throw new UsernameNotFoundException("User not found");
    }
}
