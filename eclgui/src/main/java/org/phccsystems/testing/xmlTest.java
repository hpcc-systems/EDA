package org.phccsystems.testing;

import au.com.bytecode.opencsv.CSVReader;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class xmlTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		xmlTest test = new xmlTest();
		String fileName = "c:\\Spoon Demos\\new\\saltdemos\\datahygiene_out\\rsults.xml";
		fileName = "C:\\Spoon Demos\\new\\saltdemos\\datahygiene_out\\results.xml";
		test.openResultsXML(fileName);
	}
	
	public void openResultsXML(String fileName){
	
	      String outStr;
	      try{
	    	  
	    	  DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			  DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			  Document doc = (Document) dBuilder.parse(fileName);
			  doc.getDocumentElement().normalize();
				
			  System.out.println("Root element : " + doc.getDocumentElement().getNodeName());
				
			  String wuid =  doc.getElementsByTagName("wuid").item(0).getTextContent();
	          System.out.println(wuid);
	          NodeList results = doc.getElementsByTagName("result");
	          
	          for (int temp = 0; temp < results.getLength(); temp++) {
	        	  Node result = results.item(temp);
	        	  NamedNodeMap att = result.getAttributes();
	        	  //type
	        	  System.out.println(att.getNamedItem("resulttype").getTextContent());
	        	  //filename
	        	  System.out.println(result.getTextContent());
	        	  
	        	  //so here we use type and decide what tab to open and pass filename
	          }
	          
	    }catch (Exception e){//Catch exception if any
	        System.err.println("Error: " + e.getMessage());
	        e.printStackTrace();
	     }
	  
	      
	      
	  }

}
