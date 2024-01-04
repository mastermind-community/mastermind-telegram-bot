package org.esadev.mastermindhelper.repository;


import org.esadev.mastermindhelper.dto.ai.Reaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserDailyTaskVoteRepository extends CrudRepository<org.esadev.mastermindhelper.entity.UserDailyTaskVote, Long> {

    List<org.esadev.mastermindhelper.entity.UserDailyTaskVote> findUserUklByDailyTaskMessageIdAndReactionIn(long messageId, List<Reaction> reactions);
}
