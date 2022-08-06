package com.br.dronetrack.controller;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.dronetrack.config.RabbitConfig;
import com.br.dronetrack.model.DroneDTO;

@RestController
@RequestMapping("drone-track")
public class DroneController {

    @PostMapping
    public DroneDTO sendMessageQueue(@RequestBody DroneDTO drone){
    	 RabbitAdmin admin = new RabbitAdmin(RabbitConfig.getConnection());
         
    	 Queue droneQueue = new Queue("drone-tracker-receptor");
         admin.declareQueue(droneQueue);
         
         final String exchange = "exchange.drone";
         DirectExchange exchangeDrone = new DirectExchange(exchange);
         admin.declareExchange(exchangeDrone);
         
         admin.declareBinding(BindingBuilder.bind(droneQueue).to(exchangeDrone).with("drone-receptor-key"));
         
         RabbitTemplate template = new RabbitTemplate(RabbitConfig.getConnection());
         template.convertAndSend(exchange, "drone-receptor-key", drone);
        return drone;
    }
}
