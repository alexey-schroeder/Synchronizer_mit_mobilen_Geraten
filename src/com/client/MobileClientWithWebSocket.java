package com.client;

import javax.websocket.Session;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 03.02.14
 * Time: 21:49
 * To change this template use File | Settings | File Templates.
 */
public class MobileClientWithWebSocket extends MobileClient {
    private Session session;

    public MobileClientWithWebSocket(Session session) {
        this.session = session;
    }

    @Override
    public void writeMessage(String message) {
        session.getAsyncRemote().sendText(message);
    }
}
