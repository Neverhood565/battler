package org.battler.model.session;

import lombok.Builder;
import lombok.Data;
import org.battler.model.UserId;
import org.battler.model.question.Question;

/**
 * Created by romanivanov on 10.09.2022
 */
@Data
@Builder
public class Round {

    private final Question question;
    private final UserId questioner;
    private Boolean correct;
    private Integer roundNumber;
}
