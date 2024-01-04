package org.esadev.mastermindhelper.repository;

import org.esadev.mastermindhelper.entity.DailyTask;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DailyTasksRepository extends CrudRepository<DailyTask, Long> {

    @Query("select new org.esadev.mastermindhelper.entity.DailyTask(d.task, d.likes, d.dislikes) from DailyTask d")
    List<DailyTask> findAllForGeneratingTasks();

    DailyTask findFirstByMessageId(Integer messageId);
}
