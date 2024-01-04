package org.esadev.mastermindhelper.repository;


import org.esadev.mastermindhelper.dto.ai.Reaction;
import org.esadev.mastermindhelper.entity.UserDailyTaskVote;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserDailyTaskVoteRepository extends CrudRepository<UserDailyTaskVote, Long> {

    List<UserDailyTaskVote> findUserUklByDailyTaskMessageIdAndReactionIn(long messageId, List<Reaction> reactions);

    List<UserDailyTaskVote> findUserDailyTaskVotByDailyTaskMessageIdAndUserIdAndReactionIn(long messageId, long userId, List<Reaction> reactions);
}
