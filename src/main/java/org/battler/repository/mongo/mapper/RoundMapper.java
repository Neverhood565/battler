package org.battler.repository.mongo.mapper;

import org.battler.model.session.Round;
import org.battler.repository.mongo.entity.RoundEntity;
import org.mapstruct.Mapper;

/**
 * Created by romanivanov on 06.11.2022
 */
@Mapper(componentModel = "cdi")
public interface RoundMapper {

    RoundEntity toEntity(Round round);

    Round toModel(RoundEntity roundEntity);
}
