package com.logicore.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.logicore.kafka.model.IncomingMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class MessageProducer {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

    String topic = "message";

    public void sendMessage(IncomingMessage message) throws JsonProcessingException {

        String key = message.getKey();
        String value = objectMapper.writeValueAsString(message.getValue());

        ListenableFuture<SendResult<String, String>> sendResultListenableFuture = kafkaTemplate.sendDefault(key, value);
        sendResultListenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                handleFailure(throwable);
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                handleSuccess(key, value, result);
            }
        });
    }

    public void sendMessage_approach_send(IncomingMessage message) throws JsonProcessingException {

        String key = message.getKey();
        String value = objectMapper.writeValueAsString(message.getValue());

        ListenableFuture<SendResult<String, String>> sendResultListenableFuture = kafkaTemplate.send("library-event", key, value);
        sendResultListenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                handleFailure(throwable);
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                handleSuccess(key, value, result);
            }
        });
    }

    public void sendMessage_approach_producer_record(IncomingMessage message) throws JsonProcessingException {

        String key = message.getKey();
        String value = objectMapper.writeValueAsString(message);

        ProducerRecord<String, String> producerRecord = buildProducerRecord(key, value, topic);

        ListenableFuture<SendResult<String, String>> sendResultListenableFuture = kafkaTemplate.send(producerRecord);
        sendResultListenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                handleFailure(throwable);
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                handleSuccess(key, value, result);
            }
        });
    }

    private ProducerRecord<String, String> buildProducerRecord(String key, String value, String topic) {

        List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));

        return new ProducerRecord<>(topic, null, key, value, recordHeaders);
    }

    public void handleSuccess(String key, String value, SendResult<String, String> result) {
        log.info("Message sent successfully with key {} and value {} and message {} partition is {}", key, value, "da", result.getRecordMetadata().partition());
    }

    public void handleFailure(Throwable ex) {
        log.error("Error sending the message and the exception is {}", ex.getMessage());
        try {
            throw ex;
        }catch (Throwable throwable) {
            log.error("Error in OnFailure {}", throwable.getMessage());
        }
    }

   public SendResult<String, String> sendMessageSync(IncomingMessage message) throws JsonProcessingException, ExecutionException, InterruptedException {

       String key = message.getKey();
       String value = objectMapper.writeValueAsString(message.getValue());

       SendResult<String, String> sendResult = null;

       try {
           sendResult = kafkaTemplate.sendDefault(key, value).get(1, TimeUnit.SECONDS);
       } catch (InterruptedException | ExecutionException e) {
           log.error("InterruptedException | ExecutionException Error sending the message and the exception is {}", e.getMessage());
           throw e;
       } catch (Exception e) {
           log.error("Exception Error sending the message and the exception is {}", e.getMessage());
           e.printStackTrace();
       }

       return sendResult;

   }

}
