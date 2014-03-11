package org.hpccsystems.pentaho.job.eclgraph;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Test {
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException{
		File xmlFile = new File("D:\\Users\\Public\\Documents\\HPCC Systems\\ECL\\MY\\visualizations\\google_charts\\files\\scatterchart.xslt");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);
        NodeList nList = document.getElementsByTagName("xsl:template");
        Node n = nList.item(0);
        if (n.getNodeType() == Node.ELEMENT_NODE) {    
        	Element eElement1 = (Element) n;
        	
    		System.out.println(eElement1.getElementsByTagName("div").item(0).getAttributes().getNamedItem("style").getNodeValue()); 
    		//eElement1.getElementsByTagName("div").item(0).getAttributes().getNamedItem("style").setNodeValue(size);
        }
    	Node nNode = nList.item(1);         	
    	if (nNode.getNodeType() == Node.ELEMENT_NODE) {    			
    		Element eElement = (Element) nNode;
    		
    		/*String S = "			var zoomed = false;\n			var MAX = 20;\n			var options = {\n"+
				"						animation: {duration: 1000,easing: 'in'},\n"+
				"						tooltip: { textStyle: { fontName: 'Arial', fontSize: 18, bold:false }},"+
				"						hAxis: {viewWindow : {min : 0, max:4},title: 'NAME', titleTextStyle:{color: 'Red', fontName:'Arial', fontSize:18, italic:0}},"+
				"						colors : [\"Yellow\",\"Red\"]"+						
				"			};"+
				"			function draw";	
    	    eElement.getElementsByTagName("xsl:text").item(1).setTextContent(S);
    		*/
    		System.out.println(eElement.getElementsByTagName("xsl:text").item(1).getTextContent());
    	}
	}
}

/*for(int i = 0; i<eElement.getElementsByTagName("xsl:text").getLength();i++){
    			//
    			String S = ",0.6);var "+Chart.toLowerCase()+" = new google.visualization."+Chart+"(document.getElementById('";//var options = {"+Colours+"};
    			System.out.println(eElement.getElementsByTagName("xsl:text").item(3).getTextContent());
    			eElement.getElementsByTagName("xsl:text").item(3).setTextContent(S);
    		}*/
