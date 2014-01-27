package com;

import com.security.SecurityInspector;
import com.synchronizer.Synchronizer;
import com.topicAgent.TopicToSocketBrocker;

public class Main {

    public static void main(String[] args) throws Exception {
        SecurityInspector securityInspector = new SecurityInspector();
        TopicToSocketBrocker topicToSocketBrocker = new TopicToSocketBrocker();
        topicToSocketBrocker.setSecurityInspector(securityInspector);
        Synchronizer synchronizer = new Synchronizer(1111);
        synchronizer.setTopicToSocketBrocker(topicToSocketBrocker);
        synchronizer.start();
    }
}
