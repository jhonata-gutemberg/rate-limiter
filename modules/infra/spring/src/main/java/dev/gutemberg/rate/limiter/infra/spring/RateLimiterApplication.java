package dev.gutemberg.rate.limiter.infra.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = {"dev.gutemberg.rate.limiter"})
public class RateLimiterApplication {
	public static void main(final String[] args) {
		SpringApplication.run(RateLimiterApplication.class, args);
	}
}
