package org.esadev.mastermindhelper.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.esadev.mastermindhelper.bot.MastermindHelperBot;
import org.esadev.mastermindhelper.props.TelegramProps;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.updatesreceivers.ServerlessWebhook;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class TelegramConfig {
    private final TelegramProps telegramProps;

    @Bean
    public SetWebhook webhook() {
        return SetWebhook.builder()
                .url(telegramProps.webhook())
                .build();
    }
    @Bean
    public MastermindHelperBot mastermindHelperBot() {
        MastermindHelperBot mastermindHelperBot = new MastermindHelperBot(webhook(), telegramProps.token(), telegramProps.name());
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class, new ServerlessWebhook());
            botsApi.registerBot(mastermindHelperBot, webhook());
        } catch (TelegramApiException e) {
            log.error("Error occurred when registered telegram bot", e);
        }
        return mastermindHelperBot;
    }
}
