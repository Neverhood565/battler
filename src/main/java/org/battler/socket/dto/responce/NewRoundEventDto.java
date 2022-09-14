package org.battler.socket.dto.responce;

import lombok.Builder;
import lombok.Data;

/**
 * Created by romanivanov on 14.09.2022
 */
@Data
@Builder
public class NewRoundEventDto extends GameEventDto {

    public static final String TYPE = "nextRound";

    private final String questionId;
    private final String questionText;
    private final String correctAnswer;
    private final Boolean asking;
    private final Integer currentRoundNumber;
    private final Integer totalRounds;

    @Override
    public String getEventType() {
        return TYPE;
    }
}
