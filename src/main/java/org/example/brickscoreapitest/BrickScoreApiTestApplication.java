package org.example.brickscoreapitest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 애플리케이션 시작 클래스
 * 이 클래스가 실행되면:
 * 1. 내장 Tomcat 서버 실행
 * 2. 모든 Bean 스캔
 * 3. Controller / Service / Repository 등록
 */
@SpringBootApplication
public class BrickScoreApiTestApplication {

    public static void main(String[] args) {

        // Spring Boot 애플리케이션 실행
        SpringApplication.run(BrickScoreApiTestApplication.class, args);
    }

}
