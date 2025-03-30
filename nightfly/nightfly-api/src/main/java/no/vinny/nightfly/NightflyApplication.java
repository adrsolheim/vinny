package no.vinny.nightfly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
public class NightflyApplication {

	public static void main(String[] args) {
		SpringApplication.run(NightflyApplication.class, args);
	}

}
