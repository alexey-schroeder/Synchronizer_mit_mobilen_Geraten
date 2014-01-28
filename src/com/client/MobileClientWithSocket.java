package com.client;

import com.topicAgent.TopicToSocketBrocker;

import javax.jms.JMSException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 19.01.14
 * Time: 21:51
 * To change this template use File | Settings | File Templates.
 */
public class MobileClientWithSocket extends Thread{
    Socket socket;
    private BufferedReader data_in;
    private PrintWriter data_out;
    private String EOF = "\n";
    private boolean quit;
    private String id;
    private boolean breaked;
    private TopicToSocketBrocker topicToSocketBrocker;

    public MobileClientWithSocket(Socket socket) {
        this.socket = socket;
        try {
        data_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        data_out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String readMessage() {
        String message;
        try {
            message = data_in.readLine();
            if (message != null) {
                return message.trim();
            } else {
                quit = true;
                return message;
            }
        } catch (IOException ex) {
            quit = true;
            Logger.getLogger(MobileClientWithSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void writeMessage(String message) {
        data_out.println(message + EOF);
        data_out.flush();
    }

    public String getMobileClientId() {
        return id;
    }

    public void setMobileClientId(String id) {
        this.id = id;
    }

    public boolean  isOnline(){
       return !data_out.checkError();
    }

    @Override
    public void run() {
        while(!breaked){
            String message = readMessage();
            if(!breaked){
                try {
                    topicToSocketBrocker.writeMessageInTopic(message, id);
                } catch (JMSException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
    }

    public void setTopicToSocketBrocker(TopicToSocketBrocker topicToSocketBrocker) {
        this.topicToSocketBrocker = topicToSocketBrocker;
    }
}
