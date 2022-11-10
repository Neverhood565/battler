package org.battler.repository.mongo.entity;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Getter;
import lombok.Setter;
import org.battler.model.UserId;
import org.battler.model.session.GameSessionState;
import org.battler.model.session.QuestionType;

import java.util.Date;
import java.util.List;

/**
 * Created by romanivanov on 06.11.2022
 */
@Getter
@Setter
@MongoEntity(collection = "gameSession")
public class GameSessionEntity extends PanacheMongoEntity {

    private List<UserId> players;
    private Integer numberOfRounds;
    private List<RoundEntity> rounds;
    private MeetingEntity meeting;
    private Integer currentRoundNumber;
    private GameSessionState state;
    private UserId winner;
    private Date createDate;
    private Date startDate;
    private QuestionType questionType;
}
