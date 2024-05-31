package com.bosch.datasynchronization.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Choice {

    private int     index;
    private Message message;

    @JsonProperty("finish_reason")
    private String finishReason;

    public String getFinishReason() {
        return finishReason;
    }

    public int getIndex() {
        return index;
    }

    public Message getMessage() {
        return message;
    }

    public void setFinishReason( String finishReason ) {
        this.finishReason = finishReason;
    }

    public void setIndex( int index ) {
        this.index = index;
    }

    public void setMessage( Message message ) {
        this.message = message;
    }
}