package org.battler.repository;

import org.battler.model.sessions.GameSession;

import java.util.Collection;
import java.util.concurrent.CompletionStage;

/**
 * Created by romanivanov on 10.09.2022
 */
public interface GameSessionRepository {

    CompletionStage<GameSession> findAvailableGameSession();

    CompletionStage<GameSession> findActiveGameSessionByUserId(String userId);

    void persistGameSession(GameSession session);

    CompletionStage<Collection<GameSession>> findAllGameSessions();

    void clearGameSessions();
}
