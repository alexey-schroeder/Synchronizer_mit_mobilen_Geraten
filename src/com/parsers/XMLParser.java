/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parsers;

import com.messages.Message;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alex
 */
public class XMLParser {
    static public Message getMessageFromXML(String xmlAsString)
    {
        DocumentBuilder newDocumentBuilder;
        try {
            newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xmlDocument = newDocumentBuilder.parse(new ByteArrayInputStream(xmlAsString.getBytes()));
            Element root =  xmlDocument.getDocumentElement();
            NodeList childs = root.getChildNodes();
            Message message = new Message();
            for(int i = 0; i< childs.getLength(); i++){
                Node child = childs.item(i);
                String tagName = child.getNodeName();
                String tagValue = child.getTextContent();
                message.set(tagName, tagValue);
            }
            return message;
        } catch (Exception ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
