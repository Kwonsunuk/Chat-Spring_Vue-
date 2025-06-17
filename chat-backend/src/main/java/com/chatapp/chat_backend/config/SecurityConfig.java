package com.chatapp.chat_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 보안 설정을 정의하는 클래스이다.
 *
 * 개발/테스트 단계에서 API 테스트를 편리하게 하기 위해 설정되었으며,
 * 실제 운영에서는 반드시 JWT 인증 기반으로 보안을 구성해야 합니다.
 *
 * 주요 설정:
 * - CSRF 비활성화
 * - 모든 경로에 대해 permitAll() 허용
 * - 세션 사용 안 함 (Stateless)
 *
 * @Configuration: 해당 클래스가 설정 클래스임을 나타냅니다.
 * @Bean: 반환된 객체를 Spring Bean으로 등록합니다.
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (API 테스트 용도)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 모든 요청 허용
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS)) // 세션 사용 안 함
                .httpBasic(withDefaults()); // 기본 HTTP 인증 방식 사용 가능 (테스트용)

        return http.build();
    }
    /**
     * 비밀번호 암호화를 위한 BCryptPasswordEncoder 빈 등록
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
