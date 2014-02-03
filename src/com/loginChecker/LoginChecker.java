package com.loginChecker;

import com.messages.Message;
import com.parsers.XMLParser;
import com.topicAgent.TopicToSocketBrocker;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 19.01.14
 * Time: 22:06
 * To change this template use File | Settings | File Templates.
 */
public abstract class LoginChecker extends Thread {

    protected TopicToSocketBrocker topicToSocketBrocker;
    protected String loginOkAnswerTemplate =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<message>" +
            "       <result>success</result>" +
            "   </message>";
    protected String loginFailedAnswerTemplate =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<message>" +
            "       <result>failed</result>" +
            "   </message>";

    public LoginChecker() {
    }

    public LoginChecker(TopicToSocketBrocker topicToSocketBrocker) {
        this.topicToSocketBrocker = topicToSocketBrocker;
    }

    protected String getIdFromLoginInfo(String loginInfo) {
        Message message= XMLParser.getMessageFromXML(loginInfo);
        return  message.get("login");
    }

    protected boolean checkLogin(String loginInfo){
        return true;
    }
}
