package com.chatapp.chat_backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.chatapp.chat_backend.util.JwtUtil;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JwtAuthenticationFilter
 * 사용자의 요청마다 JWT가 포함되어 있는지 검사하고, 유요한 토큰이라면 Spring Security의 인증
 * 컨텍스트(SecurityContext)에
 * 사용자 정보를 등록하는 필터이다.
 * 이 필터는 로그인 이후의 요청에서 사용자가 인증되었는지를 판단하기 위해 만들었다.
 * Spring Security의 FilterChain에서 OncePerRequestFilter를 상속받아 매 요청마다 한 번만 동작한다.
 * 
 * 주요 동작 흐름:
 * 1. 클라이언트가 Authorization 헤더에 JWT를 담아 요청
 * 2. 해당 JWT를 파싱(parseToken 메서드)하여 추출
 * 3. 유효성 검증 후 (validateToken) → 사용자명을 꺼내고
 * 4. UsernamePasswordAuthenticationToken 객체를 생성 → SecurityContext에 저장
 * 5. SecurityContextHolder에 사용자 인증 정보가 들어가므로 이후 컨트롤러, 서비스에서 인증된 사용자로 동작함
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    /**
     * JwtUtil을 주입받아 JWT 관련 로직(파싱, 검증, 사용자 추출 등)을 위임한다.
     */
    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 매 요청마다 실행되는 메서드로 Spring Security 필터 체인 중 하나이다.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        // http 서블렛을 통해 전달받은 요청에서 JWT를 추출(Authorization 헤더)
        String token = parseToken(request);

        // JWT가 존재하며 유효할 시
        if (token != null && jwtUtil.validateToken(token)) {
            String username = jwtUtil.getUsername(token); // 토큰에서 사용자명 추출

            /**
             * 인증 객체 생성
             * UsernamePasswordAuthenticationToken은 Spring Security가 사용하는 인증 객체
             * - 첫 번째 인자: 사용자명 (Principal)
             * - 두 번째 인자: 인증 정보 (비밀번호 인증을 하지 않으므로 null)
             * - 세 번째 인자: 권한 목록 (아직 권한 설정하지 않았으므로 null)
             * 
             * 왜 UsernamePasswordAuthenticationToken을 쓰나요?
             * → Spring Security가 기본적으로 사용하는 인증 객체입니다. 우리가 직접 커스터마이징한 인증 방식에서도 이 객체를 만들어서
             * SecurityContext에 넣어줘야 인증된 사용자로 인식됩니다.
             */
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null,
                    null);
            // 요청 정보를 Authentication 객체에 추가
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // SecurityContext에 인증 정보를 등록(이후 Security에서 인가에 사용한다)
            SecurityContextHolder.getContext().setAuthentication(authentication);
            /**
             * SecurityContext에 왜 넣나요?
             * → 이후 서비스나 컨트롤러에서 @AuthenticationPrincipal이나
             * SecurityContextHolder.getContext().getAuthentication()을 통해 로그인 정보를 가져올 수 있기
             * 때문.
             */
        }

        try {
            // 다음 필터로 요청을 넘긴다.
            filterChain.doFilter(request, response);
        } catch (java.io.IOException | ServletException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Authorization 헤더에서 Bearer 토큰을 추출하는 메서드
     * 형식: Authorization: Bearer {JWT}
     */
    private String parseToken(HttpServletRequest request) {
        // Authorization 헤더 먼저 확인
        String authHeader = request.getHeader("Authorization");
        // "Bearer "로 시작하는 경우에만 토큰을 간주
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // "Bearer " 다음부터 토큰 부분만 추출
        }
        // 쿠키에서 토큰 가져오기
        if(request.getCookies() != null) {
            for(Cookie cookie : request.getCookies()) {
                if("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null; // 유효하지 않으면 null 반환
    }
}
