package org.battler.service;

import org.battler.model.User;
import org.battler.model.event.GameCompletedEvent;
import org.battler.model.event.GameStartedEvent;
import org.battler.model.event.NewRoundEvent;
import org.battler.model.sessions.BaseGameSession;
import org.battler.model.sessions.GameSession;
import org.battler.model.sessions.Round;
import org.battler.socket.UserEventsEmitter;
import org.battler.socket.dto.responce.GameCompletedEventDto;
import org.battler.socket.dto.responce.GameEventDto;
import org.battler.socket.dto.responce.GameStartedEventDto;
import org.battler.socket.dto.responce.NewRoundEventDto;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.stream.Collectors;

/**
 * Created by romanivanov on 10.09.2022
 */
@ApplicationScoped
public class GameToUserEventsDispatcher {

    @Inject UserEventsEmitter userEventsEmitter;

    void onGameStarted(@Observes GameStartedEvent startedEvent) {
        GameSession gameSession = startedEvent.getGameSession();

        GameEventDto eventDto = toGameStartedDto(gameSession);
        gameSession.getPlayers().forEach(player -> userEventsEmitter.emitMessage(eventDto, player));
    }

    void onNextRound(@Observes NewRoundEvent newRoundEvent) {
        GameSession gameSession = newRoundEvent.getGameSession();
        gameSession.getPlayers().forEach(
                player -> userEventsEmitter.emitMessage(
                        toNewRoundEventDto(gameSession.getCurrentRound(), player),
                        player
                )
        );
    }

    void onGameCompleted(@Observes GameCompletedEvent gameCompletedEvent) {
        GameSession gameSession = gameCompletedEvent.getGameSession();
        gameSession.getPlayers().forEach(
                player -> userEventsEmitter.emitMessage(toGameCompletedDto(gameSession, player), player)
        );
    }

    private GameStartedEventDto toGameStartedDto(GameSession gameSession) {
        return GameStartedEventDto
                .builder()
                .meetingId(gameSession.getMeetingInfo().getId())
                .roomName(gameSession.getMeetingInfo().getRoomName())
                .build();
    }

    private NewRoundEventDto toNewRoundEventDto(Round round, User user) {
        boolean userToAsk = user.getId().equals(round.getPlayerToAsk().getId());
        return NewRoundEventDto.builder()
                               .currentRoundNumber(round.getRoundNumber())
                               .asking(userToAsk)
                               .questionId(round.getQuestion().getId())
                               .questionText(round.getQuestion().getTitle())
                               .correctAnswer(userToAsk ? round.getQuestion().getCorrectAnswer().getTitle() : null)
                               .build();
    }

    private GameCompletedEventDto toGameCompletedDto(GameSession gameSession, User user) {
        return GameCompletedEventDto.builder()
                                    .win(user.equals(gameSession.getWinner())).build();
    }
}
