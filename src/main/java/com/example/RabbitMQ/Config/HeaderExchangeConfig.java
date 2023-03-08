package com.example.RabbitMQ.Config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class HeaderExchangeConfig {
    @Autowired
    private AmqpAdmin amqpAdmin;

    @Value("${rabbitmq.header.queue1}")
    private String HEADER_QUEUE_1;
    @Value("${rabbitmq.header.queue2}")
    private String HEADER_QUEUE_2;
    @Value("${rabbitmq.header.exchange}")
    private String HEADER_EXCHANGE;

    Queue createHeaderQueue1() {
        return new Queue(HEADER_QUEUE_1, true,false,false);
    }
    Queue createHeaderQueue2() {
        return new Queue(HEADER_QUEUE_2, true,false,false);
    }
    HeadersExchange createTopicExchange(){
        return new HeadersExchange(HEADER_EXCHANGE, true, false);
    }
    Binding createHeaderBinding1() {
        return BindingBuilder.bind(createHeaderQueue1())
                .to(createTopicExchange())
                .whereAll("error","debug")
                .exist();
    }
    Binding createHeaderBinding2() {
        return BindingBuilder.bind(createHeaderQueue2())
                .to(createTopicExchange())
                .whereAny("infor","warning")
                .exist();
    }
    @Bean
    public AmqpTemplate headerExchange(ConnectionFactory connectionFactory, MessageConverter messageConverter){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setExchange(HEADER_EXCHANGE);

        return rabbitTemplate;
    }
    @PostConstruct
    public void init(){
        amqpAdmin.declareQueue(createHeaderQueue1());
        amqpAdmin.declareQueue(createHeaderQueue2());
        amqpAdmin.declareExchange(createTopicExchange());
        amqpAdmin.declareBinding(createHeaderBinding1());
        amqpAdmin.declareBinding(createHeaderBinding2());
    }
}
