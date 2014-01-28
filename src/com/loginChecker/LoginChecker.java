package com.loginChecker;

import com.client.MobileClientWithSocket;
import com.topicAgent.TopicToSocketBrocker;

import javax.jms.JMSException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 19.01.14
 * Time: 22:06
 * To change this template use File | Settings | File Templates.
 */
public class LoginChecker extends Thread {
    private Socket socket;
    private TopicToSocketBrocker topicAgent;
    public LoginChecker(Socket socket, TopicToSocketBrocker topicAgent) {
        this.socket = socket;
        this.topicAgent = topicAgent;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader =  new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String loginInfo = reader.readLine();
            if(checkLogin(loginInfo)){
                String id = getIdFromLoginInfo(loginInfo);
               if(!topicAgent.hasClientWithId(id)){
                   topicAgent.addClient(new MobileClientWithSocket(socket));
               }
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (JMSException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private String getIdFromLoginInfo(String loginInfo) {
        return "1";
    }

    private boolean checkLogin(String loginInfo){
        return true;
    }
}
