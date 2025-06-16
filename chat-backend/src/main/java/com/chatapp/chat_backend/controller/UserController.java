package com.chatapp.chat_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatapp.chat_backend.entity.User;
import com.chatapp.chat_backend.repository.UserRepository;

/**
 * 사용자 관련 REST API를 제공하는 컨트롤러입니다.
 * 현재는 테스트 목적으로 회원 등록 및 전체 조회 기능만 제공됩니다.
 *
 * 엔드포인트:
 * - POST /api/users: 새로운 사용자 등록
 * - GET /api/users: 전체 사용자 목록 조회
 *
 * 추후에는 로그인, 인증, 프로필 조회 등 기능이 추가될 예정입니다.
 *
 * ⚠️ 현재는 인증 없이 누구나 호출 가능 (SecurityConfig 참고)
 */

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    // 생성자 주입 방식
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // POST: 사용자 저장
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // GET: 모든 사용자 조회
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
