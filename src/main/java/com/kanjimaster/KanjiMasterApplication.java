package com.kanjimaster;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class KanjiMasterApplication {
	public static void main(String[] args) {
		SpringApplication.run(KanjiMasterApplication.class, args);
	}
}
