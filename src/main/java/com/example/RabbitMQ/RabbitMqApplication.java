package com.example.RabbitMQ;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RabbitMqApplication {

	@Value("${spring.rabbitmq.host}")
	private String HOST;

	@Value("${spring.rabbitmq.port}")
	private int PORT;

	@Value("${spring.rabbitmq.virtual-host}")
	private String VIRTUAL_HOST;

	@Value("${spring.rabbitmq.username}")
	private String USERNAME;

	@Value("${spring.rabbitmq.password}")
	private String PASSWORD;

	//Bean dùng để connection đến rabbitMQ. Có nhiều loại connection
	@Bean
	public ConnectionFactory connectionFactory(){
		var connectionFactory = new CachingConnectionFactory();
		connectionFactory.setUsername(USERNAME);
		connectionFactory.setPassword(PASSWORD);
		connectionFactory.setHost(HOST);
		connectionFactory.setVirtualHost(VIRTUAL_HOST);

		return connectionFactory;
	}

	//Bean converter from json in rabbitmq to java class
	@Bean
	public MessageConverter messageConverter(){
		ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
		return new Jackson2JsonMessageConverter((objectMapper));
	}


	public static void main(String[] args) {
		SpringApplication.run(RabbitMqApplication.class, args);
	}

}
