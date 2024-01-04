package org.esadev.mastermindhelper.service.impl;

import lombok.RequiredArgsConstructor;
import org.esadev.mastermindhelper.aop.Traceable;
import org.esadev.mastermindhelper.dto.ai.Reaction;
import org.esadev.mastermindhelper.entity.UserDailyTaskVote;
import org.esadev.mastermindhelper.repository.UserDailyTaskVoteRepository;
import org.esadev.mastermindhelper.service.UserDailyTaskVoteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDailyTaskVoteServiceImpl implements UserDailyTaskVoteService {
    private final UserDailyTaskVoteRepository userDailyTaskVoteRepository;

    @Traceable
    @Override
    public void save(UserDailyTaskVote userDailyTaskVote) {
        userDailyTaskVoteRepository.save(userDailyTaskVote);
    }

    @Traceable
    @Override
    public void delete(UserDailyTaskVote userDailyTaskVote) {
        userDailyTaskVoteRepository.delete(userDailyTaskVote);
    }

    @Override
    public List<UserDailyTaskVote> getUsersForVote(long messageId, List<Reaction> reactions) {
        return userDailyTaskVoteRepository.findUserUklByDailyTaskMessageIdAndReactionIn(messageId,reactions);
    }

    @Traceable
    @Override
    public List<UserDailyTaskVote> getUserForVoteById(long messageId, Long userId, List<Reaction> reactions) {
        return userDailyTaskVoteRepository.findUserDailyTaskVotByDailyTaskMessageIdAndUserIdAndReactionIn(messageId, userId, reactions);
    }
}
