package org.battler.repository.mongo.mapper;

import org.battler.model.question.Question;
import org.battler.repository.mongo.entity.QuestionEntity;
import org.mapstruct.Mapper;

/**
 * Created by romanivanov on 06.11.2022
 */
@Mapper(componentModel = "cdi")
public interface QuestionMapper {

    QuestionEntity toEntity(Question round);

    Question toModel(QuestionEntity roundEntity);
}
