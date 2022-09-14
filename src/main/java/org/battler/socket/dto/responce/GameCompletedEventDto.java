package org.battler.socket.dto.responce;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * Created by romanivanov on 14.09.2022
 */
@Data
@Builder
public class GameCompletedEventDto extends GameEventDto {

    private static final String TYPE = "gameCompleted";

    private final Boolean win;
    private final Map<String, Integer> correctAnswers;

    @Override
    public String getEventType() {
        return TYPE;
    }
}
