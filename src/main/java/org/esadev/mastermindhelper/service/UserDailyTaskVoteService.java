package org.esadev.mastermindhelper.service;

import org.esadev.mastermindhelper.dto.ai.Reaction;
import org.esadev.mastermindhelper.entity.UserDailyTaskVote;

import java.util.List;

public interface UserDailyTaskVoteService {

    void save(UserDailyTaskVote userDailyTaskVote);

    void delete(UserDailyTaskVote userDailyTaskVote);

    List<UserDailyTaskVote> getAllByMessageAndReactions(long messageId, List<Reaction> reactions);
}
