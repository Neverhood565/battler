package org.battler.repository;

import org.battler.model.UserId;
import org.battler.model.session.GameSession;
import org.battler.model.session.GameSessionState;
import org.battler.model.session.QuestionType;

import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Created by romanivanov on 10.09.2022
 */
public interface GameSessionRepository {

    CompletionStage<GameSession> findPendingGameSessionByQuestionType(QuestionType questionsType);

    CompletionStage<GameSession> findGameSessionByUserIdAndState(UserId userId, GameSessionState state);

    void persistGameSession(GameSession session);

    CompletionStage<List<GameSession>> findAllGameSessions();

    void clearGameSessions();
}
