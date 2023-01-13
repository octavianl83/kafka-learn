package com.logicore.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logicore.kafka.model.IncomingMessage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;

public class Main {

    static Long key = 1L;
    public static void main(String[] args) throws IOException, InterruptedException {
        int i = 0;
        while (i<100000) {
            IncomingMessage incomingMessage = generateNextMessage();
            String URL = "http://localhost:7100/v1/message_producer";
            sendRequest(incomingMessage, URL);
            i++;
            Thread.sleep(10);
        }
    }



    public static IncomingMessage generateNextMessage() {
        key = key + 1;
        String status = getRandomSTATUS();
        String customerType = getRandomCustomerType();
        String value = getRandomString();
        IncomingMessage incomingMessage = new IncomingMessage();
        incomingMessage.setKey(key.toString());
        incomingMessage.setValue(value);
        incomingMessage.setStatus(status);
        incomingMessage.setCustomerType(customerType);
        incomingMessage.setCustomerAddress("A");
        incomingMessage.setCustomerName("B");
        return incomingMessage;
    }

    public static String getRandomString() {
        final String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            sb.append(characters.charAt(rand.nextInt(characters.length())));
        }
        return sb.toString();
    }
    public static String getRandomSTATUS() {
        String[] messages = {"NEW", "CLOSED", "INCOMING", "UPDATED"};
        Random rand = new Random();
        int index = rand.nextInt(messages.length);
        return messages[index];
    }

    public static String getRandomCustomerType() {
        String[] messages = {"GOLD", "SILVER", "PLATINUM"};
        Random rand = new Random();
        int index = rand.nextInt(messages.length);
        return messages[index];
    }

    public static void sendRequest(Object object, String url) throws IOException, InterruptedException {
        // Create an HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Convert the Java object to JSON
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(object);

        // Create a POST request with a JSON body
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        // Send the request and get the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Print the response status and body
        System.out.println(response.statusCode());
        System.out.println(response.body());
    }
}
