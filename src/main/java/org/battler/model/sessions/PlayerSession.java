package org.battler.model.sessions;

import lombok.Data;
import org.battler.model.User;
import org.battler.model.question.Question;

import java.util.List;

/**
 * Created by romanivanov on 31.08.2022
 */
@Data
public class PlayerSession {

    private String currentSocketSession;
    private List<Question> questions;
    private User user;
    private Meeting meeting;
}
