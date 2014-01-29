package com.loginChecker;

import com.client.MobileClientWithSocket;
import com.messages.Message;
import com.parsers.XMLParser;
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
 * Date: 19.01.14
 * Time: 22:06
 * To change this template use File | Settings | File Templates.
 */
public class LoginChecker extends Thread {
    private Socket socket;
    private TopicToSocketBrocker topicAgent;
    private String loginOkAnswerTemplate =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<message>" +
            "       <result>success</result>" +
            "   </message>";
    private String loginFailedAnswerTemplate =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<message>" +
            "       <result>failed</result>" +
            "   </message>";
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
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
               if(!topicAgent.hasClientWithId(id)){
                   out.println(loginOkAnswerTemplate);
                   out.flush();
                   MobileClientWithSocket mobileClientWithSocket = new MobileClientWithSocket(socket);
                   mobileClientWithSocket.setMobileClientId(id);
                   topicAgent.addClient(mobileClientWithSocket);
               } else {
                   out.println(loginFailedAnswerTemplate);
                   out.flush();
                   out.close();
               }
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (JMSException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private String getIdFromLoginInfo(String loginInfo) {
        Message message= XMLParser.getMessageFromXML(loginInfo);
        return  message.get("login");
    }

    private boolean checkLogin(String loginInfo){
        return true;
    }
}
