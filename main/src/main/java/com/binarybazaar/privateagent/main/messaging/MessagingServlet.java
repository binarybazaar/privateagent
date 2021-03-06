package com.binarybazaar.privateagent.main.messaging;

import com.binarybazaar.privateagent.main.configuration.LocatorServiceLookup;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class MessagingServlet extends WebSocketServlet {

    private static final int MAX_MESSAGE_SIZE = 1024 * 1024 * 10;//10MB
    private static final int MAX_MESSAGE_BUFFER_SIZE = 1024 * 1024 * 50;//50MB

    @Override
    public void configure(WebSocketServletFactory factory) {

        factory.getPolicy().setMaxBinaryMessageSize(MAX_MESSAGE_SIZE);
        factory.getPolicy().setMaxBinaryMessageBufferSize(MAX_MESSAGE_BUFFER_SIZE);

        // Register your Adapater
        factory.register(MessagingRouterAdapter.class);

        // Get the current creator (for reuse)
        final WebSocketCreator creator = factory.getCreator();

        // Set your custom Creator
        factory.setCreator((ServletUpgradeRequest servletUpgradeRequest, ServletUpgradeResponse servletUpgradeResponse) -> {
            Object webSocket = creator.createWebSocket(servletUpgradeRequest, servletUpgradeResponse);

            // Use the object created by the default creator and inject your members
            LocatorServiceLookup.SERVICE_LOCATOR.inject(webSocket);
            LocatorServiceLookup.SERVICE_LOCATOR.postConstruct(webSocket);
            return webSocket;
        });
    }
}
