package com.chatapp.chat_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chatapp.chat_backend.entity.User;

/**
 * User 엔티티에 대한 데이터베이스 작업을 수행하는 JPA 리포지토리입니다.
 * JpaRepository<User, Long> 을 상속하여 기본 CRUD 메서드들을 제공합니다.
 *
 * 추가 메서드:
 * - findByUsername(String username): 사용자 이름으로 조회
 *
 * Spring Data JPA는 메서드 이름만으로도 자동 구현을 지원합니다.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    // username으로 검색하는 메서드도 추가 가능
    User findByUsername(String username);
}
