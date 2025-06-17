package com.chatapp.chat_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * ChatBackendApplication
 * 이 클래스는 Spring Boot 애플리케이션의 진입점(entry point)이다.
 * 자바의 main 메서드를 통해 스프링 부트를 실행한다.
 * 
 * @SpringBootApplication 애너테이션은 다음 세가지를 포함한다.
 * 1. @Configuration: 이 클래스가 설정 클래스임을 나타낸다.
 * 2. @EnableAutoConfiguration: Spring Boot가 클래스패스를 보고 자동 설정을 적용하게 한다.
 * 3. @ComponentScan: 현재 패키지 및 하위 패키지에서 @Component, @Service, @Repository, @Controller 등을 찾아 등록.
 * 이러한 하나의 애너테이션으로 프로젝트 전반의 구성을 자동화해 준다.
 */

@SpringBootApplication
public class ChatBackendApplication {

	/**
	 * main 애플리케이션을 시작하는 메서드읻.
	 * SpringApplication.run() 메서드는 Spring Boot 애플리에키션을 실행하고 내장 톰캣 서버를 띄워준다.
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(ChatBackendApplication.class, args);
	}

}
