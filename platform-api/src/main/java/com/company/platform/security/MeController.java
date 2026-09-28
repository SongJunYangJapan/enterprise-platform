package com.company.platform.security;

import java.security.Principal;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeController {
    @GetMapping("/api/me")
    public Map<String, String> me(Principal principal) { return Map.of("username", principal.getName()); }
}
