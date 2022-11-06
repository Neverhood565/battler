package org.battler.repository.mongo.mapper;

import org.battler.model.session.GameSession;
import org.battler.repository.mongo.entity.GameSessionEntity;
import org.mapstruct.Mapper;

/**
 * Created by romanivanov on 06.11.2022
 */
@Mapper(componentModel = "cdi", uses = {QuestionMapper.class, RoundMapper.class, MeetingMapper.class})
public interface GameSessionMapper {

    GameSessionEntity toEntity(GameSession gameSession);

    /*@Mapping(
            target = "id",
            expression = "java(gameSessionEntity.getId().toString())"
    )*/
    GameSession toModel(GameSessionEntity gameSessionEntity);
}
