package org.battler.socket.dto.responce;

import lombok.Builder;
import lombok.Data;

/**
 * Created by romanivanov on 13.11.2022
 */
@Data
@Builder
public class RejoinGameEventDto extends GameEventDto {

    private final String meetingId;
    private final String roomName;

    @Override
    public String getEventType() {
        return "rejoinGame";
    }
}
