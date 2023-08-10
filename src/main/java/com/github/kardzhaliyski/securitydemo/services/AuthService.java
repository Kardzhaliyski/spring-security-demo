package com.github.kardzhaliyski.securitydemo.services;

import com.github.kardzhaliyski.securitydemo.entities.UserEntity;
import com.github.kardzhaliyski.securitydemo.entities.dtos.LoginDTO;
import com.github.kardzhaliyski.securitydemo.entities.dtos.RegisterDTO;
import com.github.kardzhaliyski.securitydemo.enums.Role;
import com.github.kardzhaliyski.securitydemo.repositories.UserRepository;
import com.github.kardzhaliyski.securitydemo.security.JwtUtils;
import com.github.kardzhaliyski.securitydemo.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String login(LoginDTO loginDTO) {
        if (loginDTO.email() == null || loginDTO.email().isBlank() || loginDTO.password() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Username or Password");
        }

        Authentication authenticate;
        try {
            authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password())
            );
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Username or Password");
        }

        UserPrincipal principal = (UserPrincipal) authenticate.getPrincipal();
        return jwtUtils.issue(principal);
    }

    public void register(RegisterDTO registerDTO) {
        if (!registerDTO.isValid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid field data");
        }

        boolean userExist = userRepository.getReferenceByEmail(registerDTO.email()).isPresent();
        if (userExist) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already taken!");
        }

        UserEntity user = UserEntity.builder()
                .name(registerDTO.name())
                .email(registerDTO.email())
                .password(passwordEncoder.encode(registerDTO.password()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }
}
