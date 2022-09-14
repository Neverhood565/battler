package org.battler.model.sessions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import lombok.Data;
import lombok.Getter;
import org.battler.model.User;
import org.battler.model.question.Question;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by romanivanov on 28.08.2022
 */

@Getter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class BaseGameSession implements GameSession {

    private final String id = UUID.randomUUID().toString();
    private final Map<User, PlayerSession> playerSessions = new HashMap<>();
    private final Integer numberOfRounds;
    private final List<Round> rounds;
    private final Meeting meeting;
    private GameState state = GameState.PENDING;
    private User winner;
    private Boolean draw;

    public BaseGameSession(final Integer numberOfRounds, final Meeting meeting) {
        this.numberOfRounds = numberOfRounds;
        this.meeting = meeting;
        this.rounds = new ArrayList<>(numberOfRounds);
    }

    @Override
    public void joinPlayer(User user, List<Question> questions) {

        Preconditions.checkState(playerSessions.size() < 2, "Only 2 players can join game session");
        Preconditions.checkArgument(questions.size() >= numberOfRounds / 2, "Not enough questions");

        playerSessions.put(user, new PlayerSession(questions));
    }

    @Override
    public void start() {
        Preconditions.checkState(
                playerSessions.size() == 2,
                "Can not start game with amount of players = %s",
                playerSessions.size()
        );
        startFirstRound();
        state = GameState.ACTIVE;
    }

    @Override
    public void answerQuestion(final String questionId, final Boolean correct) {
        Round currentRound = getCurrentRound();

        Preconditions.checkState(
                currentRound.getQuestion().getId().equals(questionId),
                "Question ID not equal to question in current round"
        );

        currentRound.setCorrect(correct);
        if (correct) {
            playerSessions.get(currentRound.getPlayerToAnswer()).numberOfCorrectAnswers++;
        }
        if (rounds.size() == numberOfRounds) {
            complete();
            return;
        }
        startNextRound();
    }

    private void complete() {
        if (playerSessions.values().stream()
                          .map(PlayerSession::getNumberOfCorrectAnswers)
                          .collect(Collectors.toSet()).size() == 1) {
            draw = true;
        } else {
            winner = playerSessions
                    .entrySet()
                    .stream().max(Comparator.comparing(entry -> entry.getValue().numberOfCorrectAnswers))
                    .orElseThrow().getKey();
        }
        state = GameState.COMPLETED;
    }

    @Override
    public void abort() {
        state = GameState.ABORTED;
    }

    @Override
    public boolean readyToStart() {
        return playerSessions.size() == 2;
    }

    @Override
    public Round getCurrentRound() {
        return rounds.stream().reduce((first, second) -> second).orElse(null);
    }

    @Override
    public Meeting getMeetingInfo() {
        return meeting;
    }

    @Override
    public List<User> getPlayers() {
        return new ArrayList<>(playerSessions.keySet());
    }

    private void startFirstRound() {
        List<User> players = new ArrayList<>(playerSessions.keySet());
        Round firstRound = Round.builder()
                                .playerToAsk(players.get(0))
                                .playerToAnswer(players.get(1))
                                .question(playerSessions.get(players.get(0)).getNextQuestion())
                                .roundNumber(1)
                                .build();
        rounds.add(firstRound);
    }

    private void startNextRound() {
        Round current = getCurrentRound();
        Round nextRound = Round.builder()
                               .playerToAsk(current.getPlayerToAnswer())
                               .playerToAnswer(current.getPlayerToAsk())
                               .question(playerSessions.get(current.getPlayerToAnswer()).getNextQuestion())
                               .roundNumber(current.getRoundNumber() + 1)
                               .build();
        rounds.add(nextRound);
    }

    @Data
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
    private static class PlayerSession {

        private final List<Question> questions;
        private Integer currentQuestion = 0;
        private Integer numberOfCorrectAnswers = 0;


        private Question getNextQuestion() {
            return questions.get(currentQuestion++);
        }
    }
}
