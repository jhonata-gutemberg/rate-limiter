package dev.gutemberg.rate.limiter.infrastructure.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ComponentScan({
		"dev.gutemberg.rate.limiter.domain",
		"dev.gutemberg.rate.limiter.entrypoints",
		"dev.gutemberg.rate.limiter.infrastructure"
})
public class RateLimiterApplication {
	public static void main(final String[] args) {
		SpringApplication.run(RateLimiterApplication.class, args);
	}
}
