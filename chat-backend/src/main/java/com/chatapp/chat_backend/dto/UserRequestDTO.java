package com.chatapp.chat_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원가입 요청을 받을 때 사용하는 DTO.
 * 클라이언트의 입력값을 직접 Entity에 바인딩하지 않고,
 * 중간 DTO를 통해 입력 검증 및 보안을 강화함.
 */
@Setter
@Getter
public class UserRequestDTO {
    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}
