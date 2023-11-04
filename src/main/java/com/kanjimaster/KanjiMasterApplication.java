package com.kanjimaster;

import com.kanjimaster.initializer.KanjiDataInitializer;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class KanjiMasterApplication {
	private final KanjiDataInitializer kanjiDataInitializer;

	public KanjiMasterApplication(KanjiDataInitializer kanjiDataInitializer) {
		this.kanjiDataInitializer = kanjiDataInitializer;
	}

	public static void main(String[] args) {
		SpringApplication.run(KanjiMasterApplication.class, args);
	}

	@Bean
	public SmartInitializingSingleton importProcessor() {
		return () -> {
			try {
				kanjiDataInitializer.initialize();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};

	}
}
