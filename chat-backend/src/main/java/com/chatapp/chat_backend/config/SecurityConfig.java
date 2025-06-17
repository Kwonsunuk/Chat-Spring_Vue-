package com.chatapp.chat_backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.chatapp.chat_backend.security.JwtAuthenticationFilter;
import com.chatapp.chat_backend.util.JwtUtil;

/**
 * Spring Security 보안 설정을 정의하는 클래스이다.
 *
 * JWT 기반의 인증 방식을 사용하며, 세션은 사용하지 않는 Stateless 구조로 설계되었습니다.
 * 회원가입, 로그인 요청은 인증 없이 접근 가능하지만,
 * 그 외 모든 요청은 JWT 인증이 필요합니다.
 *
 * 주요 설정:
 * - CSRF, 세션, 필터 체인 등 보안 관련 설정을 수행
 * - 로그인/회원가입을 제외한 모든 요청에 대해 JWT 필터를 적용합
 * - 모든 경로에 대해 permitAll() 허용
 * - 세션 사용 안 함 (Stateless)
 *
 * @Configuration: 해당 클래스가 설정 클래스임을 나타낸다.
 * @EnableWebSecurity: Spring Security 활성화
 * @Bean: 반환된 객체를 Spring Bean으로 등록
 *        - PasswordEncoder: 비밀번호 암호화 설정
 *        - SecurityFilterChain: 보안 설정의 핵심, 필터 체인 정의
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtUtil jwtUtil;

    /**
     * 생성자 주입 방식으로 JwtUtil을 주입.
     * JwtAuthenticationFilter에 의존성을 전달하기 위해 필요.
     */
    @Autowired
    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 보안 필터 체인을 설정한다.
     * -CSRF: 비활성화(REST API 서버는 세션을 사용하지 않기에 불필요)
     * -Session: STATELESS 모드로 설정 (세션을 사용하지 않음)
     * -경로 접근 권한:
     * - /api/users/**: 인증 없이 접근 가능 (회원가입, 로그인 시)
     * - 그 외 모든 요청에는 JWT 필요로 Role 기반 페이지별 접근 권한 설정
     * -JwtAuthenticationFilter: 인증 전에 JWT 검증 필터를 적용
     * 
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (API 테스트 목적)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션을 사용하지 않는 JWT 기반 인증
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/**").permitAll() // 회원가입, 로그인은 인증 없이 접근 허용
                        .anyRequest().authenticated()) // 그 외 요청은 인증 필요
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil),
                        UsernamePasswordAuthenticationFilter.class); // 인증 전에 JWT 필터 실행

        return http.build(); // 필터 체인 빌드
    }

    /**
     * 비밀번호 암호화를 위한 BCryptPasswordEncoder 빈 등록
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
