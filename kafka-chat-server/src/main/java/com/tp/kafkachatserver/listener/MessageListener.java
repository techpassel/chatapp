package com.tp.kafkachatserver.listener;

import com.tp.kafkachatserver.constant.KafkaConstant;
import com.tp.kafkachatserver.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {
    @Autowired
    SimpMessagingTemplate template;

    //It will listen for the Kafka queue messages.
    @KafkaListener(
            topics = KafkaConstant.KAFKA_TOPIC,
            groupId = KafkaConstant.CONSUMER_GROUP_ID
    )
    public void listen(Message message) {
        System.out.println("Sending message via kafka listener.."+message);
        //It will convert the message and send that to WebSocket topic.

        template.convertAndSend("/topic/group", message);
    }

}
