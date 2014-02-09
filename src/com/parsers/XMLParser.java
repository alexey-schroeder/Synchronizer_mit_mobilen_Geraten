/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parsers;

import com.logger.Logger;
import com.messages.Message;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;


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
            Logger.log(ex.getStackTrace().toString());
        }
        return null;
    }

    public  static boolean isValid(String xmlAsString){
        Logger.log("Message wird geprüft:" + xmlAsString);
        if(xmlAsString.isEmpty()){
            return false;
        }
        DocumentBuilder newDocumentBuilder;
        try {
            newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            return false;
        }
        Document xmlDocument = null;
        try {
            xmlDocument = newDocumentBuilder.parse(new ByteArrayInputStream(xmlAsString.getBytes()));
        } catch (SAXException e) {
            Logger.log("XML is not valid: " + xmlAsString);
            return false;
        } catch (Exception e) {
            Logger.log("Unbekanntes Fehler (Vermutlich ein nicht UTF8-Zeichen in XML)");
            return false;
        }
        Element root =  xmlDocument.getDocumentElement();
        return true;
    }

    public static String replaceUmlaut(String input) {

        //replace all lower Umlauts
        String o_strResult =
                input
                        .replaceAll("ü", "ue")
                        .replaceAll("ö", "oe")
                        .replaceAll("ä", "ae")
                        .replaceAll("ß", "ss");

        //first replace all capital umlaute in a non-capitalized context (e.g. Übung)
        o_strResult =
                o_strResult
                        .replaceAll("Ü(?=[a-zäöüß ])", "Ue")
                        .replaceAll("Ö(?=[a-zäöüß ])", "Oe")
                        .replaceAll("Ä(?=[a-zäöüß ])", "Ae");

        //now replace all the other capital umlaute
        o_strResult =
                o_strResult
                        .replaceAll("Ü", "Ue")
                        .replaceAll("Ö", "Oe")
                        .replaceAll("Ä", "Ae");

        return o_strResult;
    }
}
