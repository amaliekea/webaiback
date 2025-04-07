package org.example.examai.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.processing.Generated;
import java.util.List;

/**
 * DTO til at sende data til OpenAI API.
 * Indeholder modelnavn, temperatur, max tokens og beskeder.
 */
// bruges til at oprette en request
// klassen indeholder informationer om hvilken model jeg bruger
//samtalen indeholder en message
//hvor kreavtiv gpt skal være (temperature)
//hvor langt svaret må være (max tokens)

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "model",
        "messages",
        "temperature",
        "top_p",
        "max_tokens",
        "frequency_penalty",
        "presence_penalty",
})
@Generated("jsonschema2pojo")
public class RequestDTO {
    /**
     * Navnet på GPT-modellen
     */
    @JsonProperty("model")
    private String model;

    /**
     * Liste med beskeder til GPT
     */
    @JsonProperty("messages")
    private List<Message> messages;

    /**
     * Hvor kreativt GPT skal svare (0–1)
     */
    @JsonProperty("temperature")
    private Double temperature;

    /**
     * Alternativ til temperature (0–1)
     */
    @JsonProperty("top_p")
    private Double topP;

    /**
     * Maksimalt antal tokens i svaret
     */
    @JsonProperty("max_tokens")
    private Integer maxTokens;

    /**
     * Straffer gentagelser i svaret
     */
    @JsonProperty("frequency_penalty")
    private Double frequencyPenalty;

    /**
     * Skubber GPT til at være mere kreativ
     */
    @JsonProperty("presence_penalty")
    private Double presencePenalty;

    // Getters og setters

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

    @JsonProperty("top_p")
    public Double getTopP() {
        return topP;
    }

    @JsonProperty("top_p")
    public void setTopP(Double topP) {
        this.topP = topP;
    }

    @JsonProperty("max_tokens")
    public Integer getMaxTokens() {
        return maxTokens;
    }

    @JsonProperty("max_tokens")
    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }

    @JsonProperty("frequency_penalty")
    public Double getFrequencyPenalty() {
        return frequencyPenalty;
    }

    @JsonProperty("frequency_penalty")
    public void setFrequencyPenalty(Double frequencyPenalty) {
        this.frequencyPenalty = frequencyPenalty;
    }

    @JsonProperty("presence_penalty")
    public Double getPresencePenalty() {
        return presencePenalty;
    }

    @JsonProperty("presence_penalty")
    public void setPresencePenalty(Double presencePenalty) {
        this.presencePenalty = presencePenalty;
    }
}
