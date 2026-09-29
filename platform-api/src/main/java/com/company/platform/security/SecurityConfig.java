package com.company.platform.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    // 公司 SSO 单点登录就绪后，需要设置 SSO_ISSUER_URI 环境变量，
    // 并在这里完成 JWT 声明（claims）到权限/角色的映射。
    @Bean
    @Profile("!dev")
    SecurityFilterChain oidcSecurity(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.requestMatchers("/actuator/health").permitAll().anyRequest().authenticated())
            .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults())).build();
    }
}
