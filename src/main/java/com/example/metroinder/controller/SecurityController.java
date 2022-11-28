package com.example.metroinder.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {

    @PreAuthorize("permitAll()")
    @GetMapping("/user/guest")
    public void guest() {

    }

    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("/user/member")
    public void member() {

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/admin")
    public void admin() {

    }
}
