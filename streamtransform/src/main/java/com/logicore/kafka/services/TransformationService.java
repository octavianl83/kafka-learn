package com.logicore.kafka.services;

import com.logicore.kafka.bindings.KafkaListenerBinding;
import com.logicore.kafka.model.IncomingMessage;
import com.logicore.kafka.transform.Transform;
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
public class TransformationService {

    @Autowired
    Transform transformFunction;
    @StreamListener("input-channel-1")
    @SendTo("output-channel-1")
    public KStream<String, String> process(KStream<String, IncomingMessage> input) {

        KStream<String, String> transformedKStream = input.filter((k, v) -> v.getCustomerType().equalsIgnoreCase("gold")).
                mapValues(v -> transformFunction.getTransformedMessage(v));

        transformedKStream.foreach((k,v) -> log.info(String.format("Message Transformed: key: %s, Value: %s", k, v)));

        return transformedKStream;

    }


}
