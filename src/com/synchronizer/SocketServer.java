package com.synchronizer;

import com.loginChecker.LoginChecker;
import com.loginChecker.SocketLoginChecker;
import com.topicAgent.TopicToSocketBrocker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 02.02.14
 * Time: 23:06
 * To change this template use File | Settings | File Templates.
 */
public class SocketServer extends Thread {
   private int socketPort;
    private TopicToSocketBrocker topicToSocketBrocker;
    public SocketServer(int socketPort) {
        this.socketPort = socketPort;
    }

    @Override
    public void run() {
        System.out.println("Starte ServerSocket on socketPort " + socketPort);
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(socketPort);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        System.out.println("SocketServer startet on socketPort " + socketPort);
        while(true){
            Socket client = null;
            try {
                client = serverSocket.accept();
                System.out.println("New Connection");
                LoginChecker loginChecker = new SocketLoginChecker(client, topicToSocketBrocker);
                loginChecker.start();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public int getSocketPort() {
        return socketPort;
    }

    public void setSocketPort(int socketPort) {
        this.socketPort = socketPort;
    }

    public TopicToSocketBrocker getTopicToSocketBrocker() {
        return topicToSocketBrocker;
    }

    public void setTopicToSocketBrocker(TopicToSocketBrocker topicToSocketBrocker) {
        this.topicToSocketBrocker = topicToSocketBrocker;
    }
}
