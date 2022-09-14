package org.battler.socket;

import lombok.extern.slf4j.Slf4j;
import org.battler.model.User;
import org.battler.urils.JsonUtils;

import javax.enterprise.context.ApplicationScoped;

/**
 * Created by romanivanov on 14.09.2022
 */
@Slf4j
//@ApplicationScoped
public class LogEventsEmitter implements UserEventsEmitter {

    @Override
    public void emitMessage(final Object message, final User user) {
        log.info("Emitting message. User: {}, message {}", JsonUtils.toJson(user), JsonUtils.toJson(message));
    }
}
