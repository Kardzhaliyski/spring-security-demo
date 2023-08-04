package com.github.kardzhaliyski.securitydemo.controllers;

import com.github.kardzhaliyski.securitydemo.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class RestrictedController {

    @GetMapping("/restricted")
    public ResponseEntity<String> restricted(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok("You have entered restricted area! Nice! " + principal.getEmail());
    }
}
