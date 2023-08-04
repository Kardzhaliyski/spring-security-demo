package com.github.kardzhaliyski.securitydemo.security;

import com.github.kardzhaliyski.securitydemo.entities.UserEntity;
import com.github.kardzhaliyski.securitydemo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity entity = userRepository.getReferenceByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return UserPrincipal.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .name(entity.getName())
                .password(entity.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(entity.getRole().name())))
                .build();
    }
}
