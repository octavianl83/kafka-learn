package com.logicore.kafka.model;

import lombok.Data;

@Data
public class IncomingMessage {

    private String key;
    private String value;
    private String status;
    private String customerType;
    private String customerName;
    private String customerAddress;
    private String topic;
}
