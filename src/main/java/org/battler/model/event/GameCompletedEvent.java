package org.battler.model.event;

import org.battler.model.session.GameSession;

/**
 * Created by romanivanov on 14.09.2022
 */
public class GameCompletedEvent extends AbstractGameEvent {

    public GameCompletedEvent(final GameSession gameSession) {
        super(gameSession);
    }
}
