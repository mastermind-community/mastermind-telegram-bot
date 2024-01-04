package org.esadev.mastermindhelper.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.esadev.mastermindhelper.dto.ai.AiMessageResponse;
import org.esadev.mastermindhelper.dto.ai.ChatCompletionRequest;
import org.esadev.mastermindhelper.dto.ai.ChatCompletionRequest.AiMessageRequest;
import org.esadev.mastermindhelper.entity.DailyTask;
import org.esadev.mastermindhelper.props.AiProps;
import org.esadev.mastermindhelper.service.AiService;
import org.esadev.mastermindhelper.service.DailyTasksService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.esadev.mastermindhelper.consts.Constants.AiConstants.*;

@RequiredArgsConstructor
@Service
public class AiServiceImpl implements AiService {
    private final DailyTasksService dailyTasksService;
    private final ObjectMapper objectMapper;
    private final AiProps aiProps;
    private final RestClient restClient;

    @SneakyThrows
    @Override
    public String getDailyTask() {
        AiMessageResponse response = fetchAiResponse();
        return response.getChoices().getFirst().getMessage().getContent();
    }

    private AiMessageResponse fetchAiResponse() throws JsonProcessingException {
        ChatCompletionRequest request = buildDailyTasksRequest();
        return restClient.post()
                .uri(aiProps.completionUrl())
                .headers(httpHeaders -> {
                    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                    httpHeaders.setBearerAuth(aiProps.key());
                })
                .body(request)
                .retrieve()
                .body(AiMessageResponse.class);
    }

    private ChatCompletionRequest buildDailyTasksRequest() throws JsonProcessingException {
        List<DailyTask> previousDailyTasks = dailyTasksService.findAllForDailyTasks();
        String content = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(previousDailyTasks);
        ChatCompletionRequest chatCompletionRequest = new ChatCompletionRequest();
        chatCompletionRequest.setMessages(List.of(
                new AiMessageRequest(AI_SYSTEM_ROLE, AI_SYSTEM_MESSAGE_DAILY_TASKS),
                new AiMessageRequest(AI_USER_ROLE, content)

        ));
        return chatCompletionRequest;
    }
}

