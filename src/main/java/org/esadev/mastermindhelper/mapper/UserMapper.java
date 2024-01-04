package org.esadev.mastermindhelper.mapper;

import org.esadev.mastermindhelper.entity.UserDailyTaskVote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);

    @Mappings(
            {
                    @Mapping(target = "userId", source = "id"),
                    @Mapping(target = "login", source = "userName"),
                    @Mapping(target = "dailyTask", ignore = true),
                    @Mapping(target = "reaction", ignore = true),
                    @Mapping(target = "voteTime", ignore = true)
            })
    UserDailyTaskVote userToUserUkl(org.telegram.telegrambots.meta.api.objects.User user);
}
