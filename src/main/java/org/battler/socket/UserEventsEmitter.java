package org.battler.socket;

import org.battler.model.User;

/**
 *
 */
public interface UserEventsEmitter {

    public void emitMessage(Object message, User user);
}
