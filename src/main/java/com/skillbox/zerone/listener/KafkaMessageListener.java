package com.skillbox.zerone.listener;

import com.skillbox.zerone.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaMessageListener {

    private final PersonService personService;

    @KafkaListener(topics = "${app.kafka.kafkaMessageTopicEmail}",
            groupId = "${app.kafka.kafkaMessageGroupId}",
            containerFactory = "kafkaMessageConcurrentKafkaListenerContainerFactory")
    public void listen(@Payload KafkaMessage kafkaMessage) {
        personService.changePersonEmail(kafkaMessage.getMessage(), kafkaMessage.getUserId());
    }

    @KafkaListener(topics = "${app.kafka.kafkaMessageTopicPassword}",
            groupId = "${app.kafka.kafkaMessageGroupId}",
            containerFactory = "kafkaMessageConcurrentKafkaListenerContainerFactory")
    public void listenPassword(@Payload KafkaMessage kafkaMessage) {
        personService.recoverPersonPassword(kafkaMessage.getMessage(), kafkaMessage.getUserId());
    }
}
