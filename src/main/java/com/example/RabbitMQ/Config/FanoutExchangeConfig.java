package com.example.RabbitMQ.Config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class FanoutExchangeConfig {
    @Autowired
    private AmqpAdmin amqpAdmin;

    @Value("${rabbitmq.fanout.queue1}")
    private String FANOUT_QUEUE_1;

    @Value("${rabbitmq.fanout.queue2}")
    private String FANOUT_QUEUE_2;

    @Value("${rabbitmq.fanout.exchange}")
    private String FANOUT_EXCHANGE;

    Queue createFanoutQueue1() {
        return new Queue(FANOUT_QUEUE_1, true,false,false);
    }

    Queue createFanoutQueue2() {
        return new Queue(FANOUT_QUEUE_2, true,false,false);
    }

    FanoutExchange createFanoutExchange(){
        return new FanoutExchange(FANOUT_EXCHANGE, true, false);
    }

    Binding createFanoutBinding1() {
        return BindingBuilder.bind(createFanoutQueue1()).to(createFanoutExchange());
    }
    Binding createFanoutBinding2() {
        return BindingBuilder.bind(createFanoutQueue2()).to(createFanoutExchange());
    }

    @Bean
    public AmqpTemplate fanoutExchange(ConnectionFactory connectionFactory, MessageConverter messageConverter){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setExchange(FANOUT_EXCHANGE);

        return rabbitTemplate;
    }

    @PostConstruct
    public void init(){
        amqpAdmin.declareQueue(createFanoutQueue1());
        amqpAdmin.declareQueue(createFanoutQueue2());
        amqpAdmin.declareExchange(createFanoutExchange());
        amqpAdmin.declareBinding(createFanoutBinding1());
        amqpAdmin.declareBinding(createFanoutBinding2());
    }

}
