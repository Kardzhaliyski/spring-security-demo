package com.github.kardzhaliyski.securitydemo.controllers;

import com.github.kardzhaliyski.securitydemo.security.UserPrincipal;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restricted")
public class RestrictedController {

    @GetMapping("/admin")
    public ResponseEntity<String> admin(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok("You have entered restricted area Only for ADMINS! Nice! " + principal.getEmail());
    }

    @GetMapping("/admin2")
    @Secured({"ADMIN"})
    public ResponseEntity<String> admin2(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok("You have entered restricted area 2 Only for ADMINS! Nice! " + principal.getName());
    }

    @GetMapping("/users")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostAuthorize("hasAuthority('USER')")
//    @RolesAllowed({"USER"})
    public ResponseEntity<String> users(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok("You have entered restricted area Only for Users! Nice! " + principal.getName());
    }

    @GetMapping({"/all", "/"})
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
//    @PostAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<String> all(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok("You have entered restricted area for Users and Admins! Nice! " + principal.getName());
    }


}
