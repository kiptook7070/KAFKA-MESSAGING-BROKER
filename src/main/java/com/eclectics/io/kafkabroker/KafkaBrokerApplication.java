package com.eclectics.io.kafkabroker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class KafkaBrokerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaBrokerApplication.class, args);
	}

@Bean
	CommandLineRunner runner(KafkaTemplate<String, String> kafkaTemplate){
		return args -> {
			kafkaTemplate.send("eclectics", " HELLO ECLECTICS");
		};
}
}
