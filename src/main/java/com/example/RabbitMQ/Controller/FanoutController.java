package com.example.RabbitMQ.Controller;

import com.example.RabbitMQ.model.QueueObject;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class FanoutController {
    @Autowired
    private AmqpTemplate fanoutExchange;

    @PostMapping("/fanoutExchange")
    public ResponseEntity fanoutExchange() {
        QueueObject object = new QueueObject("fanoutExchange", LocalDateTime.now());
        fanoutExchange.convertAndSend(object);

        return ResponseEntity.ok(true);
    }
}
