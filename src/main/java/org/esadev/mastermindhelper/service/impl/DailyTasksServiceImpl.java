package org.esadev.mastermindhelper.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.esadev.mastermindhelper.aop.Traceable;
import org.esadev.mastermindhelper.bot.MastermindHelperBot;
import org.esadev.mastermindhelper.dto.ai.Reaction;
import org.esadev.mastermindhelper.dto.telegram.InlineButton;
import org.esadev.mastermindhelper.entity.DailyTask;
import org.esadev.mastermindhelper.entity.UserDailyTaskVote;
import org.esadev.mastermindhelper.mapper.UserMapper;
import org.esadev.mastermindhelper.props.TelegramProps;
import org.esadev.mastermindhelper.repository.DailyTasksRepository;
import org.esadev.mastermindhelper.service.DailyTasksService;
import org.esadev.mastermindhelper.service.UserDailyTaskVoteService;
import org.esadev.mastermindhelper.utils.TelegramObjectCreator;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import static org.esadev.mastermindhelper.consts.Constants.TelegramConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyTasksServiceImpl implements DailyTasksService {
    private final DailyTasksRepository dailyTasksRepository;
    private final UserDailyTaskVoteService userDailyTaskVoteService;
    private final MastermindHelperBot mastermindHelperBot;
    private final TelegramProps telegramProps;

    @Override
    public List<DailyTask> findAllForGeneratingTasks() {
        return dailyTasksRepository.findAllForGeneratingTasks();
    }

    @Override
    public void save(Message message, String callback) {
        LocalDateTime triggerTime = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(message.getDate()), TimeZone.getDefault().toZoneId());

        DailyTask build = DailyTask.builder()
                .messageId(message.getMessageId())
                .date(triggerTime)
                .callback(callback)
                .task(message.getText())
                .build();

        dailyTasksRepository.save(build);
    }

    @Traceable
    @Override
    public String handleDailyTaskVote(Update update) {
        try {
            int messageId = update.getCallbackQuery().getMessage().getMessageId();
            String data = update.getCallbackQuery().getData();
            DailyTask dailyTask = dailyTasksRepository.findFirstByMessageId(messageId);
            if (dailyTask != null) {
                Long userId = update.getCallbackQuery().getFrom().getId();
                Optional<UserDailyTaskVote> userUkl = getVoteUser(messageId, userId, data);
                return processUser(update, messageId, dailyTask, userUkl);
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        return UNKNOWN_ERROR_RESPONSE;
    }

    @Traceable
    private Optional<UserDailyTaskVote> getVoteUser(int messageId, Long userId, String buttonCallback) {
        List<UserDailyTaskVote> voteUsers = userDailyTaskVoteService.getUserForVoteById(messageId, userId, List.of(Reaction.values()));
        return voteUsers.stream()
                .filter(userUkl -> {
                    Reaction reaction = userUkl.getReaction();
                    if (LIKE_DAILY_TASK_CALLBACK.equals(buttonCallback)) {
                        return Reaction.SUCCESS_REACTIONS.contains(reaction);
                    } else if (DISLIKE_DAILY_TASK_CALLBACK.equals(buttonCallback)) {
                        return Reaction.FAIL_REACTIONS.contains(reaction);
                    }
                    return false;
                })
                .findFirst();
    }

    @Traceable
    private UserDailyTaskVote getVoteUser(Update update, DailyTask vote) {
        User user = update.getCallbackQuery().getFrom();
        UserDailyTaskVote userDailyTaskVote = UserMapper.USER_MAPPER.userToUserUkl(user);
        userDailyTaskVote.setDailyTask(vote);
        userDailyTaskVote.setVoteTime(LocalDateTime.now());
        userDailyTaskVote.setReaction(update.getCallbackQuery().getData().equals(LIKE_DAILY_TASK_CALLBACK) ? Reaction.FIRE : Reaction.PILE_OF_POOP);
        return userDailyTaskVote;
    }

    @Traceable
    private String handleUserPresent(int messageId, UserDailyTaskVote user) throws TelegramApiException {
        userDailyTaskVoteService.delete(user);
        DailyTask firstByMessageId = dailyTasksRepository.findFirstByMessageId(messageId);
        updateRatingValue(user, firstByMessageId, firstByMessageId.getLikes() - 1, firstByMessageId.getDislikes() - 1);
        dailyTasksRepository.save(firstByMessageId);
        mastermindHelperBot.execute(editMessageReplyMarkupVoteUkl(messageId));
        return REMOVED_RESPONSE;
    }

    @Traceable
    private String handleUserNotPresent(Update update, int messageId, DailyTask dailyTask) throws TelegramApiException {
        DailyTask firstByMessageId = dailyTasksRepository.findFirstByMessageId(messageId);
        UserDailyTaskVote userDailyTaskVote = getVoteUser(update, dailyTask);
        updateRatingValue(userDailyTaskVote, firstByMessageId, firstByMessageId.getLikes() + 1, firstByMessageId.getDislikes() + 1);
        dailyTasksRepository.save(firstByMessageId);
        userDailyTaskVoteService.save(userDailyTaskVote);
        mastermindHelperBot.execute(editMessageReplyMarkupVoteUkl(messageId));

        return ADDED_VOTE_RESPONSE;
    }

    @Traceable
    private String processUser(Update update, int messageId, DailyTask vote, Optional<UserDailyTaskVote> user) throws TelegramApiException {
        String alertResponse;
        if (user.isPresent()) {
            alertResponse = handleUserPresent(messageId, user.get());
        } else {
            alertResponse = handleUserNotPresent(update, messageId, vote);
        }
        return alertResponse;
    }

    @Traceable
    @Override
    public SendMessage createDailyGroupTask(String dailyTask) {
        SendMessage message = TelegramObjectCreator.MessageCreator.createHtmlMessage(DAILY_TASK_MESSAGE.formatted(dailyTask), telegramProps.chatId());

        InlineButton likeDailyTaskCallback = InlineButton.builder().text(DAILY_TASK_LIKE).callBackData(LIKE_DAILY_TASK_CALLBACK).build();
        InlineButton dislikeDailyTaskCallback = InlineButton.builder().text(DAILY_TASK_DISLIKE).callBackData(DISLIKE_DAILY_TASK_CALLBACK).build();

        InlineKeyboardButton likeButton = TelegramObjectCreator.KeyboardCreator.createInlineButton(likeDailyTaskCallback);
        InlineKeyboardButton dislikeButton = TelegramObjectCreator.KeyboardCreator.createInlineButton(dislikeDailyTaskCallback);

        InlineKeyboardMarkup inlineKeyboardMarkup = TelegramObjectCreator.KeyboardCreator.getInlineKeyboardMarkup(List.of(likeButton, dislikeButton));

        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }

    @Traceable
    private EditMessageReplyMarkup editMessageReplyMarkupVoteUkl(long messageId) {
        EditMessageReplyMarkup editMessageReplyMarkup =
                TelegramObjectCreator.KeyboardCreator.createEditMessageReplyMarkup(telegramProps.chatId(), Math.toIntExact(messageId));

        InlineButton likeDailyTaskCallback = InlineButton.builder().text(DAILY_TASK_LIKE + userDailyTaskVoteService.getUsersForVote(messageId, Reaction.SUCCESS_REACTIONS).size()).callBackData(LIKE_DAILY_TASK_CALLBACK).build();
        InlineButton dislikeDailyTaskCallback = InlineButton.builder().text(DAILY_TASK_DISLIKE + userDailyTaskVoteService.getUsersForVote(messageId, Reaction.FAIL_REACTIONS).size()).callBackData(DISLIKE_DAILY_TASK_CALLBACK).build();

        InlineKeyboardButton likeButton = TelegramObjectCreator.KeyboardCreator.createInlineButton(likeDailyTaskCallback);
        InlineKeyboardButton dislikeButton = TelegramObjectCreator.KeyboardCreator.createInlineButton(dislikeDailyTaskCallback);

        InlineKeyboardMarkup inlineKeyboardMarkup = TelegramObjectCreator.KeyboardCreator.getInlineKeyboardMarkup(List.of(likeButton, dislikeButton));
        editMessageReplyMarkup.setReplyMarkup(inlineKeyboardMarkup);

        return editMessageReplyMarkup;
    }

    private void updateRatingValue(UserDailyTaskVote user, DailyTask firstByMessageId, int firstByMessageId1, int firstByMessageId2) {
        if (user.getReaction().equals(Reaction.FIRE)) {
            firstByMessageId.setLikes(firstByMessageId1);
        } else {
            firstByMessageId.setDislikes(firstByMessageId2);
        }
    }

}
