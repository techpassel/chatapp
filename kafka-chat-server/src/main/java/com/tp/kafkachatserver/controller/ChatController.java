package com.tp.kafkachatserver.controller;

import com.tp.kafkachatserver.constant.KafkaConstant;
import com.tp.kafkachatserver.model.Message;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

@RestController
@AllArgsConstructor
public class ChatController {
    // Constructor based dependency injection. Instead, we could have used field based dependency injection also using
    // '@Autowired' annotation. But Constructor based dependency injection should be always preferred.
    private final KafkaTemplate<String, Message> kafkaTemplate;

    //Api where user messages will be delivered.
    @PostMapping(value = "/api/send", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> sendMessage(@RequestBody Message message){
        message.setTimestamp(LocalDateTime.now().toString());
        ListenableFuture<SendResult<String, Message>> future =
                kafkaTemplate.send(KafkaConstant.KAFKA_TOPIC, message);
        try {
            SendResult<String, Message> response = future.get();
            System.out.println("Record metadata : "+response.getRecordMetadata());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InterruptedException |ExecutionException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // MessageListener will call this api and pass the message to it.
    // This would broadcast the Message to all the clients(UI side) who have subscribed to this topic.
    @MessageMapping("/sendMessage")
    @SendTo("/topic/group")
    public Message broadcastGroupMessage(@Payload Message message) {
        //Sending this message to all the subscribers
        System.out.println("Sending messages to all subscribers - "+message);
        return message;
    }
    //Configuration for Websockets is set on WebSocketConfig file
}
