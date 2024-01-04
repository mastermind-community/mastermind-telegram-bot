package org.esadev.mastermindhelper.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface AiService {
    String getDailyTask() throws JsonProcessingException;
}
