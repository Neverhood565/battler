package org.battler.service;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.battler.model.event.GameCompletedEvent;
import org.battler.model.event.GameEvent;
import org.battler.model.event.GameStartedEvent;
import org.battler.model.UserId;
import org.battler.model.event.NewRoundEvent;
import org.battler.model.question.Question;
import org.battler.model.session.GameSession;
import org.battler.model.session.GameSessionState;
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
    public void findGame(UserId user) {
        gameSessionRepository.findAvailableGameSession().thenCompose(gameSession -> {
            if (gameSession == null) {
                return createNewGameSession();
            }
            return CompletableFuture.completedStage(gameSession);
        }).thenAcceptBoth(
                questionRepository.getNextRandomQuestions(NUMBER_OF_ROUNDS / 2),
                (gameSession, questions) -> {
                    gameSession.joinPlayer(user, questions);
                    if (gameSession.readyToStart()) {

                        gameSession.start();
                        gameEvents.fire(new GameStartedEvent(gameSession));
                        gameEvents.fire(new NewRoundEvent(gameSession));
                        log.info("Game session started. Game session ID: {}, User ID: {}", gameSession.getId(), user);

                    } else {
                        log.info("Game session pending. Game session ID: {}, User ID: {}", gameSession.getId(), user);
                    }
                    gameSessionRepository.persistGameSession(gameSession);
                }
        ).exceptionally(exception -> {
            log.error(exception.getMessage(), exception);
            return null;
        });
    }

    @Override
    public void answerQuestion(UserId user, String questionId, Boolean correct) {
        gameSessionRepository.findActiveGameSessionByUserId(user).thenAccept(gameSession -> {

            Preconditions.checkNotNull(gameSession, "There is no active game session for user id = %s", user);

            gameSession.answerQuestion(
                    user,
                    questionId,
                    correct
            );

            log.info("Question answered. Game session ID: {}, User ID: {}", gameSession.getId(), user);

            if (gameSession.getState().equals(GameSessionState.ACTIVE)) {

                gameEvents.fire(new NewRoundEvent(gameSession));
                log.info("New round started. Game session ID: {}, User ID: {}", gameSession.getId(), user);

            } else if (gameSession.getState().equals(GameSessionState.COMPLETED)) {

                gameEvents.fire(new GameCompletedEvent(gameSession));
                log.info("Game completed. Game session ID: {}, User ID: {}", gameSession.getId(), user);

            }
            gameSessionRepository.persistGameSession(gameSession);
        }).exceptionally(exception -> {
            log.error(exception.getMessage(), exception);
            return null;
        });
    }

    @Override
    public void leaveGame(final UserId user) {
        gameSessionRepository.findActiveGameSessionByUserId(user).thenAccept(gameSession -> {
            gameSession.abort();
            log.info("Game aborted. Game session ID: {}, User ID: {}", gameSession.getId(), user);
            gameSessionRepository.persistGameSession(gameSession);
        }).exceptionally(exception -> {
            log.error(exception.getMessage(), exception);
            return null;
        });
    }


    private CompletionStage<GameSession> createNewGameSession() {
        return meetingService.createMeeting()
                             .thenApply(meeting -> GameSession.createNew(NUMBER_OF_ROUNDS, meeting));
    }
}
