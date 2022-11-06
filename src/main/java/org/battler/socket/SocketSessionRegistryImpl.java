package org.battler.socket;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by romanivanov on 10.09.2022
 */
@ApplicationScoped
public class SocketSessionRegistryImpl implements SocketSessionRegistry {

    private final Map<String, Session> sessionByUserId = new ConcurrentHashMap<>();
    private final Map<String, String> userIdBySessionId = new ConcurrentHashMap<>();


    @Override
    public void registerSession(final Session session, final String userId) {
        Preconditions.checkNotNull(session, "Session is null");
        Preconditions.checkArgument(StringUtils.isNotBlank(userId), "User id is blank");

        sessionByUserId.put(userId, session);
        userIdBySessionId.put(session.getId(), userId);
    }

    @Override
    public Session getSessionByUserId(final String userId) {
        return sessionByUserId.get(userId);
    }

    @Override
    public String getUserIdBySessionId(final String sessionId) {
        return userIdBySessionId.get(sessionId);
    }

    @Override
    public void removeSession(final String sessionId) {
        Optional.ofNullable(userIdBySessionId.remove(sessionId))
                .ifPresent(sessionByUserId::remove);
    }
}
