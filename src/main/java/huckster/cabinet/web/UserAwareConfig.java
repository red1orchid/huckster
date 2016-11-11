package huckster.cabinet.web;

import huckster.cabinet.repository.UserData;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * Created by PerevalovaMA on 09.11.2016.
 */
public class UserAwareConfig extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        UserData user = (UserData) ((HttpSession) request.getHttpSession()).getAttribute("userData");
        config.getUserProperties().put("user", user);
    }
}