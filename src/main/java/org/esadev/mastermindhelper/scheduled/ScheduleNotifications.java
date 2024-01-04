package org.esadev.mastermindhelper.scheduled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.esadev.mastermindhelper.bot.MastermindHelperBot;
import org.esadev.mastermindhelper.dto.LeaderInfoDto;
import org.esadev.mastermindhelper.props.TelegramProps;
import org.esadev.mastermindhelper.service.AiService;
import org.esadev.mastermindhelper.service.DailyTasksService;
import org.esadev.mastermindhelper.service.MessageFormatService;
import org.esadev.mastermindhelper.service.SheetService;
import org.esadev.mastermindhelper.utils.TelegramObjectCreator.MessageCreator;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static org.esadev.mastermindhelper.consts.Constants.SEND_DAILY_STATS;
import static org.esadev.mastermindhelper.consts.Constants.SEND_DAILY_TASK;
import static org.esadev.mastermindhelper.consts.Constants.TelegramConstants.CALLBACK_DATA_UKL_VOTE;

@EnableScheduling
@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleNotifications {
    private final MastermindHelperBot mastermindHelperBot;
    private final SheetService sheetService;
    private final MessageFormatService messageFormatService;
    private final TelegramProps telegramProps;
    private final AiService aiService;
    private final DailyTasksService dailyTasksService;

    @Scheduled(cron = SEND_DAILY_STATS)
    public void sendSprintStatistics() throws TelegramApiException {
        List<LeaderInfoDto> info = sheetService.getLeadersInfo();
        String leadersInfo = messageFormatService.formatLeaderInfo(info);
        SendMessage simpleMessage = MessageCreator.createSimpleMessage(leadersInfo, telegramProps.chatId());
        mastermindHelperBot.execute(simpleMessage);
    }

    @Scheduled(cron = SEND_DAILY_TASK)
    public void sendDailyTask() throws TelegramApiException {
        String dailyTask = aiService.getDailyTask();
        if (StringUtils.isNotEmpty(dailyTask)) {
            SendMessage dailyGroupTask = dailyTasksService.createDailyGroupTask(dailyTask);
            Message dailyTaskPublishResponse = mastermindHelperBot.execute(dailyGroupTask);
            dailyTasksService.save(dailyTaskPublishResponse, CALLBACK_DATA_UKL_VOTE);
        } else {
            log.info("No task were generated, skipping sending daily task to telegram..");
        }
    }

}
