package com.topicAgent;

import com.client.MobileClient;
import com.logger.Logger;
import com.security.SecurityInspector;

import javax.jms.JMSException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 26.01.14
 * Time: 12:23
 * To change this template use File | Settings | File Templates.
 */
public class TopicToSocketBrocker {
    private Producer producer;
    private HashMap<String, Set<MobileClient>> mobileClients;
    private HashMap<String, Consumer> consumers;
    private SecurityInspector securityInspector;
    private static TopicToSocketBrocker instance;

    public static synchronized TopicToSocketBrocker getInstance(){
        if (instance == null) {
            try {
                instance = new TopicToSocketBrocker();
            } catch (JMSException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return instance;
    }

    private TopicToSocketBrocker() throws JMSException {
        producer = new Producer();
        producer.start();
        mobileClients = new HashMap<String, Set<MobileClient>>();
        consumers = new HashMap<String, Consumer>();
    }

    public boolean hasClientWithId(String id) {
        Set<MobileClient> clients = mobileClients.get(id);
        return clients != null && !clients.isEmpty();
    }

    public void removeMobileClient(String clientId, MobileClient mobileClient) {
        Set<MobileClient> clients = mobileClients.get(clientId);
        if (clients != null) {
            clients.remove(mobileClient);
            if (clients.isEmpty()) {
                Consumer consumer = consumers.get(clientId);
                consumer.breakJob();
                consumers.remove(clientId);
                mobileClients.remove(clientId);
            }
        }
        Logger.log("Client " + clientId + " disconected");
    }

    public void addClient(MobileClient client) throws JMSException {
        String clientId = client.getMobileClientId();
        Set<MobileClient> clients = mobileClients.get(clientId);
        if (clients == null) {
            Consumer consumer = new Consumer(clientId);
            consumer.setTopicToSocketBrocker(this);
            consumers.put(clientId, consumer);
            consumer.start();
            clients = new HashSet<MobileClient>();
            mobileClients.put(clientId, clients);
        }
        clients.add(client);
        client.setTopicToSocketBrocker(this);
        client.start();

        Logger.log("New client added. Client id = " + clientId);
    }

    public void setSecurityInspector(SecurityInspector securityInspector) {
        this.securityInspector = securityInspector;
    }

    public void writeMessageInTopic(String message, String clientId) throws JMSException {
        if (securityInspector.canClientWriteInTopic(clientId)) {
            producer.send(message);
            Logger.log("topicToSocket brocker hat in Topic folgendes Message gepostet: \n" + message);
        }
    }

    public void writeMessageInSocket(String clientId, String message) {
        if (securityInspector.canClientReadFromTopic(clientId)) {
            Set<MobileClient> allClients = mobileClients.get(clientId);
            for (MobileClient mobileClient : allClients) {
                mobileClient.writeMessage(message);
            }
        }
    }
}
