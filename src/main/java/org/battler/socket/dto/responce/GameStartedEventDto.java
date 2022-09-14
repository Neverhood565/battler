package org.battler.socket.dto.responce;

import lombok.Builder;
import lombok.Data;

/**
 * Created by romanivanov on 14.09.2022
 */
@Data
@Builder
public class GameStartedEventDto extends GameEventDto {

    public static final String TYPE = "gameStarted";

    private final String meetingId;
    private final String roomName;

    @Override
    public String getEventType() {
        return TYPE;
    }
}
