package org.battler.repository.mongo.mapper;

import org.battler.model.question.Question;
import org.battler.repository.mongo.entity.QuestionEntity;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Created by romanivanov on 06.11.2022
 */
@Mapper(componentModel = "cdi", imports = ObjectId.class)
public interface QuestionMapper {

    @Mapping(expression = "java(question.getId() != null ? new ObjectId(question.getId()) : null)", target = "id")
    QuestionEntity toEntity(Question question);

    @Mapping(expression = "java(questionEntity.getId().toHexString())", target = "id")
    Question toModel(QuestionEntity questionEntity);
}
