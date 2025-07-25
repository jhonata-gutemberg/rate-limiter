package dev.gutemberg.rate.limiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RateLimiterApplication {
	public static void main(final String[] args) {
		SpringApplication.run(RateLimiterApplication.class, args);
	}
}
