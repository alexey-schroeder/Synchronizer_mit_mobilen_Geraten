package com.client;

import com.topicAgent.TopicToSocketBrocker;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 03.02.14
 * Time: 21:47
 * To change this template use File | Settings | File Templates.
 */
public abstract class MobileClient extends Thread {
    public static String notValidMessageTemplate = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<message>" +
            "<error>NotValidXMLException</error>" +
            "</message>";
    protected boolean quit;
    protected String id;
    protected TopicToSocketBrocker topicToSocketBrocker;
    public String getMobileClientId() {
        return id;
    }

    public void setMobileClientId(String id) {
        this.id = id;
    }

    public void setTopicToSocketBrocker(TopicToSocketBrocker topicToSocketBrocker) {
        this.topicToSocketBrocker = topicToSocketBrocker;
    }
    public abstract void writeMessage(String message);
}
