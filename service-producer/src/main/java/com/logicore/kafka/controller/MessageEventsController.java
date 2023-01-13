package com.logicore.kafka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.logicore.kafka.model.IncomingMessage;
import com.logicore.kafka.producer.MessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@Slf4j
public class MessageEventsController {


    @Autowired
    MessageProducer messageProducer;

    @PostMapping("/v1/message")
    public ResponseEntity<IncomingMessage> postIncomingMessage(@RequestBody IncomingMessage message) throws JsonProcessingException {
        //invoke kafka producer
        log.info("before sendMessage");
        messageProducer.sendMessage(message);
        log.info("after sendMessage");
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @PostMapping("/v1/message_sync")
    public ResponseEntity<IncomingMessage> postIncomingMessageSync(@RequestBody IncomingMessage message) throws JsonProcessingException, ExecutionException, InterruptedException {
        //invoke kafka producer
        log.info("before sendMessage");
        SendResult<String, String> sendResult = messageProducer.sendMessageSync(message);
        log.info("sendResult {}", sendResult.toString());
        log.info("after sendMessage");
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }


    @PostMapping("/v1/message_producer")
    public ResponseEntity<IncomingMessage> postIncomingMessageProducer(@RequestBody IncomingMessage message) throws JsonProcessingException, ExecutionException, InterruptedException {
        //invoke kafka producer
        log.info("before sendMessage");
//        message.setIncomingMessageType(IncomingMessageType.NEW);
        messageProducer.sendMessage_approach_producer_record(message);
        log.info("after sendMessage");
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @PutMapping("/v1/message_producer")
    public ResponseEntity<?> putIncomingMessageProducer(@RequestBody IncomingMessage message) throws JsonProcessingException, ExecutionException, InterruptedException {
        //invoke kafka producer
        if (message.getKey() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please pass message key id");
        }

        log.info("before sendMessage");
//        message.setIncomingMessageType(IncomingMessageType.UPDATE);
        messageProducer.sendMessage_approach_producer_record(message);
        log.info("after sendMessage");
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }
}
