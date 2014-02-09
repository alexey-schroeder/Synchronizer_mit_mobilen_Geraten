package com.topicAgent;

import com.logger.Logger;
import com.parsers.XMLParser;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Consumer  extends Thread{
    // URL of the JMS server
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private  String id;
    // Name of the queue we will receive messages from
    private static String subject = "Werkstatt1";
    private boolean quit;
    private TopicToSocketBrocker topicToSocketBrocker;
    private Session session;
    private Connection connection;
    private MessageConsumer consumer;
    public Consumer(String id) {
        this.id = id;
    }

    public void run()  {
        ConnectionFactory connectionFactory
                = new ActiveMQConnectionFactory(url);
        try {
            connection = connectionFactory.createConnection();
            connection.setClientID(id);
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Topic destination = session.createTopic(subject);

            consumer = session.createDurableSubscriber(destination, id);

            while (!quit) {
                Message jmsMessage = consumer.receive();
                if(jmsMessage != null){
                Logger.log("aus topic wurde von consumer mit id = " + id + " folgendes message gelesen" + ((TextMessage) jmsMessage).getText());
                }
                if (!quit && jmsMessage instanceof TextMessage) { // weil in receive hängt der client, bracked kann kommen, aber der client bekommt es nicht mit
                    TextMessage textMessage = (TextMessage) jmsMessage;
                    com.messages.Message message = XMLParser.getMessageFromXML(textMessage.getText());
                    if (id.equals(message.get("idTo"))) {
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
        quit = true;
        try {
            connection.stop();
            consumer.close();
            session.close();
//            connection.stop();
//            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void setTopicToSocketBrocker(TopicToSocketBrocker topicToSocketBrocker) {
        this.topicToSocketBrocker = topicToSocketBrocker;
    }
}