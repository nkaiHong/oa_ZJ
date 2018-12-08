package org.fkjava.hr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@ComponentScan("org.fkjava")
public class HumanResourcesConfig {

	public static void main(String[] args) {
		SpringApplication.run(HumanResourcesConfig.class, args);
	}
}
