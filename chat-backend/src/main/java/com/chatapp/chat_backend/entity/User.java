package com.chatapp.chat_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * User 엔티티 클래스는 DB의 users 테이블과 매핑되는 클래스입니다.
 * 회원 정보를 저장하기 위한 도메인 객체로, JPA가 이 객체를 기반으로 자동으로 테이블을 생성/관리합니다.
 */
@Entity // JPA가 이 클래스가 엔티티임을 인식하게 해주는 애너테이션 (필수)
@Table(name = "users") // DB에서 이 클래스가 매핑될 테이블명을 "users"로 지정

@Getter // Lombok: 모든 필드에 대해 Getter 메서드 자동 생성
@Setter // Lombok: 모든 필드에 대해 Setter 메서드 자동 생성
@NoArgsConstructor // Lombok: 파라미터 없는 기본 생성자 자동 생성 (JPA에서 필수로 요구함)
public class User {

    @Id // 이 필드가 기본키(PK)임을 나타냄
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 기본키 자동 생성 전략 중 "IDENTITY"는 DB에서 auto_increment 형태로 생성됨
    private Long id;

    @Column(nullable = false, unique = true)
    // username은 null이 될 수 없고, 중복도 허용되지 않도록 제약 조건을 설정함
    private String username;

    @Column(nullable = false)
    // password는 null이 될 수 없음. 실무에서는 추가적인 암호화가 적용됨 (ex. BCrypt)
    private String password;
}






























































