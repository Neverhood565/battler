package org.battler.model.event;

import org.battler.model.session.GameSession;

/**
 * Created by romanivanov on 13.11.2022
 */
public class LookingForGameEvent extends AbstractGameEvent {

    public LookingForGameEvent(final GameSession gameSession) {
        super(gameSession);
    }
}
