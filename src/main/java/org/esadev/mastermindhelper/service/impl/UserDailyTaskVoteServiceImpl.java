package org.esadev.mastermindhelper.service.impl;

import lombok.RequiredArgsConstructor;
import org.esadev.mastermindhelper.aop.Traceable;
import org.esadev.mastermindhelper.dto.ai.Reaction;
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
    public void save(org.esadev.mastermindhelper.entity.UserDailyTaskVote userDailyTaskVote) {
        userDailyTaskVoteRepository.save(userDailyTaskVote);
    }

    @Traceable
    @Override
    public void delete(org.esadev.mastermindhelper.entity.UserDailyTaskVote userDailyTaskVote) {
        userDailyTaskVoteRepository.delete(userDailyTaskVote);
    }

    @Traceable
    @Override
    public List<org.esadev.mastermindhelper.entity.UserDailyTaskVote> getAllByMessageAndReactions(long messageId, List<Reaction> reactions) {
        return userDailyTaskVoteRepository.findUserUklByDailyTaskMessageIdAndReactionIn(messageId, reactions);
    }
}
