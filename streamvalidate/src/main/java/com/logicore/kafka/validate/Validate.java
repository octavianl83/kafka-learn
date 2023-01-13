package com.logicore.kafka.validate;

import com.logicore.kafka.model.IncomingMessage;
import org.springframework.stereotype.Component;

@Component
public class Validate {

    public IncomingMessage getTransformedMessage(IncomingMessage incomingMessage) {
        incomingMessage.setStatus("VALIDATED");
        return incomingMessage;
    }
}
