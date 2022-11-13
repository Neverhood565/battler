package org.battler.socket.dto.responce;

/**
 * Created by romanivanov on 13.11.2022
 */
public class UserLeftGameEventDto extends GameEventDto {

    @Override
    public String getEventType() {
        return "userLeft";
    }
}
