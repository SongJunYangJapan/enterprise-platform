package com.company.platform.security;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@Profile("dev")
public class DevSecurityConfig {
    // 仅限开发环境：没有真实的账号体系或密码，所有请求都模拟成 dev.user 登录。
    // 除本地开发外，任何环境都绝不能启用这个 profile。
    @Bean
    SecurityFilterChain devSecurity(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
            .addFilterBefore(new OncePerRequestFilter() {
                @Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                    FilterChain chain) throws ServletException, IOException {
                    var principal = new UsernamePasswordAuthenticationToken("dev.user", "",
                        java.util.List.of(new SimpleGrantedAuthority("ROLE_DEVELOPER")));
                    SecurityContextHolder.getContext().setAuthentication(principal);
                    try { chain.doFilter(request, response); }
                    finally { SecurityContextHolder.clearContext(); }
                }
            }, org.springframework.security.web.authentication.AnonymousAuthenticationFilter.class).build();
    }
}
