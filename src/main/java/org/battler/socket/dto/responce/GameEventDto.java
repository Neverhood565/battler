package org.battler.socket.dto.responce;

import lombok.Data;

/**
 * Created by romanivanov on 31.08.2022
 */
@Data
public abstract class GameEventDto {

    public abstract String getEventType();
    private final Boolean success = true;

}
