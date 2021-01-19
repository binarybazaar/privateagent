package com.binarybazaar.privateagent.main.messaging;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class MessagingHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagingHandler.class);

    private final Map<String, Session> sessionIds;
    private final Map<Session, String> map;


    @Inject
    public MessagingHandler() {
        this.sessionIds = new ConcurrentHashMap<>();
        this.map = new ConcurrentHashMap<>();
    }

    public void handleBinaryFromClient(Session session, byte[] payload) {
        
    }

    public void sendBinaryToClient(byte[] payload) throws IOException {

    }


    private void verifySession(String commId, Session session) {

        Session s = sessionIds.get(commId);

        if (Objects.equals(s, session)) {
            return;
        }

        sessionIds.put(commId, session);
        map.put(session, commId);
    }

    private Session findSession(String commId) {

        Session session = sessionIds.get(commId);
        if (session == null) {

//            LOGGER.trace("Session is null!");
            return null;
        } else if (!session.isOpen()) {
            LOGGER.trace("Session is not open. Well... Gotta try again.");
            return null;
        }
        return session;
    }

    public void removeSession(Session session) {
        if (!session.isOpen()) {
            String commId = map.get(session);
            map.remove(session);
            sessionIds.remove(commId);
        }
    }

}
