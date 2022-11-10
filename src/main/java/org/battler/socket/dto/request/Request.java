package org.battler.socket.dto.request;

import lombok.Data;

/**
 * Created by romanivanov on 14.09.2022
 */
@Data
public class Request {

    public static final String FIND_GAME = "find_game";
    public static final String ANSWER_QUESTION = "answer_question";
    public static final String LEAVE_GAME = "leave_game";

    private String event;
    private Boolean correct;
    private String questionId;
    private String questionsType;
}
