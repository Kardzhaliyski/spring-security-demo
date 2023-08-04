package com.github.kardzhaliyski.securitydemo.controllers;

import com.github.kardzhaliyski.securitydemo.entities.dtos.LoginDTO;
import com.github.kardzhaliyski.securitydemo.entities.dtos.RegisterDTO;
import com.github.kardzhaliyski.securitydemo.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public ResponseEntity<String> loginScreen() {
        return ResponseEntity.ok("Login menu!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        String jwt = authService.login(loginDTO);
        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) throws URISyntaxException {
        authService.register(registerDTO);

        return ResponseEntity.created(new URI("/login")).body("Registered!");
    }
}
