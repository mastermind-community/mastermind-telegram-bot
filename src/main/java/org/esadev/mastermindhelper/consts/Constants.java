package org.esadev.mastermindhelper.consts;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String LEADERS_RANGE = "Dashboard!B2:C12";
    public static final String SEND_DAILY_STATS = "0 0 6 * * *";
    public static final String SEND_DAILY_TASK = "0 0 4 * * *";
    public static final String NO_LEADER_INFO_MESSAGE = "`З якоїсь причини не можу отримати інформацію. Зверніться в підтримку";
    public static final String LEADER_INFO_FIRST_LINE_MESSAGE = "**Статистика на %s**\n\n";
    public static final String LEADER_INFO_BUILD_LINE_MESSAGE = "`%s місце - %s => %s %s`\n";
    public static final String NO_DATA = "Немає даних";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class AiConstants {
        public static final String AI_SYSTEM_MESSAGE_DAILY_TASKS = """
                You are a helpful assistant that can help to come up with ideas for self development. 
                You will receive a list of previous activities and you need to come up with a new one. 
                Please pay attention to likes and dislikes and a new task based on them. 
                You must to respond only in json format but provide it without additional text. 
                Provide only a raw text just like that {"new_task": "new task description"}. 
                DON'T USE ANY MARKDOWN""";
        public static final String AI_SYSTEM_ROLE = "system";
        public static final String AI_USER_ROLE = "user";

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class TelegramConstants {
        public static final String LIKE_DAILY_TASK_CALLBACK = "like_daily_task_callback";
        public static final String DISLIKE_DAILY_TASK_CALLBACK = "dislike_daily_task_callback";

        public static final String REMOVED_RESPONSE = "Голос видалений \uD83D\uDE22";
        public static final String ADDED_VOTE_RESPONSE = "Дякую, голос зарахований \uD83D\uDE0A";
        public static final String UNKNOWN_ERROR_RESPONSE = "Помилка \uD83E\uDD2A Спробуйте через декілька секунд";
        public static final String DAILY_TASK_LIKE = "\uD83D\uDD25";
        public static final String DAILY_TASK_DISLIKE = "\uD83D\uDCA9";
        public static final String DAILY_TASK_MESSAGE = """
            %s
                        
            <i>ШІ робить нові завдання базуючись на попередніх. Тож прошу активно голосувати наскільки завдання було цікаве</i>
            """;

        public static final String CALL_BACK_DATA_UKL_VOTE = "daily_task_callback";
    }
}
