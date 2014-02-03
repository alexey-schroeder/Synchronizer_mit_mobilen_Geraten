package com;

import com.security.SecurityInspector;
import com.synchronizer.Synchronizer;
import com.topicAgent.TopicToSocketBrocker;

public class Main {

    public static void main(String[] args) throws Exception {
        SecurityInspector securityInspector = new SecurityInspector();
        TopicToSocketBrocker topicToSocketBrocker = TopicToSocketBrocker.getInstance();
        topicToSocketBrocker.setSecurityInspector(securityInspector);
        Synchronizer synchronizer = new Synchronizer(1111, 2222);
        synchronizer.setTopicToSocketBrocker(topicToSocketBrocker);
        synchronizer.start();
    }
}
