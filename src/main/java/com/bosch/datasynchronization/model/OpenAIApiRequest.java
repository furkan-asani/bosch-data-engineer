package com.bosch.datasynchronization.model;

import java.util.List;


public class OpenAIApiRequest {

    private String        model;
    private List<Message> messages;
    private double        temperature;

    public OpenAIApiRequest(String model, List<Message> messages, double temperature ) {
        this.model = model;
        this.messages = messages;
        this.temperature = temperature;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public String getModel() {
        return model;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setMessages( List<Message> messages ) {
        this.messages = messages;
    }

    public void setModel( String model ) {
        this.model = model;
    }

    public void setTemperature( double temperature ) {
        this.temperature = temperature;
    }

    public record Message(String role, String content) {

    }
}