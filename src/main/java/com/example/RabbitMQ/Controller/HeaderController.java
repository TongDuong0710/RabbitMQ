package com.example.RabbitMQ.Controller;

import com.example.RabbitMQ.model.QueueObject;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class HeaderController {
    @Autowired
    private AmqpTemplate headerExchange;

    @PostMapping("/header")
    public ResponseEntity fanoutExchange(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "debug", required = false) String debug,
            @RequestParam(value = "infor", required = false) String infor,
            @RequestParam(value = "warning", required = false) String warning) {
        QueueObject object = new QueueObject("headerExchange", LocalDateTime.now());
        MessageBuilder builder = MessageBuilder.withBody(object.toString().getBytes());
        if(error != null){
            builder.setHeader("error",error);
        }
        if(debug != null){
            builder.setHeader("debug",debug);
        }
        if(infor != null){
            builder.setHeader("infor",infor);
        }
        if(warning != null){
            builder.setHeader("warning",warning);
        }

        headerExchange.convertAndSend(builder.build());

        return ResponseEntity.ok(true);
    }
}
