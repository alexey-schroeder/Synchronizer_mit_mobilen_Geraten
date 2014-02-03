package com.synchronizer;

import com.topicAgent.TopicToSocketBrocker;
import org.glassfish.tyrus.server.Server;

import javax.websocket.DeploymentException;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 19.01.14
 * Time: 22:40
 * To change this template use File | Settings | File Templates.
 */
public class Synchronizer {
    private int socketPort;
    private int webSocketPort;
    private TopicToSocketBrocker topicToSocketBrocker;

    public Synchronizer(int socketPort, int webSocketPort) {
        this.socketPort = socketPort;
        this.webSocketPort = webSocketPort;
    }

    public void setTopicToSocketBrocker(TopicToSocketBrocker topicToSocketBrocker) {
        this.topicToSocketBrocker = topicToSocketBrocker;
    }

    public void start() throws IOException, DeploymentException {
        startServerSocket();
        startWebSocketServer();
    }

    private void startServerSocket() throws IOException {
        SocketServer socketServer = new SocketServer(socketPort);
        socketServer.setTopicToSocketBrocker(topicToSocketBrocker);
        socketServer.start();
    }

    private void startWebSocketServer() throws DeploymentException {
        System.out.println("Starte WebServerSocket on socketPort " + webSocketPort);
        Server server = new Server("localhost", 2222, "", WebSocketServerEndPoint.class);
        server.start();
    }
}
