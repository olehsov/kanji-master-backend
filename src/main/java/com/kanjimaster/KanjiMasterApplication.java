package com.kanjimaster;


import com.kanjimaster.config.OpenAiConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
@EnableConfigurationProperties({OpenAiConfigurationProperties.class})
public class KanjiMasterApplication {
	public static void main(String[] args) {
		SpringApplication.run(KanjiMasterApplication.class, args);
	}
}
