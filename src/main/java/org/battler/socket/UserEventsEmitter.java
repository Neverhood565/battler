package org.battler.socket;

import org.battler.model.UserId;

/**
 *
 */
public interface UserEventsEmitter {

    void emitMessage(Object message, UserId userId);
}
