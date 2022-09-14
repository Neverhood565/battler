package org.battler.model.sessions;

import lombok.Builder;
import lombok.Data;
import org.battler.model.User;
import org.battler.model.question.Answer;
import org.battler.model.question.Question;

/**
 * Created by romanivanov on 10.09.2022
 */
@Data
@Builder
public class Round {

    private final Question question;
    private final User playerToAsk;
    private final User playerToAnswer;
    private Answer answer;
    private Boolean correct;
    private Integer roundNumber;
}
