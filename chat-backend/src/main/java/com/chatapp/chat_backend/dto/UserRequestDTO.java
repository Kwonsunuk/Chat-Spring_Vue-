package com.chatapp.chat_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원가입 요청을 받을 때 사용하는 DTO (Data Transfer Object).
 *
 * ✅ 사용 목적:
 * - 클라이언트(프론트엔드)로부터 전달받은 회원가입 데이터(username, password)를 담는다.
 * - 컨트롤러에서 @RequestBody로 매핑되며, JSON 요청 본문(body)의 데이터를 자동으로 이 객체에 바인딩해준다.
 * - @Valid와 함께 사용하여 입력값 유효성 검사(Validation)를 적용할 수 있다.
 *
 * ✅ Entity와 분리한 이유:
 * - 보안 측면: Entity는 데이터베이스 구조와 직접 연결되어 있기 때문에 외부 요청과 직접 연결되면 민감한 데이터(예: DB ID, 권한 정보 등)가 노출될 위험이 있다.
 * - 확장성 측면: DTO는 요청/응답 형식을 자유롭게 커스터마이징할 수 있어서, API의 요구사항에 따라 유연하게 대응할 수 있다.
 * - 단일 책임 원칙(SRP): Entity는 DB와의 매핑 역할만, DTO는 요청/응답 데이터 전송 역할만 하도록 역할을 분리하여 유지보수성을 높인다.
 *
 * ✅ 어노테이션 설명:
 * - @Getter, @Setter: Lombok이 자동으로 getter/setter 메서드를 생성해줌으로써 코드의 간결성을 유지한다.
 * - @NotBlank: 문자열이 null이거나 "" (빈 문자열), 공백(" ")일 경우에도 유효하지 않다고 판단. → 필수 입력 필드에 사용.
 *              예: username이 "    "처럼 공백만 있어도 유효하지 않은 값으로 처리됨.
 */
@Setter
@Getter
public class UserRequestDTO {

    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}

