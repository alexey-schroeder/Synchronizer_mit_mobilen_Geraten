package com.topicAgent;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Consumer {
    // URL of the JMS server
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String id = "Consumer_1";
    // Name of the queue we will receive messages from
    private static String subject = "Werkstatt1";
    private boolean breaked;
    private TopicToSocketBrocker topicToSocketBrocker;

    public Consumer(String id) {
        this.id = id;
    }

    public void start() throws JMSException {
        ConnectionFactory connectionFactory
                = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.setClientID(id);
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic destination = session.createTopic(subject);

        MessageConsumer consumer = session.createDurableSubscriber(destination, id);

        while (!breaked) {
            Message message = consumer.receive();
            if (!breaked) { // weil in receive hängt der client, bracked kann kommen, aber der client bekommt es nicht mit
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    topicToSocketBrocker.writeMessageToSocket(id, textMessage.getText());
                }
            }
        }
        connection.close();
    }

    public String getId() {
        return id;
    }

    public void stop() {
        breaked = true;
    }

    public void setTopicToSocketBrocker(TopicToSocketBrocker topicToSocketBrocker) {
        this.topicToSocketBrocker = topicToSocketBrocker;
    }
}