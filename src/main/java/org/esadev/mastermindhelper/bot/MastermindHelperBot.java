package org.esadev.mastermindhelper.bot;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

public class MastermindHelperBot extends SpringWebhookBot {
    private final String botUserName;

    public MastermindHelperBot(SetWebhook setWebhook, String botToken, String botUserName) {
        super(setWebhook, botToken);
        this.botUserName = botUserName;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getBotPath() {
        return "/telegram";
    }
}