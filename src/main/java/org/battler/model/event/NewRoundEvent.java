package org.battler.model.event;

import org.battler.model.sessions.GameSession;

/**
 * Created by romanivanov on 14.09.2022
 */
public class NewRoundEvent extends AbstractGameEvent {

    public NewRoundEvent(final GameSession gameSession) {
        super(gameSession);
    }
}
