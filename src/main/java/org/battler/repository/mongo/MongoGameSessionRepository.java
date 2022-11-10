package org.battler.repository.mongo;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheQuery;
import lombok.extern.slf4j.Slf4j;
import org.battler.model.UserId;
import org.battler.model.session.GameSession;
import org.battler.model.session.GameSessionState;
import org.battler.model.session.QuestionType;
import org.battler.repository.GameSessionRepository;
import org.battler.repository.mongo.entity.GameSessionEntity;
import org.battler.repository.mongo.mapper.GameSessionMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static org.battler.model.session.GameSessionState.PENDING;

/**
 * Created by romanivanov on 05.11.2022
 */
@Slf4j
@ApplicationScoped
public class MongoGameSessionRepository implements GameSessionRepository,
        ReactivePanacheMongoRepository<GameSessionEntity> {

    @Inject
    GameSessionMapper mapper;

    @Override
    public CompletionStage<GameSession> findAvailableGameSession(QuestionType questionsType) {
        return querySession(questionsType)
                .firstResult()
                .map(mapper::toModel)
                .subscribeAsCompletionStage();
    }

    private ReactivePanacheQuery<GameSessionEntity> querySession(final QuestionType questionsType) {
        if (questionsType != null) {
            return find("state = ?1, type = ?2", PENDING, questionsType);
        }
        return find("state = ?1", PENDING);
    }

    @Override
    public CompletionStage<GameSession> findActiveGameSessionByUserId(final UserId userId) {
        return find("players._id", userId.getId())
                .firstResult()
                .map(mapper::toModel)
                .subscribeAsCompletionStage();
    }

    @Override
    public void persistGameSession(final GameSession session) {
        persistOrUpdate(mapper.toEntity(session)).subscribe().with(entity -> log.info(
                "Game session persisted. Game session ID = {}",
                session.getId()
        ));
    }

    @Override
    public CompletionStage<List<GameSession>> findAllGameSessions() {
        return findAll()
                .page(0, 1000)
                .stream()
                .map(mapper::toModel)
                .collect().asList()
                .subscribeAsCompletionStage();
    }

    @Override
    public void clearGameSessions() {
        deleteAll().subscribe().with(numberOfDeleted -> log.warn("Deleted {} game sessions", numberOfDeleted));
    }
}
