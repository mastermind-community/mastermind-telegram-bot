package org.esadev.mastermindhelper.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.esadev.mastermindhelper.dto.ai.Reaction;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(schema = "telegram_schema")
public class UserDailyTaskVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private LocalDateTime voteTime;
    private String login;
    private String firstName;
    private String lastName;
    @ManyToOne
    @Cascade(CascadeType.PERSIST)
    @JoinColumn(name = "MESSAGE_ID")
    private DailyTask dailyTask;
    @Enumerated(EnumType.STRING)
    private Reaction reaction;
}
