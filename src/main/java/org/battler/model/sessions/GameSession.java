package org.battler.model.sessions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.battler.model.User;
import org.battler.model.question.Question;

import java.util.List;
import java.util.Map;

/**
 * Created by romanivanov on 10.09.2022
 */
public interface GameSession {

    String getId();

    void joinPlayer(User user, List<Question> questions);

    void answerQuestion(String questionId, Boolean correct);

    void start();

    void abort();

    boolean readyToStart();

    Round getCurrentRound();

    GameState getState();

    Meeting getMeetingInfo();

    List<User> getPlayers();

    User getWinner();
}
