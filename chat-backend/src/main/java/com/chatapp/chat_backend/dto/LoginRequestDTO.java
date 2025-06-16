package com.chatapp.chat_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/*
 * 로그인 요청 시 username, password를 담는 DTO
 */
@Getter
@Setter
public class LoginRequestDTO {
    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}
