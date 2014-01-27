package com.security;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 27.01.14
 * Time: 21:19
 * To change this template use File | Settings | File Templates.
 */
public class SecurityInspector {
    public boolean canClientWriteInTopic(String id){
        return  true;
    }

    public boolean canClientReadFromTopic(String id){
        return  true;
    }
}
