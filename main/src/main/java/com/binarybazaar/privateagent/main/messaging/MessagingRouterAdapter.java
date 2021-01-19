package com.binarybazaar.privateagent.main.messaging;

import javax.inject.Inject;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessagingRouterAdapter extends WebSocketAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagingRouterAdapter.class);
    private Session session;

    @Inject
    private MessagingHandler mh;

    @Override
    public void onWebSocketConnect(Session session) {
        this.session = session;
        super.onWebSocketConnect(session);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
//        switch (getServiceName(session.getUpgradeRequest().getRequestURI().getPath(), Config.get().basePath())) {
        mh.removeSession(session);
//        LOGGER.debug("Socket Closed: [ {} ] {}", statusCode, reason);
        super.onWebSocketClose(statusCode, reason);
    }

    @Override
    public void onWebSocketText(String message) {
        LOGGER.debug("received Message TEXT : {}", message);
        super.onWebSocketText(message);
    }

    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len) {
        try {
            mh.handleBinaryFromClient(session, payload);
        } catch (Exception ex) {
            LOGGER.error("Problem handling message!", ex);
        }
        super.onWebSocketBinary(payload, offset, len);
    }

}
