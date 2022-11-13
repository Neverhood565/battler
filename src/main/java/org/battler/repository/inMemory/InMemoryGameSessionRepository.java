package org.battler.repository.inMemory;

import io.quarkus.arc.DefaultBean;
import org.battler.model.UserId;
import org.battler.model.session.GameSession;
import org.battler.model.session.GameSessionState;
import org.battler.model.session.QuestionType;
import org.battler.repository.GameSessionRepository;
import org.bson.types.ObjectId;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.concurrent.CompletableFuture.completedStage;
import static org.battler.model.session.GameSessionState.ACTIVE;
import static org.battler.model.session.GameSessionState.PENDING;

/**
 * Created by romanivanov on 14.09.2022
 */
@DefaultBean
@ApplicationScoped
public class InMemoryGameSessionRepository implements GameSessionRepository {

    private final Map<ObjectId, GameSession> sessions = new ConcurrentHashMap<>();

    @Override
    public CompletionStage<GameSession> findPendingGameSessionByQuestionType(QuestionType questionType) {
        return completedStage(sessions.values().stream()
                                      .filter(gameSession -> gameSession.getState().equals(PENDING))
                                      .findFirst()
                                      .orElse(null));
    }

    @Override
    public CompletionStage<GameSession> findGameSessionByUserIdAndState(UserId userId, GameSessionState state) {
        return completedStage(sessions.values().stream()
                                      .filter(gameSession -> ACTIVE.equals(gameSession.getState()) &&
                                              gameSession.getPlayers().stream()
                                                         .anyMatch(user -> user.equals(userId)))
                                      .findFirst()
                                      .orElse(null));
    }

    @Override
    public void persistGameSession(final GameSession session) {
        sessions.put(session.getId(), session);
    }

    @Override
    public CompletionStage<List<GameSession>> findAllGameSessions() {
        return completedStage(new ArrayList<>(sessions.values()));
    }

    @Override
    public void clearGameSessions() {
        sessions.clear();
    }
}
