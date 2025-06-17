package com.chatapp.chat_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
 *
 * 현재는 아래와 같은 기능을 제공합니다:
 * - 회원가입 (POST /api/users/signup)
 * - 로그인 (POST /api/users/login)
 * - 사용자 전체 조회 (GET /api/users)
 *
 * 보안 및 인증:
 * - Spring Security는 현재 모든 요청을 허용하도록 설정되어 있음 (SecurityConfig 참고)
 * - 로그인 성공 시 JWT 토큰을 발급하고, 이후 인증 기반 시스템에서 활용 가능
 *
 * 기술 요소 요약:
 * - @RestController: 모든 메서드의 결과가 HTTP 응답으로 처리됨
 * - @RequestMapping("/api/users"): 해당 컨트롤러의 모든 API는 /api/users 경로에서 제공됨
 * - @RequestBody: JSON → DTO 자동 매핑
 * - @Valid: DTO 필드의 유효성 검사 수행 (@NotBlank 등)
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
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

    /**
     * 회원 가입 API
     * 
     * @param requestDTO: 클라이언트가 보낸 회원가입 요청(JSON)-> DTO로 매핑
     * @return 중복 검사 후 성공 메시지나 실패 메시지
     * 
     *         - @Valid: DTO에 선언된 유효성 검사(@NotBlank 등)를 자동으로 적용
     */
    @PostMapping("/signup")
    public String signup(@Valid @RequestBody UserRequestDTO requestDTO) {
        // 아이디 중복 체크!
        if (userRepository.findByUsername(requestDTO.getUsername()) != null) {
            return "이미 존재하는 아이디입니다.";
        }

        // 비밀번호 암호화 후 DB에 저장
        User user = new User();
        user.setUsername(requestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

        userRepository.save(user);
        return "회원가입 성공.";
    }

    /**
     * 로그인 API
     * 
     * @param requestDTO: 클라이언트가 보낸 로그인 정보
     * @return 성공 시 JWT 토큰 반환, 실패 시 에러 메시지 반환
     * 
     *         - 사용자 존재 여부 확인
     *         - 비밀번호 일치 여부 확인
     *         - JWT 발급
     */
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
}

/**
 * @RequestBody:
 *               - 클라이언트가 보낸 JSON 형식의 HTTP 요청 본문(body)을 자바 객체로 자동 변환해주는 애너테이션이다.
 *               - 일반적으로 POST, PUT 요청과 함께 사용되며, 요청 데이터를 DTO나 엔티티 객체로 매핑할 때 사용된다.
 *               - Spring MVC의 HttpMessageConverter가 내부적으로 JSON을 객체로
 *               역직렬화(deserialization)한다.
 * 
 *               사용 예시:
 *               클라이언트가 아래와 같은 JSON 요청을 보냈을 때
 *               {
 *               "username": "woogi",
 *               "password": "1234"
 *               }
 * 
 *               컨트롤러에서 @RequestBodt가 선언되어있다면 자동으로 해당하는 DTO의 클래스의 필드에 자동으로 패밍한다.
 * 
 *               1. 클라이언트가 Content-Type: application/json 으로 요청을 보냄
 *               2. Spring은 @RequestBody가 붙은 파라미터를 보고
 *               3. 내부적으로 MappingJackson2HttpMessageConverter라는 클래스를 사용해
 *               4. 요청 본문의 JSON을 DTO 객체(UserRequestDTO)로 변환함
 * 
 *               HTTP Request
 *               └─> JSON Body
 *               └─> {"username": "woogi", "password": "1234"}
 *               └─> @RequestBody UserRequestDTO
 *               └─> Spring이 Jackson(ObjectMapper)을 사용해 자동 역직렬화
 *               └─> new UserRequestDTO("woogi", "1234") 와 유사한 객체 생성
 * 
 *               ⚠️ 주의사항:
 *               - 매핑 대상 객체에 기본 생성자와 getter/setter가 반드시 있어야 합니다.
 *               - 요청 Content-Type은 application/json 이어야 하며, 유효성 검사를 위해 @Valid와
 *               함께 자주 사용됩니다.
 */

/**
 * @Valid
 * - 클라이언트의 JSON 요청이 DTO 객체로 매핑된 후, 해당 필드 값들이 유효한지를 검사한다.
 * - 예: DTO 클래스의 필드에 @NotBlank가 붙어 있다면, @Valid는 null 또는 공백("") 여부를 자동으로 검사하여 유효하지 않으면 예외를 발생시킨다.
 *
 * @RequestBody
 * - 클라이언트가 보낸 JSON 데이터를 Java 객체(DTO)로 자동 변환해준다.
 * - 이 과정은 Spring의 HttpMessageConverter가 처리하며, 내부적으로 Jackson(ObjectMapper)을 사용하여 JSON → 객체 매핑이 수행된다.
 * 
 * 정리하면, 클라이언트 → JSON → @RequestBody → DTO 객체로 변환되고,
 * 그 변환된 객체 → @Valid → 유효성 검사(@NotBlank 등)까지 자동으로 수행된다.
 */
