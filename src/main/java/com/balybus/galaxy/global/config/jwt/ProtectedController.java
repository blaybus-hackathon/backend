package com.balybus.galaxy.global.config.jwt;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/protected")
public class ProtectedController {
    @GetMapping("/user")
    public String getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return "Authenticated user: " + userDetails.getUsername();
    }
}
