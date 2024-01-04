package org.esadev.mastermindhelper.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.esadev.mastermindhelper.bot.MastermindHelperBot;
import org.esadev.mastermindhelper.scheduled.ScheduleNotifications;
import org.esadev.mastermindhelper.service.DailyTasksService;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.UUID;

import static org.esadev.mastermindhelper.consts.Constants.TelegramConstants.CALLBACK_DATA_UKL_VOTE;
import static org.esadev.mastermindhelper.utils.TelegramObjectCreator.MessageCreator;

@RestController
@RequestMapping("callback")
@RequiredArgsConstructor
@Slf4j
public class TelegramController {
    private final MastermindHelperBot mastermindHelperBot;
    private final DailyTasksService dailyTasksService;
    private final ScheduleNotifications scheduleNotifications;

    @PostMapping("telegram")
    public void getUpdates(@RequestBody Update update) throws TelegramApiException {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        if (callbackQuery != null) {
            AnswerCallbackQuery answerCallbackQuery = processCallback(update, callbackQuery);
            try {
                mastermindHelperBot.execute(answerCallbackQuery);
            } catch (TelegramApiException e) {
                UUID randomUUID = UUID.randomUUID();
                log.error("Something went wrong with processing callback {}", randomUUID, e);
                mastermindHelperBot.execute(MessageCreator.createSimpleMessage(("Щось пішло не так, зверніться в підтримку з цим кодом: %s".formatted(randomUUID)), update.getMessage().getChatId().toString()));
            }
        }
    }

    @SneakyThrows
    @GetMapping
    public void createDailyGroupTask() {
        scheduleNotifications.sendDailyTask();
    }

    private AnswerCallbackQuery processCallback(Update update, CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        String message;
        if (data.endsWith(CALLBACK_DATA_UKL_VOTE)) {
            message = dailyTasksService.handleDailyTaskVote(update);
        } else {
            message = StringUtils.EMPTY;
        }
        return MessageCreator.createAnswerCallbackQuery(update.getCallbackQuery().getId(), message);
    }
}
