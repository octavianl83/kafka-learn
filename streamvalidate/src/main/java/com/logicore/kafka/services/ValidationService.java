package com.logicore.kafka.services;

import com.logicore.kafka.bindings.KafkaListenerBinding;
import com.logicore.kafka.model.IncomingMessage;
import com.logicore.kafka.validate.Validate;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@EnableBinding(KafkaListenerBinding.class)
public class ValidationService {


    @Autowired
    Validate validateFunction;
    @StreamListener("input-channel-1")
    @SendTo("#{headers.topic}")
    public void process(KStream<String, IncomingMessage> input) {

        input.foreach((k,v) -> log.info(String.format("Message Validated: key: %s, Value: %s", k, v)));
//
//        return input
//                .filter((key, value) -> value.getCustomerType("GOLD"))
//                .transform(() -> new TopicRouterTransformer());

    }

}
