package com.logicore.kafka.transform;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.logicore.kafka.model.IncomingMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Transform {

    public String getTransformedMessage(IncomingMessage incomingMessage) {
        incomingMessage.setStatus("PROCESSED");

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(incomingMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
