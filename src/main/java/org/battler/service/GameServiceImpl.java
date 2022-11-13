package org.battler.service;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.battler.model.event.GameCompletedEvent;
import org.battler.model.event.GameEvent;
import org.battler.model.event.GameStartedEvent;
import org.battler.model.UserId;
import org.battler.model.event.LookingForGameEvent;
import org.battler.model.event.NewRoundEvent;
import org.battler.model.event.RejoinGameEvent;
import org.battler.model.event.UserLeftGameEvent;
import org.battler.model.session.GameSession;
import org.battler.model.session.GameSessionState;
import org.battler.model.session.QuestionType;
import org.battler.repository.GameSessionRepository;
import org.battler.repository.QuestionRepository;
import org.battler.service.meeting.MeetingService;
import org.battler.socket.UserEventsEmitter;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedStage;
import static org.battler.model.session.GameSessionState.ACTIVE;

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
    public void findGame(UserId user, QuestionType questionsType) {
        gameSessionRepository
                .findPendingGameSessionByQuestionType(questionsType)
                .thenCompose(gameSession -> {
                                 if (gameSession == null) {
                                     return completedStage(GameSession.createNew(
                                             NUMBER_OF_ROUNDS,
                                             questionsType
                                     ));
                                 }
                                 return completedStage(gameSession);
                             }
                ).thenCombine(
                        questionRepository.getNextRandomQuestions(NUMBER_OF_ROUNDS / 2, questionsType),
                        (gameSession, questions) -> {
                            gameSession.joinPlayer(user, questions);
                            return gameSession;
                        }
                ).thenCompose(gameSession -> {
                                  if (gameSession.readyToStart()) {
                                      return startGame(user, gameSession);
                                  } else {
                                      log.info("Game session pending. Game session ID: {}, User ID: {}",
                                               gameSession.getId(), user
                                      );
                                      gameEvents.fire(new LookingForGameEvent(gameSession));
                                      return completedStage(gameSession);
                                  }
                              }
                ).thenAccept(gameSessionRepository::persistGameSession)
                .exceptionally(exception ->
                               {
                                   log.error(
                                           exception.getMessage(),
                                           exception
                                   );
                                   return null;
                               });
    }


    private CompletionStage<GameSession> startGame(final UserId user, final GameSession gameSession) {
        return meetingService.createMeeting()
                             .thenApply(meeting -> {
                                 gameSession.start(meeting);
                                 gameEvents.fire(new GameStartedEvent(gameSession));
                                 gameEvents.fire(new NewRoundEvent(gameSession));
                                 log.info(
                                         "Game session started. Game session ID: {}, User ID: {}",
                                         gameSession.getId(),
                                         user
                                 );
                                 return gameSession;
                             });
    }

    @Override
    public void answerQuestion(UserId user, String questionId, Boolean correct) {
        gameSessionRepository.findGameSessionByUserIdAndState(user, ACTIVE).thenAccept(gameSession -> {

            Preconditions.checkNotNull(gameSession, "There is no active game session for user id = %s", user);

            gameSession.answerQuestion(
                    user,
                    questionId,
                    correct
            );

            log.info("Question answered. Game session ID: {}, User ID: {}", gameSession.getId(), user);

            if (gameSession.getState().equals(ACTIVE)) {

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
        gameSessionRepository.findGameSessionByUserIdAndState(user, ACTIVE).thenAccept(gameSession -> {
            Preconditions.checkNotNull(gameSession, "There is no active game sessions to abort. For user = %s", user);
            gameSession.abort();
            log.info("Game aborted. Game session ID: {}, User ID: {}", gameSession.getId(), user);
            gameSessionRepository.persistGameSession(gameSession);
        }).exceptionally(exception -> {
            log.error(exception.getMessage(), exception);
            return null;
        });
    }

    @Override
    public void rejoinGame(final UserId user) {
        gameSessionRepository.findGameSessionByUserIdAndState(user, ACTIVE).thenAccept(gameSession -> {
            log.info(
                    "Active game session found. Send rejoin events. User = {}. Game session id = {}",
                    user,
                    gameSession.getId()
            );
            gameEvents.fire(new RejoinGameEvent(gameSession));
            gameEvents.fire(new NewRoundEvent(gameSession));
        });
    }

    @Override
    public void userLeft(final UserId user) {
        gameSessionRepository.findGameSessionByUserIdAndState(user, ACTIVE).thenAccept(gameSession -> {
            log.info(
                    "User leaving active game session. Userid = {}. Game session id = {}",
                    user,
                    gameSession.getId()
            );
            gameEvents.fire(new UserLeftGameEvent(gameSession, user));
        });
    }
}
