package com.kopo.learning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 이 파일이 실제 서버를 구동시키는 진입점(Entry Point)입니다.
// 서버를 실행할 때 가장 먼저 실행되는 파일
@SpringBootApplication
public class LearningApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningApplication.class, args);
	}

}
