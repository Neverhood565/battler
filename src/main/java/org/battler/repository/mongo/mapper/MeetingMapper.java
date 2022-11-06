package org.battler.repository.mongo.mapper;

import org.battler.model.session.Meeting;
import org.battler.repository.mongo.entity.MeetingEntity;
import org.mapstruct.Mapper;

/**
 * Created by romanivanov on 06.11.2022
 */
@Mapper(componentModel = "cdi")
public interface MeetingMapper {

    MeetingEntity toEntity(Meeting round);

    Meeting toModel(MeetingEntity roundEntity);
}
