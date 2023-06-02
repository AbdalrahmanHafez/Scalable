package com.example.productApp;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

@EnableMongoRepositories
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class ProductAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductAppApplication.class, args);
	}


//	@Bean
//	CommandLineRunner commandLineRunner(KafkaTemplate<String , Map<String,Object>> kafkaTemplate){
//		Map<String,Object> map= new HashMap<>();
//		map.put("test" , "test");
//		return args -> {
//			kafkaTemplate.send("productApp" , map);
//		};
//	}
}
