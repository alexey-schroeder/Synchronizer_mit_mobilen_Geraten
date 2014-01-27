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
        if (mobileClientWithSocket == null) {
            return false;
        }
        boolean isClientOnLine = isClientOnline(mobileClientWithSocket.getMobileClientId());
        if (isClientOnLine) {
            return true;
        } else { // client ist nicht mehr online, socket ist nicht  aktiv
            removeMobileClient(id);
            return false;
        }
    }

    private void removeMobileClient(String clientId) {
        Consumer consumer = consumers.get(clientId);
        consumer.stop();
        consumers.remove(clientId);

        mobileClients.remove(clientId);
    }

    private boolean isClientOnline(String clientId) {
        return mobileClients.get(clientId).isOnline();
    }

    public void addClient(MobileClientWithSocket clientWithSocket) throws JMSException {
        String id = clientWithSocket.getMobileClientId();
        Consumer consumer = new Consumer(clientWithSocket.getMobileClientId());
        consumer.setTopicToSocketBrocker(this);
        consumers.put(id, consumer);
        mobileClients.put(id, clientWithSocket);
        clientWithSocket.start();
        consumer.start();
    }

    public void setSecurityInspector(SecurityInspector securityInspector) {
        this.securityInspector = securityInspector;
    }

    public void writeMessageInTopic(String message, String clientId) throws JMSException {
        if (securityInspector.canClientWriteInTopic(clientId)) {
            producer.send(message);
        }
    }

    public void writeMessageToSocket(String clientId, String message) {
        if (securityInspector.canClientReadFromTopic(clientId)) {
            mobileClients.get(clientId).writeMessage(message);
        }
    }
}
