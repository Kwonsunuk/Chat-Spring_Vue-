package com.chatapp.chat_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 로그인 요청 시 username, password를 담는 DTO
 * 로그인 요청시 클라이언트가 보내느 데이터(username, password)를 담는 DTO(Data Transfer Object)이다
 * 주로 컨트롤러에서 @RequestBody와 함께 사용한다.
 * 
 * @Getter, @Setter: Lombok 애너테이션으로, 각 필드에 대해 getter/setter 메서드를 자동 생성해준다.
 * @NotBlank: 유효성 검사용 애너테이션으로, 빈 문자열이나 null이 들어오는 것을 방지한다.
 *            message 속성으로 클라이언트에게 보여줄 에러 메시지를 지정할 수 있다.
 * @NotBlank는 HTML 폼이나 JSON에서 공백 문자열(””)까지 체크해 주기 때문에, 로그인 시 필수 입력을 강제할 때 매우 유용하다.
 */
@Getter
@Setter
public class LoginRequestDTO {
    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}
