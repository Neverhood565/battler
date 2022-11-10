package org.battler.repository.mongo.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Data;
import org.battler.model.session.QuestionType;
import org.bson.types.ObjectId;

/**
 * Created by romanivanov on 06.11.2022
 */
@Data
@MongoEntity(collection = "question")
public class QuestionEntity {

    private ObjectId id;
    private String title;
    private String correctAnswer;
    private QuestionType type;
}
