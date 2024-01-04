package org.esadev.mastermindhelper.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "telegram_schema", name = "daily_tasks")
public class DailyTask {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(length = 5000)
    private String task;
    private LocalDateTime date;
    private int likes;
    private int dislikes;
    private int messageId;
    private String callback;

    public DailyTask(String task, Integer likes, Integer dislikes) {
        this.task = task;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyTask dailyTask = (DailyTask) o;
        return likes == dailyTask.likes && dislikes == dailyTask.dislikes && messageId == dailyTask.messageId && Objects.equals(task, dailyTask.task) && Objects.equals(date, dailyTask.date) && Objects.equals(callback, dailyTask.callback);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, task, date, likes, dislikes, messageId, callback);
    }
}