package com.loginChecker;

import com.client.MobileClientWithSocket;
import com.topicAgent.TopicToSocketBrocker;

import javax.jms.JMSException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 03.02.14
 * Time: 20:59
 * To change this template use File | Settings | File Templates.
 */
public class SocketLoginChecker extends LoginChecker {
    private Socket socket;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public SocketLoginChecker(Socket socket, TopicToSocketBrocker topicAgent) {
        this.socket = socket;
        this.topicToSocketBrocker = topicAgent;

    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String loginInfo = reader.readLine();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            if (checkLogin(loginInfo)) {
                String id = getIdFromLoginInfo(loginInfo);
                out.println(loginOkAnswerTemplate);
                out.flush();
                MobileClientWithSocket mobileClientWithSocket = new MobileClientWithSocket(socket);
                mobileClientWithSocket.setMobileClientId(id);
                topicToSocketBrocker.addClient(mobileClientWithSocket);
            } else {
                out.println(loginFailedAnswerTemplate);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (JMSException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
