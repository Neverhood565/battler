package org.battler.service;

import org.battler.model.UserId;
import org.battler.model.event.GameCompletedEvent;
import org.battler.model.event.GameStartedEvent;
import org.battler.model.event.NewRoundEvent;
import org.battler.model.question.Question;
import org.battler.model.session.GameSession;
import org.battler.model.session.Round;
import org.battler.socket.UserEventsEmitter;
import org.battler.socket.dto.responce.GameCompletedEventDto;
import org.battler.socket.dto.responce.GameEventDto;
import org.battler.socket.dto.responce.GameStartedEventDto;
import org.battler.socket.dto.responce.NewRoundEventDto;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

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
        gameSession.getPlayers().forEach(
                player -> emitters.forEach(emitter -> emitter.emitMessage(eventDto, player))
        );
    }

    void onNextRound(@Observes NewRoundEvent newRoundEvent) {
        GameSession gameSession = newRoundEvent.getGameSession();
        gameSession.getPlayers().forEach(
                player -> {
                    NewRoundEventDto newRoundEventDto = toNewRoundEventDto(gameSession.getCurrentRound(), player);
                    emitters.forEach(emitter -> emitter.emitMessage(newRoundEventDto, player));
                }
        );
    }

    void onGameCompleted(@Observes GameCompletedEvent gameCompletedEvent) {
        GameSession gameSession = gameCompletedEvent.getGameSession();
        gameSession.getPlayers().forEach(
                player -> {
                    GameCompletedEventDto gameCompletedEventDto = toGameCompletedDto(gameSession, player);
                    emitters.forEach(emitter -> emitter.emitMessage(gameCompletedEventDto, player));
                }
        );
    }

    private GameStartedEventDto toGameStartedDto(GameSession gameSession) {
        return GameStartedEventDto
                .builder()
                .meetingId(gameSession.getMeeting().getId())
                .roomName(gameSession.getMeeting().getRoomName())
                .build();
    }

    private NewRoundEventDto toNewRoundEventDto(Round round, UserId userId) {
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
}
