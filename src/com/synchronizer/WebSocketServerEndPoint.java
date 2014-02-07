package com.synchronizer;

import com.client.MobileClient;
import com.client.MobileClientWithWebSocket;
import com.loginChecker.WebSocketLoginChecker;
import com.parsers.XMLParser;
import com.topicAgent.TopicToSocketBrocker;

import javax.jms.JMSException;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 02.02.14
 * Time: 21:58
 * To change this template use File | Settings | File Templates.
 */
@ServerEndpoint(value = "/")
public class WebSocketServerEndPoint {
    private Session session;
    private boolean isLoged;
    private boolean firstChecking = true;
    private String clientId;
    private MobileClientWithWebSocket clientWithWebSocket;

    public WebSocketServerEndPoint() {
        System.out.println("Endpoint startet");
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig conf) {
        this.session = session;
        System.out.println("websocket session opened: " + session);
    }

    @OnMessage
    public void onMessage(Session session, String text) {
        System.out.println("message from websocket: " + text);
        if (!isLoged && firstChecking) {
            firstChecking = false;
            WebSocketLoginChecker webSocketLoginChecker = new WebSocketLoginChecker(session, TopicToSocketBrocker.getInstance());
            webSocketLoginChecker.setLoginInfo(text);
            webSocketLoginChecker.setWebSocketServerEndPoint(this);
            webSocketLoginChecker.start();
        } else if (isLoged) {
            try {
                if(XMLParser.isValid(text)){
                TopicToSocketBrocker.getInstance().writeMessageInTopic(text, clientId);
                } else {
                    session.getAsyncRemote().sendText(MobileClient.notValidMessageTemplate);
                }
            } catch (JMSException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        try {
            System.out.println("Session for client " + clientId + " closed");
            TopicToSocketBrocker.getInstance().removeMobileClient(clientId, clientWithWebSocket);
            session.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void setMobileClient(String clientId, MobileClientWithWebSocket clientWithWebSocket) {
        if (clientId == null) {
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } else {
            this.isLoged = true;
            this.clientId = clientId;
            this.clientWithWebSocket = clientWithWebSocket;
        }
    }
}
