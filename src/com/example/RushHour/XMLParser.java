package com.example.RushHour;

import com.example.RushHour.GameObjects.Puzzle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: GSUS
 * Date: 27.10.2013
 * Time: 21:31
 * To change this template use File | Settings | File Templates.
 */
public class XMLParser {

    public ArrayList<Puzzle> parsePuzzleFile(){
        ArrayList<Puzzle> puzzles = new ArrayList<Puzzle>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(getClass().getResource("/assets/levels.xml").toString());
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("puzzle");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode  ;
                    int id =  Integer.valueOf(eElement.getAttribute("id"));
                    int level = Integer.valueOf(eElement.getElementsByTagName("level").item(0).getTextContent());
                    int length = Integer.valueOf(eElement.getElementsByTagName("length").item(0).getTextContent());
                    String setup = eElement.getElementsByTagName("setup").item(0).getTextContent();

                    puzzles.add(new Puzzle(id, level, setup));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return puzzles;
    }
}
