package org.hpccsystems.mapper;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.spi.DirStateFactory.Result;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author ChambeJX
 *
 */
public class BuildHelpIndex {
	
	private String url = "";
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public static void main(String[] args){
		BuildHelpIndex b = new BuildHelpIndex();
		b.setUrl("C:/Documents and Settings/ChambeJX.RISK/My Documents/spoon-plugins/spoon-plugins/eclgui/src/main/resources/org/hpccsystems/mapper/helpfiles/index.xml");
		b.setUrl("file:/C:/Program Files/data-integration/plugins/hpcc-common/helpfiles/index.xml");
		b.getMap();
	}
	public HashMap<String,String> getMap() 
    {
        HashMap<String,String>hMap=new HashMap<String, String>();
        /*URL url = null;
        String path = "plugins/hpcc-common/helpfiles";
	 	try{
		 	URL baseURL = this.getClass().getResource("../../../");
		 	
		 	System.out.println("BaseURL: " + baseURL.getPath());
		 	if(baseURL != null){
		 		url = new URL(baseURL, path);
		 	}else {
		 		url = this.getClass().getResource(path);
		 	}
	 	}catch (MalformedURLException e){
	 		System.out.println("can't find helf files" + e.toString());
	 	}
	 	System.out.println(url);*/
        
        File file=new File(url);

        if(file.exists())
        {
           DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
            try
            {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document=builder.parse(file);
                Element documentElement=document.getDocumentElement();
                NodeList entryList=documentElement.getElementsByTagName("entry");
                
                if (entryList != null && entryList.getLength() > 0)
                {
                	System.out.println("here" + entryList.getLength());
                    for (int i = 0; i < entryList.getLength(); i++)
                    {
                    	//key
                        //System.out.println(entryList.item(i).getAttributes().getNamedItem("keyword").getTextContent());
                    	String key = entryList.item(i).getAttributes().getNamedItem("keyword").getTextContent();
                        //value
                        //System.out.println("Value: " + entryList.item(i).getChildNodes().getLength());
                        
                        NodeList topics = entryList.item(i).getChildNodes();
                        for(int k =0; k < topics.getLength(); k++){
                        	//System.out.println("topic");
                        	if(topics.item(k).getNodeName().equals("topic")){
                        		//System.out.println("T: " + topics.item(k).getAttributes().getNamedItem("href").getTextContent());
                        		
                        		hMap.put(key, topics.item(k).getAttributes().getNamedItem("href").getTextContent());
                        	}
                        }
                        
                        //System.out.println(sList.item(i).getFirstChild().getAttributes().getNamedItem("href").getTextContent());
                    }
                }
            } catch(Exception e){
                System.out.println("exception occured" + e.toString());
            }
        }else
        {
            System.out.println("File not exists");
        }

        	return hMap;
    }
}
