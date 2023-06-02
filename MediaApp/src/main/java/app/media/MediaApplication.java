package app.media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import app.media.interceptors.CustomInterceptor;

// @EnableScheduling
// @EnableRabbit
// @SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@SpringBootApplication()
@EnableMongoRepositories
public class MediaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MediaApplication.class, args);
	}

}
