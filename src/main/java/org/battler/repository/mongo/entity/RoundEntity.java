package org.battler.repository.mongo.entity;

import lombok.Data;
import org.battler.model.UserId;

/**
 * Created by romanivanov on 06.11.2022
 */
@Data
public class RoundEntity {

    private QuestionEntity question;
    private UserId questioner;
    private Boolean correct;
    private Integer roundNumber;
}
