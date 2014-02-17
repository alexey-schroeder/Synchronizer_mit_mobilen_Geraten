package com.topicAgent;

import com.logger.Logger;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 19.01.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */
public class Producer {
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String topicName = "Werkstatt1";
    private MessageProducer producer;
    private Session session;

    public Producer(String url, String topicName) {
        this.url = url;
        this.topicName = topicName;
    }

    public Producer() {
        this(url, topicName);
    }

    public void start() throws JMSException {
        Logger.log("Starte producer for ActiveMQ");
        ConnectionFactory connectionFactory =  new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        System.out.println("Connection to ActiveMQ created");
        connection.start();
        System.out.println("Connection to ActiveMQ startet");
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        System.out.println("Session for ActiveMQ startet");
        Destination destination = session.createTopic(topicName);
        producer = session.createProducer(destination);
        Logger.log("MessageProducer for ActiveMQ created");
    }

    public void send(String text) throws JMSException {
        TextMessage message = session.createTextMessage(text);
        producer.send(message, DeliveryMode.PERSISTENT, 9, -1);  // wichtig: -1 steht für die zeit von lebendauer der nachricht!
    }
}
