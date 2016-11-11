package huckster.cabinet.web;

import huckster.cabinet.repository.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.EOFException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by PerevalovaMA on 09.11.2016.
 */
@ServerEndpoint(value = "/sync", configurator = UserAwareConfig.class)
public class WebSocketServer {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServer.class);
    private static Map<Integer, Set<Session>> sessions = new ConcurrentHashMap<>();
    private EndpointConfig config;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.config = config;
        UserData user = (UserData) config.getUserProperties().get("user");
        sessions.computeIfAbsent(user.getCompanyId(), v -> ConcurrentHashMap.newKeySet()).add(session);
    }

    @OnClose
    public void onClose(Session session) {
        UserData user = (UserData) config.getUserProperties().get("user");
        if (sessions.containsKey(user.getCompanyId())) {
            sessions.get(user.getCompanyId()).remove(session);
        }
    }

    @OnError
    public void onError(Throwable t) {
        if (t.getClass() != EOFException.class) {
            LOG.error("WebSocket error", t);
        }
    }

    public static void startSynchronization(int companyId) {
        for (Session session : sessions.get(companyId)) {
            if (session.isOpen()) {
                session.getAsyncRemote().sendText("show");
            }
        }
    }

    public static void endSynchronization(int companyId) {
        for (Session session : sessions.get(companyId)) {
            if (session.isOpen()) {
                session.getAsyncRemote().sendText("hide");
            }
        }
    }
}
