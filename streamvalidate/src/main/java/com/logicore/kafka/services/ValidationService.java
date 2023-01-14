package com.logicore.kafka.services;

import com.logicore.kafka.bindings.KafkaListenerBinding;
import com.logicore.kafka.model.IncomingMessage;
import com.logicore.kafka.validate.Validate;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Predicate;
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
//    @SendTo({"output-channel-1","output-channel-2","output-channel-3"})
    public void process(KStream<String, IncomingMessage> input) {

        input.foreach((k,v) -> log.info(String.format("Message Validated: key: %s, Value: %s", k, v)));

        //Send stream on a specific kafka topic without having to define binding for output channel
        input.to("flowengine");
        input.to("rediscache");
        input.to("database");

//        input.filter((k,v) -> !isValidMessage).to("error-topic");
//        input.filter((k,v) -> isValidMessage).to("good-payment-topic");
//        return input
//                .filter((key, value) -> value.getCustomerType("GOLD"))
//                .transform(() -> new TopicRouterTransformer());

    }

//Method that is branching based on soem decision using predicated of kstream
//When processing error we send using with .to() method to error topic
//    @StreamListener("input-channel-1")
//    @SendTo({"output-rulengine","output-transform","output-flow"})
//    public void process(KStream<String, IncomingMessage> input) {
//
//        input.foreach((k,v) -> log.info(String.format("Message Validated: key: %s, Value: %s", k, v)));
////
//        input.filter((k,v) -> !isValidMessage).to("error-topic");

//          Predicate<String, String> IsRulengineKsStream = input.filter((k, v) -> v.eq("rulengine"));
//          Predicate<String, String> IsTransformKsStream = input.filter((k, v) -> v.eq("transform"));
//          Predicate<String, String> IsFlowProcessorKsStream = input.filter((k, v) -> v.eq("flow"));
//          return input.branch(IsRulengineKsStream, IsTransformKsStream, IsFlowProcessorKsStream);
//    }

}
