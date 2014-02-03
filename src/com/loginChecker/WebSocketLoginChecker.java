package com.loginChecker;

import com.client.MobileClientWithWebSocket;
import com.synchronizer.WebSocketServerEndPoint;
import com.topicAgent.TopicToSocketBrocker;

import javax.jms.JMSException;
import javax.websocket.Session;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 03.02.14
 * Time: 20:56
 * To change this template use File | Settings | File Templates.
 */
public class WebSocketLoginChecker extends LoginChecker {
    private Session webSocketSession;
    private String loginInfo;
    private WebSocketServerEndPoint webSocketServerEndPoint;

    public WebSocketLoginChecker(Session webSocketSession, TopicToSocketBrocker topicToSocketBrocker) {
        this.webSocketSession = webSocketSession;
        this.topicToSocketBrocker = topicToSocketBrocker;
    }

    public Session getWebSocketSession() {
        return webSocketSession;
    }

    public void setWebSocketSession(Session webSocketSession) {
        this.webSocketSession = webSocketSession;
    }

    @Override
    public void run() {
        if (checkLogin(loginInfo)) {
            String id = getIdFromLoginInfo(loginInfo);
            webSocketSession.getAsyncRemote().sendText(loginOkAnswerTemplate);
            MobileClientWithWebSocket mobileClientWithWebSocket = new MobileClientWithWebSocket(webSocketSession);
            mobileClientWithWebSocket.setMobileClientId(id);
            try {
                topicToSocketBrocker.addClient(mobileClientWithWebSocket);
            } catch (JMSException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            webSocketServerEndPoint.setMobileClient(id, mobileClientWithWebSocket);
        } else {
            webSocketSession.getAsyncRemote().sendText(loginFailedAnswerTemplate);
            webSocketServerEndPoint.setMobileClient(null, null);
        }
    }

    public String getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(String loginInfo) {
        this.loginInfo = loginInfo;
    }

    public WebSocketServerEndPoint getWebSocketServerEndPoint() {
        return webSocketServerEndPoint;
    }

    public void setWebSocketServerEndPoint(WebSocketServerEndPoint webSocketServerEndPoint) {
        this.webSocketServerEndPoint = webSocketServerEndPoint;
    }
}
