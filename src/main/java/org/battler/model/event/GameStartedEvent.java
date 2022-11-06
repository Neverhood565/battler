package org.battler.model.event;

import org.battler.model.session.GameSession;

/**
 * Created by romanivanov on 10.09.2022
 */

public class GameStartedEvent extends AbstractGameEvent {

    public GameStartedEvent(final GameSession gameSession) {
        super(gameSession);
    }
}
