package com.logicore.kafka.bindings;

import com.logicore.kafka.model.IncomingMessage;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

public interface KafkaListenerBinding {

    @Input("input-channel-1")
    KStream<String, IncomingMessage> inputStream1();

    @Output("output-channel-1")
    KStream<String, IncomingMessage> outputStream1();

//    @Input("input-channel-2")
//    KStream<String, IncomingMessage> inputStream2();
//
//    @Output("output-channel-2")
//    KStream<String, IncomingMessage> outputStream2();
}
