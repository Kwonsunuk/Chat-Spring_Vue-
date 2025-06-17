package com.chatapp.chat_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chatapp.chat_backend.entity.User;

/**
 * UserRepository는 사용자(User) 엔티티에 대한 데이터베이스 작업을 수행하는 인터페이스이다.
 * Spring Data JPA에서 제공하는 JpaRepository에서 JpaRepository<User, Long>을 사용해 상속받아 기본적이 CRUD 메서드(create, read, update, delete)를 자동으로 제공한다.
 * 
 * User findByUsername(String username) Spring Data JPA의 네이밍 규ㅜ칙에 따라 메서드 이름만으로 SQL 쿼리를 자동으로 생성한다.
 * SELECT * FROM users WHERE username = ? 와 같은 쿼리를 자동으로 실행해준다.
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
