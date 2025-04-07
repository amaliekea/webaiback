
package org.example.examai.dto;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.processing.Generated;
import java.util.LinkedHashMap;
import java.util.Map;
//Klassen Usage bruges til at repræsentere information om token-forbrug i et svar fra OpenAI’s API.
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "prompt_tokens",
    "total_tokens",
    "completion_tokens"
})
@Generated("jsonschema2pojo")
public class Usage {

    @JsonProperty("prompt_tokens")
    private Integer promptTokens;
    @JsonProperty("total_tokens")
    private Integer totalTokens;
    @JsonProperty("completion_tokens")
    private Integer completionTokens;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("prompt_tokens")
    public Integer getPromptTokens() {
        return promptTokens;
    }

    @JsonProperty("prompt_tokens")
    public void setPromptTokens(Integer promptTokens) {
        this.promptTokens = promptTokens;
    }

    @JsonProperty("total_tokens")
    public Integer getTotalTokens() {
        return totalTokens;
    }

    @JsonProperty("total_tokens")
    public void setTotalTokens(Integer totalTokens) {
        this.totalTokens = totalTokens;
    }

    @JsonProperty("completion_tokens")
    public Integer getCompletionTokens() {
        return completionTokens;
    }

    @JsonProperty("completion_tokens")
    public void setCompletionTokens(Integer completionTokens) {
        this.completionTokens = completionTokens;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
