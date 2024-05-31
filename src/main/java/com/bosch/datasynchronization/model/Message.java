package com.bosch.datasynchronization.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {

    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    private String role;
    private String content;

    public String getContent() {
        return content;
    }

    public String getRole() {
        return role;
    }

    public void setContent( String content ) {
        this.content = content;
    }

    public void setRole( String role ) {
        this.role = role;
    }
}