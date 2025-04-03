package org.example.medai.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.processing.Generated;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "model",
        "messages",
        "temperature",
        "max_tokens",
        "presence_penalty",
        "stop"
})
@Generated("jsonschema2pojo")
public class RequestDTO {
    @JsonProperty("model")
    private String model;

    @JsonProperty("messages")
    private List<Message> messages;

    @JsonProperty("temperature")
    private Double temperature;

    @JsonProperty("max_tokens")
    private Integer maxTokens;


    @JsonProperty("presence_penalty")
    private Double presencePenalty;

    @JsonProperty("stop")
    private List<String> stop;

    @JsonProperty("model")
    public String getModel() {
        return model;
    }

    @JsonProperty("model")
    public void setModel(String model) {
        this.model = model;
    }

    @JsonProperty("messages")
    public List<Message> getMessages() {
        return messages;
    }

    @JsonProperty("messages")
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @JsonProperty("temperature")
    public Double getTemperature() {
        return temperature;
    }

    @JsonProperty("temperature")
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    @JsonProperty("max_tokens")
    public Integer getMaxTokens() {
        return maxTokens;
    }

    @JsonProperty("max_tokens")
    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }


    @JsonProperty("presencePenalty")
    public Double getPresencePenalty() {
        return presencePenalty;
    }

    @JsonProperty("presencePenalty")
    public void setPresencePenalty(Double presencePenalty) {
        this.presencePenalty = presencePenalty;
    }


    @JsonProperty("stop")
    public List<String> getStop() {
        return stop;
    }

    @JsonProperty("stop")
    public void setStop(List<String> stop) {
        this.stop = stop;
    }
}
