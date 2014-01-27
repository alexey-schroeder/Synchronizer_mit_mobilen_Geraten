package com.synchronizer;

import com.loginChecker.LoginChecker;
import com.topicAgent.TopicToSocketBrocker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 19.01.14
 * Time: 22:40
 * To change this template use File | Settings | File Templates.
 */
public class Synchronizer {
    private int port;
    private TopicToSocketBrocker topicToSocketBrocker;
    public Synchronizer(int port) {
       this.port = port;
    }

    public void setTopicToSocketBrocker(TopicToSocketBrocker topicToSocketBrocker) {
        this.topicToSocketBrocker = topicToSocketBrocker;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        while(true){
            Socket client = serverSocket.accept();
            LoginChecker loginChecker = new LoginChecker(client, topicToSocketBrocker);
            loginChecker.start();
        }
    }
}
