package org.esadev.mastermindhelper.dto.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatCompletionRequest {
    private String model = "gpt-4-1106-preview";
    private List<AiMessageRequest> messages;
    @JsonProperty("response_format")
    private ResponseFormat responseFormat;

    @Data
    static class ResponseFormat {
        private String type = "json_object";
    }

    @Data
    @AllArgsConstructor
    public static class AiMessageRequest {
        private String role;
        private String content;
    }
}
