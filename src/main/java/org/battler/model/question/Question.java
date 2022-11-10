package org.battler.model.question;

import lombok.Builder;
import lombok.Data;
import org.battler.model.session.QuestionType;

/**
 * Created by romanivanov on 31.08.2022
 */
@Data
@Builder
public class Question {

    private String id;
    private String title;
    private String correctAnswer;
    private QuestionType type;
}
