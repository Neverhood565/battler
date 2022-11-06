package org.battler.model.session;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.google.common.base.Preconditions;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.battler.model.UserId;
import org.battler.model.question.Question;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.battler.model.session.GameSessionState.PENDING;

@Slf4j
@Getter
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class GameSession {

    private static final int NUMBER_OF_PLAYERS = 2;

    private final ObjectId id;
    private final List<UserId> players;
    private final Integer numberOfRounds;
    private final List<Round> rounds;
    private final Meeting meeting;
    private final Date createDate;
    private Date startDate;
    private Integer currentRoundNumber;
    private GameSessionState state;
    private UserId winner;

    public static GameSession createNew(Integer numberOfRounds, Meeting meeting) {
        return GameSession
                .builder()
                .numberOfRounds(numberOfRounds)
                .meeting(meeting)
                .players(new ArrayList<>())
                .createDate(new Date())
                .state(PENDING)
                .id(new ObjectId())
                .rounds(new ArrayList<>())
                .build();
    }

    public void joinPlayer(UserId user, List<Question> questions) {

        if (players.contains(user)) {
            log.info("Player already joined to this session.");
            return;
        }

        Preconditions.checkState(
                players.size() < NUMBER_OF_PLAYERS,
                "Only %s players can join game session",
                NUMBER_OF_PLAYERS
        );
        Preconditions.checkArgument(
                questions.size() == numberOfRounds / 2,
                "Number of questions must be equal to numberOfRounds / 2"
        );
        Preconditions.checkState(
                state == PENDING,
                "Game session is in wrong state = %s",
                state
        );

        AtomicInteger roundNumber = new AtomicInteger(players.size() + 1);

        questions.forEach(question -> {
            rounds.add(Round.builder().questioner(user).question(question).roundNumber(roundNumber.get()).build());
            roundNumber.addAndGet((NUMBER_OF_PLAYERS));
        });

        state = PENDING;
        players.add(user);
    }

    public void start() {
        Preconditions.checkState(
                players.size() == NUMBER_OF_PLAYERS,
                "Can not start game with amount of players = %s",
                players.size()
        );
        rounds.sort(Comparator.comparing(Round::getRoundNumber));
        currentRoundNumber = 1;
        state = GameSessionState.ACTIVE;
        startDate = new Date();
    }

    public void answerQuestion(final UserId user, final String questionId, final Boolean correct) {
        Round currentRound = getCurrentRound();

        Preconditions.checkState(
                currentRound.getQuestioner().equals(user),
                "Only questioner can set round result"
        );

        Preconditions.checkState(
                currentRound.getQuestion().getId().equals(questionId),
                "Question ID not equal to question in current round"
        );

        currentRound.setCorrect(correct);

        if (Objects.equals(currentRoundNumber, numberOfRounds)) {
            complete();
            return;
        }
        currentRoundNumber++;
    }

    private void complete() {
        Map<UserId, Integer> correctAnswers = countCorrectAnswers();

        winner = correctAnswers
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow()
                .getKey();

        state = GameSessionState.COMPLETED;
    }

    private Map<UserId, Integer> countCorrectAnswers() {
        Map<UserId, Integer> correctAnswers = players.stream().collect(Collectors.toMap(
                Function.identity(),
                key -> 0
        ));

        rounds.stream()
              .filter(Round::getCorrect)
              .forEach(round -> correctAnswers.put(
                      round.getQuestioner(),
                      correctAnswers.get(round.getQuestioner()) + 1
              ));
        return correctAnswers;
    }

    public void abort() {
        state = GameSessionState.ABORTED;
    }

    public boolean readyToStart() {
        return players.size() == NUMBER_OF_PLAYERS;
    }

    public Round getCurrentRound() {
        return rounds.get(currentRoundNumber - 1);
    }
}
