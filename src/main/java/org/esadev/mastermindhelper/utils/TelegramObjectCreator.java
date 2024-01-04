package org.esadev.mastermindhelper.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.esadev.mastermindhelper.dto.telegram.InlineButton;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TelegramObjectCreator {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class KeyboardCreator {

        public static InlineKeyboardMarkup createInlineKeyboard() {
            return new InlineKeyboardMarkup();
        }

        public static InlineKeyboardButton createInlineButton(InlineButton button) {
            InlineKeyboardButton voteButton = new InlineKeyboardButton(button.getText());
            voteButton.setCallbackData(button.getCallBackData());
            voteButton.setUrl(button.getUrl());

            return voteButton;
        }

        public static EditMessageReplyMarkup createEditMessageReplyMarkup(String chatId, int messageId) {
            EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
            editMessageReplyMarkup.setChatId(chatId);
            editMessageReplyMarkup.setMessageId(messageId);
            return editMessageReplyMarkup;
        }

        @SafeVarargs
        public static InlineKeyboardMarkup getInlineKeyboardMarkup(List<InlineKeyboardButton>... rows) {
            InlineKeyboardMarkup inlineKeyboardMarkup = KeyboardCreator.createInlineKeyboard();
            inlineKeyboardMarkup.setKeyboard(List.of(rows));
            return inlineKeyboardMarkup;

        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MessageCreator {
        public static SendMessage createHtmlMessage(String message, String chatId) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(message);
            sendMessage.setAllowSendingWithoutReply(true);
            sendMessage.enableHtml(true);
            sendMessage.disableWebPagePreview();
            return sendMessage;
        }

        public static SendMessage createSimpleMessage(String message, String chatId) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(message);
            sendMessage.enableMarkdown(true);
            sendMessage.setAllowSendingWithoutReply(true);
            sendMessage.disableWebPagePreview();
            return sendMessage;
        }

        public static AnswerCallbackQuery createAnswerCallbackQuery(String callbackId, String message) {
            AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
            answerCallbackQuery.setShowAlert(true);
            answerCallbackQuery.setText(message);
            answerCallbackQuery.setCallbackQueryId(callbackId);
            return answerCallbackQuery;
        }

    }
}