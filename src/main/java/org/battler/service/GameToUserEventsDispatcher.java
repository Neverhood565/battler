package org.battler.service;

import org.battler.model.UserId;
import org.battler.model.event.GameCompletedEvent;
import org.battler.model.event.GameStartedEvent;
import org.battler.model.event.LookingForGameEvent;
import org.battler.model.event.NewRoundEvent;
import org.battler.model.event.RejoinGameEvent;
import org.battler.model.event.UserLeftGameEvent;
import org.battler.model.question.Question;
import org.battler.model.session.GameSession;
import org.battler.model.session.Round;
import org.battler.socket.UserEventsEmitter;
import org.battler.socket.dto.responce.GameCompletedEventDto;
import org.battler.socket.dto.responce.GameEventDto;
import org.battler.socket.dto.responce.GameStartedEventDto;
import org.battler.socket.dto.responce.LookingForGameEventDto;
import org.battler.socket.dto.responce.NewRoundEventDto;
import org.battler.socket.dto.responce.RejoinGameEventDto;
import org.battler.socket.dto.responce.UserLeftGameEventDto;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.function.BiFunction;

/**
 * Created by romanivanov on 10.09.2022
 */
@ApplicationScoped
public class GameToUserEventsDispatcher {

    @Inject
    Instance<UserEventsEmitter> emitters;

    void onGameStarted(@Observes GameStartedEvent startedEvent) {
        GameSession gameSession = startedEvent.getGameSession();
        GameEventDto eventDto = toGameStartedDto(gameSession);
        sendCommonEvent(gameSession, eventDto);
    }

    void onNextRound(@Observes NewRoundEvent newRoundEvent) {
        GameSession gameSession = newRoundEvent.getGameSession();
        sendPersonalEvent(gameSession, this::toNewRoundEventDto);
    }

    void onGameCompleted(@Observes GameCompletedEvent gameCompletedEvent) {
        GameSession gameSession = gameCompletedEvent.getGameSession();
        sendPersonalEvent(gameSession, this::toGameCompletedDto);
    }

    void onLookingForGame(@Observes LookingForGameEvent lookingForGameEvent) {
        sendCommonEvent(lookingForGameEvent.getGameSession(), new LookingForGameEventDto());
    }

    void onRejoinGame(@Observes RejoinGameEvent rejoinGameEvent) {
        sendCommonEvent(rejoinGameEvent.getGameSession(), toRejoinGameEventDto(rejoinGameEvent.getGameSession()));
    }

    void onUserLeft(@Observes UserLeftGameEvent userLeftGameEvent) {
        sendCommonEvent(userLeftGameEvent.getGameSession(), new UserLeftGameEventDto());
    }

    private void sendCommonEvent(GameSession gameSession, GameEventDto gameEventDto) {
        gameSession.getPlayers().forEach(player -> {
            emitters.forEach(emitter -> emitter.emitMessage(gameEventDto, player));
        });
    }

    private void sendPersonalEvent(GameSession gameSession, BiFunction<GameSession, UserId, GameEventDto> eventMapper) {
        gameSession.getPlayers().forEach(player -> {
            GameEventDto gameEventDto = eventMapper.apply(gameSession, player);
            emitters.forEach(emitter -> emitter.emitMessage(gameEventDto, player));
        });
    }

    private GameStartedEventDto toGameStartedDto(GameSession gameSession) {
        return GameStartedEventDto
                .builder()
                .meetingId(gameSession.getMeeting().getId())
                .roomName(gameSession.getMeeting().getRoomName())
                .build();
    }

    private NewRoundEventDto toNewRoundEventDto(GameSession gameSession, UserId userId) {
        Round round = gameSession.getCurrentRound();
        boolean questioner = userId.equals(round.getQuestioner());
        Question question = round.getQuestion();
        return NewRoundEventDto.builder()
                               .currentRoundNumber(round.getRoundNumber())
                               .asking(questioner)
                               .questionId(question.getId())
                               .questionText(question.getTitle())
                               .correctAnswer(questioner ? question.getCorrectAnswer() : null)
                               .build();
    }

    private GameCompletedEventDto toGameCompletedDto(GameSession gameSession, UserId user) {
        return GameCompletedEventDto.builder()
                                    .win(user.equals(gameSession.getWinner())).build();
    }

    private RejoinGameEventDto toRejoinGameEventDto(GameSession gameSession) {
        return RejoinGameEventDto
                .builder()
                .meetingId(gameSession.getMeeting().getId())
                .roomName(gameSession
                                  .getMeeting()
                                  .getRoomName())
                .build();
    }
}
