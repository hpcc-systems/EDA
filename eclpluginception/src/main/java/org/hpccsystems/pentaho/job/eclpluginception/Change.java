package org.hpccsystems.pentaho.job.eclpluginception;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Change {
    public void change_plugin(Text PluginName, String Path) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException{
    	File file = new File(Path+"\\job\\ecl"+PluginName.getText().toLowerCase()+"\\src\\main\\resources\\plugin\\plugin.xml");//D:\\Users\\703119704\\Documents\\spoon-plugins\\spoon-plugins
    	System.out.println(file.exists());
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        
        NodeList nList = document.getElementsByTagName("plugin");
        System.out.println(nList.getLength());
        
        Node n = nList.item(0);
        if (n.getNodeType() == Node.ELEMENT_NODE) {
        	Element eElement1 = (Element) n;
        	
    		//System.out.println(eElement1.getElementsByTagName("name").item(0).getTextContent()); 
        	eElement1.setAttribute("id", "ECL"+PluginName.getText());
        	eElement1.setAttribute("iconfile","myplugin.png");
        	eElement1.setAttribute("description",PluginName.getText());
        	eElement1.setAttribute("tooltip",PluginName.getText());
        	eElement1.setAttribute("category","HPCC EDA");
        	eElement1.setAttribute("classname","org.hpccsystems.pentaho.job.ecl"+PluginName.getText().toLowerCase()+".ECL"+PluginName.getText());
        	eElement1.getElementsByTagName("library").item(2).getAttributes().getNamedItem("name").setNodeValue("ecl"+PluginName.getText().toLowerCase()+".jar");
        					
        }
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        

        
        StreamResult result = new StreamResult(new PrintWriter(new FileOutputStream(file, false)));
        DOMSource source = new DOMSource(document);
        transformer.transform(source, result);
    }
    
    
    public void write_pom(Text PluginName,String Path)throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException{
    	//D:\\Users\\703119704\\Documents\\spoon-plugins\\spoon-plugins
    	PrintWriter writer = new PrintWriter(Path+"\\job\\ecl"+PluginName.getText().toLowerCase()+"\\pom.xml", "UTF-8");
    	String S = "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n"+
    			   "	<modelVersion>4.0.0</modelVersion>\n"+
    			   "	<groupId>org.hpccsystems.pentaho.job</groupId>\n"+
    			   "	<artifactId>ecl"+PluginName.getText().toLowerCase()+"</artifactId>\n"+
    			   "	<packaging>jar</packaging>\n"+
    			   "	<name>HPCC Systems Pentaho Data Integration ECL "+PluginName.getText()+" Plugin</name>\n"+
    			   "    <description></description>\n"+
    			   "	<parent>\n"+	
    			   "		<groupId>org.hpccsystems.pentaho</groupId>\n"+
    			   "		<artifactId>job</artifactId>\n"+
    			   "		<version>0.1.0</version>\n"+
    			   "	</parent>\n"+

    			   "   	<properties>\n"+
    			   "		<iconfile>myplugin.png</iconfile>\n"+
    			   "	</properties>\n"+
    
	  			   "	<build>\n"+
	  			   "		<pluginManagement>\n"+
	  			   "			<plugins>\n"+
	  			   "				<plugin>\n"+
	  			   "					<groupId>org.apache.maven.plugins</groupId>\n"+
	  			   "					<artifactId>maven-assembly-plugin</artifactId>\n"+
	  			   "					<version>${maven.assembly.plugin.version}</version>\n"+
	  			   "				</plugin>\n"+
	  			   "			</plugins>\n"+
	  			   "		</pluginManagement>\n"+
		
		    	   "		<plugins>\n"+
		    	   "			<plugin>\n"+
		    	   "				<groupId>org.apache.maven.plugins</groupId>\n"+
		    	   "				<artifactId>maven-assembly-plugin</artifactId>\n"+
		    	   "				<executions>\n"+
		    	   "					<execution>\n"+
		    	   "						<id>make-assembly</id>\n"+
		    	   "						<phase>package</phase>\n"+
		    	   "						<goals>\n"+
		    	   "							<goal>single</goal>\n"+
		    	   "						</goals>\n"+
		    	   "					</execution>\n"+
		    	   "				</executions>\n"+
		    	   "				<configuration>\n"+
		    	   "					<descriptors>\n"+
		    	   "						<descriptor>../assembly.xml</descriptor>\n"+
		    	   "					</descriptors>\n"+
		    	   "				</configuration>\n"+
		    	   "			</plugin>\n"+
		    	   "		</plugins>\n"+
		    	   "	</build>\n"+
		    	   "</project>";
    	writer.println(S);
    	//writer.println("The second line");
    	writer.close();

    }
    
    public void change_project(Text PluginName,String Path) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException{
    	//D:\\Users\\703119704\\Documents\\spoon-plugins\\spoon-plugins
    	File file = new File(Path+"\\job\\ecl"+PluginName.getText().toLowerCase()+"\\.project");
    	System.out.println(file.exists());
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        
        NodeList nList = document.getElementsByTagName("projectDescription");
        System.out.println(nList.getLength());
        
        Node n = nList.item(0);
        if (n.getNodeType() == Node.ELEMENT_NODE) {
        	Element eElement1 = (Element) n;
        	
    		System.out.println(eElement1.getElementsByTagName("name").item(0).getTextContent()); 
    		eElement1.getElementsByTagName("name").item(0).setTextContent("ecl"+PluginName.getText().toLowerCase());
        }
        
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        

        
        StreamResult result = new StreamResult(new PrintWriter(new FileOutputStream(file, false)));
        DOMSource source = new DOMSource(document);
        transformer.transform(source, result);
        
    }


}
