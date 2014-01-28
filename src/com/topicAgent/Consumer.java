package com.topicAgent;

import com.parsers.XMLParser;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Consumer  extends Thread{
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

    public void run()  {
        ConnectionFactory connectionFactory
                = new ActiveMQConnectionFactory(url);
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            connection.setClientID(id);
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Topic destination = session.createTopic(subject);

            MessageConsumer consumer = session.createDurableSubscriber(destination, id);

            while (!breaked) {
                Message jmsMessage = consumer.receive();
                if (!breaked && jmsMessage instanceof TextMessage) { // weil in receive hängt der client, bracked kann kommen, aber der client bekommt es nicht mit
                    TextMessage textMessage = (TextMessage) jmsMessage;
                    com.messages.Message message = XMLParser.getMessageFromXML(textMessage.getText());
                    if (id.equals(message.get("id"))) {
                        topicToSocketBrocker.writeMessageInSocket(id, textMessage.getText());
                    }
                }
            }
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public String getClientId() {
        return id;
    }

    public void breakJob() {
        breaked = true;
    }

    public void setTopicToSocketBrocker(TopicToSocketBrocker topicToSocketBrocker) {
        this.topicToSocketBrocker = topicToSocketBrocker;
    }
}