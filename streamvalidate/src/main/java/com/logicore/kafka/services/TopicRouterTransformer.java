package com.logicore.kafka.services;

import com.logicore.kafka.model.IncomingMessage;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.springframework.kafka.support.KafkaHeaders;

class TopicRouterTransformer implements Transformer<String, String, KeyValue<String, IncomingMessage>> {
    private ProcessorContext context;

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public KeyValue<String, IncomingMessage> transform(String key, String value) {
        String topic = "default-topic";
        if (value.contains("high-priority")) {
            topic = "high-priority-topic";
        } else if (value.contains("medium-priority")) {
            topic = "medium-priority-topic";
        }
        // set the topic header
        context.headers().add(KafkaHeaders.TOPIC, topic.getBytes());
        return new KeyValue<>(key, new IncomingMessage());
    }

    @Override
    public void close() { }
}