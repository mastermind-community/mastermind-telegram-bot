package org.esadev.mastermindhelper.service;

import org.esadev.mastermindhelper.entity.DailyTask;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface DailyTasksService {
    List<DailyTask> findAllForGeneratingTasks();

    void save(Message message, String callback);

    String handleDailyTaskVote(Update update);

    SendMessage createDailyGroupTask(String dailyTask);

}
