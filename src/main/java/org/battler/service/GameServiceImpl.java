package org.battler.service;

import lombok.extern.slf4j.Slf4j;
import org.battler.model.event.GameCompletedEvent;
import org.battler.model.event.GameEvent;
import org.battler.model.event.GameStartedEvent;
import org.battler.model.User;
import org.battler.model.event.NewRoundEvent;
import org.battler.model.sessions.BaseGameSession;
import org.battler.model.sessions.GameSession;
import org.battler.model.sessions.GameState;
import org.battler.repository.GameSessionRepository;
import org.battler.repository.QuestionRepository;
import org.battler.service.meeting.MeetingService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Created by romanivanov on 28.08.2022
 */
@Slf4j
@ApplicationScoped
public class GameServiceImpl implements GameService {

    public static final int NUMBER_OF_ROUNDS = 4;
    @Inject
    QuestionRepository questionRepository;

    @Inject
    MeetingService meetingService;

    @Inject
    Event<GameEvent> gameEvents;

    @Inject
    GameSessionRepository gameSessionRepository;

    @Override
    public void findGame(String userId) {
        gameSessionRepository.findAvailableGameSession().thenCompose(gameSession -> {
            if (gameSession == null) {
                return createNewGameSession();
            }
            return CompletableFuture.completedStage(gameSession);
        }).thenAccept(gameSession -> {
            gameSession.joinPlayer(
                    User.builder().id(userId).build(),
                    questionRepository.getNextRandomQuestions(NUMBER_OF_ROUNDS / 2)
            );
            if (gameSession.readyToStart()) {

                gameSession.start();
                gameEvents.fire(new GameStartedEvent(gameSession));
                gameEvents.fire(new NewRoundEvent(gameSession));
                log.info("Game session started. Game session ID: {}, User ID: {}", gameSession.getId(), userId);

            } else {
                log.info("Game session pending. Game session ID: {}, User ID: {}", gameSession.getId(), userId);
            }
            gameSessionRepository.persistGameSession(gameSession);
        }).exceptionally(exception -> {
            log.error(exception.getMessage(), exception);
            return null;
        });

    }

    @Override
    public void answerQuestion(String userId, String questionId, Boolean correct) {
        gameSessionRepository.findActiveGameSessionByUserId(userId).thenAccept(gameSession -> {
            gameSession.answerQuestion(
                    questionId,
                    correct
            );

            log.info("Question answered. Game session ID: {}, User ID: {}", gameSession.getId(), userId);

            if (gameSession.getState().equals(GameState.ACTIVE)) {

                gameEvents.fire(new NewRoundEvent(gameSession));
                log.info("New round started. Game session ID: {}, User ID: {}", gameSession.getId(), userId);

            } else if (gameSession.getState().equals(GameState.COMPLETED)) {

                gameEvents.fire(new GameCompletedEvent(gameSession));
                log.info("Game completed. Game session ID: {}, User ID: {}", gameSession.getId(), userId);

            }
            gameSessionRepository.persistGameSession(gameSession);
        }).exceptionally(exception -> {
            log.error(exception.getMessage(), exception);
            return null;
        });
    }

    @Override
    public void leaveGame(final String userId) {
        gameSessionRepository.findActiveGameSessionByUserId(userId).thenAccept(gameSession -> {
            gameSession.abort();
            log.info("Game aborted. Game session ID: {}, User ID: {}", gameSession.getId(), userId);
            gameSessionRepository.persistGameSession(gameSession);
        }).exceptionally(exception -> {
            log.error(exception.getMessage(), exception);
            return null;
        });
    }


    private CompletionStage<GameSession> createNewGameSession() {
        return meetingService.createMeeting().thenApply(meeting -> new BaseGameSession(NUMBER_OF_ROUNDS, meeting));
    }
}