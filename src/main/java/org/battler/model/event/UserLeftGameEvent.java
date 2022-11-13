package org.battler.model.event;

import lombok.Getter;
import org.battler.model.UserId;
import org.battler.model.session.GameSession;

/**
 * Created by romanivanov on 13.11.2022
 */
public class UserLeftGameEvent extends AbstractGameEvent {

    @Getter
    private final UserId user;

    public UserLeftGameEvent(final GameSession gameSession, final UserId user) {
        super(gameSession);
        this.user = user;
    }
}
