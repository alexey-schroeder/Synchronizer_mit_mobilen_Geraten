package com.topicAgent;

import com.client.MobileClientWithSocket;
import com.security.SecurityInspector;

import javax.jms.JMSException;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 26.01.14
 * Time: 12:23
 * To change this template use File | Settings | File Templates.
 */
public class TopicToSocketBrocker {
    private Producer producer;
    private HashMap<String, MobileClientWithSocket> mobileClients;
    private HashMap<String, Consumer> consumers;
    private SecurityInspector securityInspector;

    public TopicToSocketBrocker() throws JMSException {
        producer = new Producer();
        producer.start();
        mobileClients = new HashMap<String, MobileClientWithSocket>();
        consumers = new HashMap<String, Consumer>();
    }

    public boolean hasClientWithId(String id) {
        MobileClientWithSocket mobileClientWithSocket = mobileClients.get(id);
      return  mobileClientWithSocket != null;
    }

    public void removeMobileClient(String clientId) {
        Consumer consumer = consumers.get(clientId);
        consumer.breakJob();
        consumers.remove(clientId);
        mobileClients.remove(clientId);
        System.out.println("Client " + clientId + " disconected");
    }

    public void addClient(MobileClientWithSocket clientWithSocket) throws JMSException {
        String id = clientWithSocket.getMobileClientId();
        Consumer consumer = new Consumer(id);
        consumer.setTopicToSocketBrocker(this);
        consumers.put(id, consumer);
        mobileClients.put(id, clientWithSocket);
        clientWithSocket.setTopicToSocketBrocker(this);
        clientWithSocket.start();
        consumer.start();
        System.out.println("New client added. Client id = " + id);
    }

    public void setSecurityInspector(SecurityInspector securityInspector) {
        this.securityInspector = securityInspector;
    }

    public void writeMessageInTopic(String message, String clientId) throws JMSException {
        if (securityInspector.canClientWriteInTopic(clientId)) {
            producer.send(message);
            System.out.println("topicToSocket brocker hat in Topic folgendes Message gepostet: \n" + message);
        }
    }

    public void writeMessageInSocket(String clientId, String message) {
        if (securityInspector.canClientReadFromTopic(clientId)) {
            mobileClients.get(clientId).writeMessage(message);
        }
    }
}
