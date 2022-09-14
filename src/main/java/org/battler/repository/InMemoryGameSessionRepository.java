package org.battler.repository;

import org.battler.model.sessions.GameSession;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.util.concurrent.CompletableFuture.completedStage;
import static org.battler.model.sessions.GameState.ACTIVE;
import static org.battler.model.sessions.GameState.PENDING;

/**
 * Created by romanivanov on 14.09.2022
 */
@ApplicationScoped
public class InMemoryGameSessionRepository implements GameSessionRepository {

    private final Map<String, GameSession> sessions = new ConcurrentHashMap<>();

    @Override
    public CompletionStage<GameSession> findAvailableGameSession() {
        return completedStage(sessions.values().stream()
                                      .filter(gameSession -> gameSession.getState().equals(PENDING))
                                      .findFirst()
                                      .orElse(null));
    }

    @Override
    public CompletionStage<GameSession> findActiveGameSessionByUserId(final String userId) {
        return completedStage(sessions.values().stream()
                                      .filter(gameSession -> ACTIVE.equals(gameSession.getState()) &&
                                              gameSession.getPlayers().stream()
                                                         .anyMatch(user -> user.getId().equals(userId)))
                                      .findFirst()
                                      .orElse(null));
    }

    @Override
    public void persistGameSession(final GameSession session) {
        sessions.put(session.getId(), session);
    }

    @Override
    public CompletionStage<Collection<GameSession>> findAllGameSessions() {
        return completedStage(sessions.values());
    }

    @Override
    public void clearGameSessions() {
        sessions.clear();
    }
}
