package com.chatapp.chat_backend.controller;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatapp.chat_backend.dto.LoginRequestDTO;
import com.chatapp.chat_backend.dto.UserRequestDTO;
import com.chatapp.chat_backend.entity.User;
import com.chatapp.chat_backend.repository.UserRepository;
import com.chatapp.chat_backend.util.JwtUtil;

import jakarta.validation.Valid;

/**
 * 사용자 관련 REST API를 제공하는 컨트롤러입니다.
 * 현재는 테스트 목적으로 회원 등록 및 전체 조회 기능만 제공됩니다.
 *
 * 엔드포인트:
 * - POST /api/users: 새로운 사용자 등록
 * - GET /api/users: 전체 사용자 목록 조회
 *
 * 현재는 로그인 기능만 구현.
 *
 * ⚠️ 현재는 인증 없이 누구나 호출 가능 (SecurityConfig 참고)
 * 
 * 
 */

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 생성자를 통한 의존성 주입
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    

    // GET: 모든 사용자 조회
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

        // 로그인 요청
    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequestDTO request) {
        User user = userRepository.findByUsername(request.getUsername());

        if (user == null) {
            return "존재하지 않는 사용자입니다.";
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return "비밀번호가 일치하지 않습니다.";
        }

        // JWT 발급
        String token = jwtUtil.createToken(user.getUsername());
        return "로그인 성공! JWT: " + token;
    }

    @PostMapping("/signup")
    public String postMethodName(@Valid @RequestBody UserRequestDTO requestDTO) {
        // 아이디 중복 체크!
        if(userRepository.findByUsername(requestDTO.getUsername()) != null) {
            return "이미 존재하는 아이디입니다.";
        }

        // 비밀번호 암호화 후 DB에 저장
        User user = new User();
        user.setUsername(requestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

        userRepository.save(user);
        return "회원가입 성공.";
    }
    
}
