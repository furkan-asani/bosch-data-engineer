package com.bosch.datasynchronization.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "parsedResponses")
public class OpenAIChatCompletionResponse {

    @Id
    private String id;
    private String object;
    private long created;
    private List<Choice> choices;
    private Usage usage;

    public List<Choice> getChoices() {
        return choices;
    }

    public long getCreated() {
        return created;
    }

    public String getId() {
        return id;
    }

    public String getObject() {
        return object;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }

    @Override
    public String toString() {
        return "OpenAIChatCompletionResponse{" + "id='" + id + '\'' + ", object='" + object + '\'' + ", created=" + created + ", choices=" + choices + ", usage="
                + usage + '}';
    }

}


@JsonIgnoreProperties(ignoreUnknown = true)
class Usage {

    @JsonProperty("prompt_tokens")
    private int promptTokens;
    @JsonProperty("completion_tokens")
    private int completionTokens;
    @JsonProperty("total_tokens")
    private int totalTokens;

    public int getCompletionTokens() {
        return completionTokens;
    }

    public int getPromptTokens() {
        return promptTokens;
    }

    public int getTotalTokens() {
        return totalTokens;
    }

    public void setCompletionTokens(int completionTokens) {
        this.completionTokens = completionTokens;
    }

    public void setPromptTokens(int promptTokens) {
        this.promptTokens = promptTokens;
    }

    public void setTotalTokens(int totalTokens) {
        this.totalTokens = totalTokens;
    }

}