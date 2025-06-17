package com.chatapp.chat_backend.util;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * JwtUtil은 JWT(Json Web Token)을 생성하고 검증하는 유틸리티 클래스이다.
 * 로그인 성공 시 토큰을 발급하고, 이후 각종 통신 과정 속에서 발생하는 인증 과정에서 토큰의 유효성을 검사하는데 사용한다.
 * 
 * @Component: Spring이 해당 클래스를 Bean으로 자동 등록하는데 다른 클래스에서 @Autowired로 의존성 주입이 가능하다.
 * SECRET: HMAC: 암호화 시 사용할 비밀 키(32자 이어야 안전하다)
 * EXPIRATION_TIME: JWT(Json Web Token)의 만료 시간(1시간)
 *
 * createToken(String username): 매개변수로 주어진 사용자 이름(username)으로 JWT을 생성하는 메서드
 * getUsername(String token): 토큰에서 사용자명(subject)을 추출하는 메서드 
 * validateToken(String token): 토큰의 유효성을 검사하기 위한 메서드로 서명오류나, 만료 등을 체크한다.
 * 
 * JWT는 총 3개의 부분으로 구성되며, 각각 `.`(닷)으로 구분됩니다.
 * 형식: <Header>.<Payload>.<Signature>
 * 예시: `eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3b29naSIsImlhdCI6MTY4...`
 * 
 * 1. Header (헤더)
 * - JWT의 타입과 서명에 사용된 알고리즘 정보를 담습니다.
 * - 예시: { "alg": "HS256", "typ": "JWT" }
 * 
 * 2. Payload (페이로드)
 * - 실제로 서버와 클라이언트 간에 전달할 정보를 담는 부분입니다.
 * - 사용자명, 권한, 발급 시간(iat), 만료 시간(exp) 등이 포함됩니다.
 * - 예시: { "sub": "woogi", "iat": 1681234567, "exp": 1681238167 }
 * 
 * 3. Signature (서명)
 * - 위의 Header와 Payload를 base64 인코딩 후, SECRET 키와 함께 지정된 알고리즘으로 서명한 결과입니다.
 * - 이 서명을 통해 토큰이 위조되지 않았음을 검증할 수 있습니다.
 * - 예시: HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload), SECRET)
 * 
 *  보안 주의사항:
 * - SECRET 키는 외부에 노출되지 않도록 환경변수 또는 .env로 분리해야 합니다.
 * - Payload에 너무 민감한 정보(예: 주민번호, 비밀번호)를 넣지 않도록 주의합니다. JWT는 누구나 디코딩할 수 있기 때문입니다.
 */

@Component
public class JwtUtil {
    // JWT 서명을 받기 위한 비밀 키로서 외부로 유출이 되어서는 안된다.
    private static final String SECRET = "mychatappsecretkey1234567890!@#$";
    
    // 발근된 토큰(JWT)의 만료 시간을 정하는 변수 (1시간)
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    // HMAC-SHA256 알고리즘 방식으로 사용하 Key 객체 생성
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    /**
     * JWT 생성 메서드
     * @param username 로그인한 사용자 이름
     * @return 생성된 JWT 문자열
     */
    public String createToken(String username) {
        return Jwts.builder() // JWT 빌더 객체 반환 (메서드 체이닝 가능)
                .setSubject(username) // JWT의 payload에 'username' 값을 subject로 저장
                .setIssuedAt(new Date()) // 토큰의 발급 시간 (iat)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))// 만료 시간(EXP)
                .signWith(key, SignatureAlgorithm.HS256) // 키와 알고리즘으로 서명(signature)
                .compact(); // 최종적으로 JWT 문자열로 직렬화(base64 인코딩 포함!)
    }

    /**
     * JWT에서 사용자 이름 추출
     * @param token 클라이언트로부터 전달받은 JWT(쿠키)
     * @return 토큰의 subject(사용자 이름)
     */
    public String getUsername(String token) {
        return Jwts.parserBuilder() // JWT 파서 생성기
                .setSigningKey(key) // 검증에 사용할 키 설정
                .build() // 파서 객체 생성
                .parseClaimsJws(token) // 토큰 파싱 및 서명을 검증
                .getBody() // JWT의 payload(claims) 부분 추출
                .getSubject(); // 'subject' 필드 값 반환 (==username)
    }

    /**
     * JWT의 유효성 검사(형식 오류, 만료 여부 등 확인)
     * @param token 클라이언트가 보낸 JWT
     * @return 유효한 토큰이면 true, 그렇지 않으면 false
     */
    public boolean validateToken(String token) {
        try {
            // 파싱 시 파서 객체를 생성해 서명을 검증하며 만료 확인 등이 함께 진행된다.
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            // 서명 오류나 만료, 형식에 오류가 있다면 false 반환
            return false;
        }
    }
}

/**
 * Jwts.builder()
 * JWT 객체 생성을 시작하는 static 메서드. Header + Payload + Signature 구성 가능
 * .setSubject()
 * payload에 들어가는 표준 필드. 사용자의 식별자(ID 등)를 저장
 * .setIssuedAt()
 * 발급 시각(iat) 필드 설정
 * .setExpiration()
 * 만료 시각(exp) 설정
 * .signWith(key, alg)
 * JWT에 서명(암호화) 설정. 위조 방지 역할
 * .compact()
 * JWT 문자열 생성 (header.payload.signature 포맷)
 * .parserBuilder()
 * 받은 JWT를 파싱할 수 있도록 도와주는 빌더
 * .getBody()
 * payload 영역(claims)을 가져오기
 * .getSubject()
 * payload 중 sub 필드 값 반환 (username)
 */