package com.chatapp.chat_backend.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 인증된 사용자의 정보를 가져오기 위한 유틸 클래스
 * JwtAuthenticationFilter에서 SecurityContextr에 저장된 인증 객체로부터
 * 현재 로그인 된 사용자의 username을 가져온다.
 */
public class AuthUtil {
    /**
     * 현재 인증된 사용자의 username을 반환한다.
     * 인증되지 않거나 비어있는 경우 null을 반환.
     * JwtUtil.java의 getUsername의 경우 로그인 성공 직후 클라이언트가 보낸 JWT로부터
     * username을 꺼내서 SecurityContext에 저장하고,
     * AuthUtil에서는 SecurityContext에서 해당 username을 꺼내온다.
     * 
     * 이렇게 역할을 분리하는 이유는 다음과 같다:
     * 
     * 1. 로그인 성공 → JWT 생성
     * • jwtUtil.createToken(username)
     * 
     * 2. JWT를 파싱해 사용자명 추출 → SecurityContext에 등록
     * • jwtUtil.getUsername(token) →
     * SecurityContextHolder.getContext().setAuthentication(...)
     * 
     * 3. 이후 요청에서는 SecurityContext에서 사용자명만 꺼내면 됨
     * • AuthUtil.getCurrentUsername()
     *
     * ➕ 이렇게 구조를 나누면 얻는 장점:
     * - 컨트롤러나 서비스 로직에서 토큰 파싱 로직이 필요 없어 코드가 간결해짐
     * - JWT 검증 및 사용자 정보 추출은 필터에서 한 번만 수행
     * - 인증 정보는 전역(SecurityContext)에 저장되므로 어떤 계층에서도 쉽게 접근 가능
     * - 테스트나 유지보수, 확장 시 유연하게 대응 가능 (예: Role, 권한 등 추가)
     *
     * 🔐 실제 운영 환경에서는 요청마다 SecurityContext에 있는 인증 정보로
     * 현재 사용자를 식별하고 권한 처리를 하게 되므로, 이 유틸 메서드는 핵심적인 역할을 한다.
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName(); // JwtAuthenticationFilter에서 등록한 사용자명
        }
        return null;
    }

}
