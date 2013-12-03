/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.eclgraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.hpccsystems.javaecl.Table;
import org.pentaho.di.cluster.SlaveServer;
import org.pentaho.di.compatibility.Value;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Result;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.hpccsystems.ecljobentrybase.*;

/**
 *
 * @author KeshavS
 */
public class ECLGraph extends ECLJobEntry{//extends JobEntryBase implements Cloneable, JobEntryInterface {
	
	private String Name = "";
	private String Typ = "";
	private String DatasetName = "";
	private String normList = "";
	private java.util.List people = new ArrayList();
	
	public void setPeople(java.util.List people){
		this.people = people;
	}
	
	public java.util.List getPeople(){
		return people;
	}

	public String getName(){
		return Name;
	}
    
	public void setName(String Name){
		this.Name = Name;
	}
	
	public String getTyp(){
		return Typ;
	}
    
	public void setTyp(String Typ){
		this.Typ = Typ;
	}
	
	
	public String getDatasetName(){
		return DatasetName;
	}
    
	public void setDatasetName(String DatasetName){
		this.DatasetName = DatasetName;
	}
	
	public String getnormList(){
		return normList;
	}
    
	public void setnormList(String normList){
		this.normList = normList;
	}
    
    @Override
    public Result execute(Result prevResult, int k) throws KettleException {
    	
    	
        Result result = prevResult;
        if(result.isStopped()){
        	return result;
        }
        else{
        		Player axis = new Player();
        		String Colours = "colors : ["; String fields = ""; String graph = "";
        	
	        	logBasic(normList);
	        	if(this.getTyp().equals("PieChart")){
	        		String[] norm = normList.split("-");
	        		for(int i = 0; i<norm.length; i++){
	        			String[] S = norm[i].split(",");
	        			if(S[2].equals("string")){
	        				String temp = S[0]+","+S[1]+","+S[2];
	        				S = norm[0].split(",");
	        				norm[i] = S[0]+","+S[1]+","+S[2];
	        				norm[0] = temp;
	        				break;
	        			}
	        		}
	        		for(int i = 0; i<norm.length; i++){
	        			String[] S = norm[i].split(",");
	        			fields += this.getDatasetName()+"."+S[0]+";\n";
	        		}
	        	}
	        	else{
	        		for(Iterator it = people.iterator(); it.hasNext();){
		        		Player P = (Player) it.next();
		        		if(P.getColour() == 1)
		        			axis = P;        				
		        	}
		        	people.remove(axis);
		        	people.add(0,axis);
		        	
		        	boolean f = true;int i = 0;
		        	for(Iterator it = people.iterator(); it.hasNext();){
		        		if(f){
		        			Player p = (Player) it.next();
		        			fields += this.getDatasetName()+"."+p.getFirstName()+";\n";
		        			i++;
		        			f = false;
		        			continue;
		        		}
		        		Player P = (Player) it.next();
		        		fields += this.getDatasetName()+"."+P.getFirstName()+";\n";
		        		if(i != people.size() - 1)
		        			Colours += "\""+ColourOption.INSTANCES[P.getColour()]+"\",";
		        		else
		        			Colours += "\""+ColourOption.INSTANCES[P.getColour()]+"\"]";
		        		i++;
		        	}	
		        	
			        try {
			        	logBasic(Colours + getTyp());        	
						Change(Colours, this.getTyp());
						logBasic("File Changed?");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        	
	        	}
        	
	        	graph += "MyRec:=RECORD\n"+fields+"END;\n";
	        	graph += "MyTable:=TABLE("+getDatasetName()+",MyRec);\n";
	        	graph += "OUTPUT(MyTable,named(\'"+this.getTyp()+"_MyChart\'));\n";
	        	
		        logBasic("graph Job =" + graph);//{Dataset Job} 
		        
		        result.setResult(true);
		        
		        RowMetaAndData data = new RowMetaAndData();
		        data.addValue("ecl", Value.VALUE_TYPE_STRING, graph);
		        
		        
		        List list = result.getRows();
		        list.add(data);
		        String eclCode = parseEclFromRowData(list);
		        result.setRows(list);
		        result.setLogText("ECLFrequency executed, ECL code added");
        }
        return result;
    }
    
    public String savePeople(){
    	String out = "";
    	
    	Iterator it = people.iterator();
    	boolean isFirst = true;
    	while(it.hasNext()){
    		if(!isFirst){out+="|";}
    		Player p = (Player) it.next();
    		out +=  p.getFirstName()+","+p.getColour().toString()+","+p.getTy();
            isFirst = false;
    	}
    	return out;
    }

    public void openPeople(String in){
        String[] strLine = in.split("[|]");
        int len = strLine.length;
        if(len>0){
        	people = new ArrayList();
        	for(int i = 0; i<len; i++){
        		String[] S = strLine[i].split(",");
        		Player P = new Player();
        		P.setFirstName(S[0]);
        		P.setColour(Integer.parseInt(S[1]));
        		P.setTy(S[2]);
        		people.add(P);
        	}
        }
    }

    
    @Override
    public void loadXML(Node node, List<DatabaseMeta> list, List<SlaveServer> list1, Repository rpstr) throws KettleXMLException {
        try {
            super.loadXML(node, list, list1);
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "name")) != null)
                setDatasetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "name")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")) != null)
                setDatasetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")));

            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "Typ")) != null)
                setTyp(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "Typ")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "normList")) != null)
                setnormList(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "normList")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "people")) != null)
                openPeople(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "people")));
            
            
        } catch (Exception e) {
            throw new KettleXMLException("ECL Dataset Job Plugin Unable to read step info from XML node", e);
        }

    }

    public String getXML() {
        String retval = "";
        
        retval += super.getXML();
        retval += "		<name><![CDATA[" + Name + "]]></name>" + Const.CR;
        retval += "		<people><![CDATA[" + this.savePeople() + "]]></people>" + Const.CR;
        retval += "		<Typ><![CDATA[" + Typ + "]]></Typ>" + Const.CR;
        retval += "		<normList><![CDATA[" + this.getnormList() + "]]></normList>" + Const.CR;
        retval += "		<dataset_name><![CDATA[" + DatasetName + "]]></dataset_name>" + Const.CR;
        
        return retval;

    }

    public void loadRep(Repository rep, ObjectId id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
            throws KettleException {
        try {
        	if(rep.getStepAttributeString(id_jobentry, "Name") != null)
                Name = rep.getStepAttributeString(id_jobentry, "Name"); //$NON-NLS-1$
        	
        	if(rep.getStepAttributeString(id_jobentry, "datasetName") != null)
                DatasetName = rep.getStepAttributeString(id_jobentry, "datasetName"); //$NON-NLS-1$

            if(rep.getStepAttributeString(id_jobentry, "Typ") != null)
            	Typ = rep.getStepAttributeString(id_jobentry, "Typ"); //$NON-NLS-1$
            
            if(rep.getStepAttributeString(id_jobentry, "normList") != null)
                this.setnormList(rep.getStepAttributeString(id_jobentry, "normList")); //$NON-NLS-1$
            
            if(rep.getStepAttributeString(id_jobentry, "people") != null)
                this.openPeople(rep.getStepAttributeString(id_jobentry, "people")); //$NON-NLS-1$
            
            
        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }

    public void saveRep(Repository rep, ObjectId id_job) throws KettleException {
        try {
        	rep.saveStepAttribute(id_job, getObjectId(), "datasetName", DatasetName); //$NON-NLS-1$
        	
        	rep.saveStepAttribute(id_job, getObjectId(), "Typ", Typ); //$NON-NLS-1$
        	
        	rep.saveStepAttribute(id_job, getObjectId(), "Name", Name); //$NON-NLS-1$
        	
        	rep.saveStepAttribute(id_job, getObjectId(), "normList", this.getnormList()); //$NON-NLS-1$
        	
            rep.saveStepAttribute(id_job, getObjectId(), "people", this.savePeople()); //$NON-NLS-1$
            
        } catch (Exception e) {
            throw new KettleException("Unable to save info into repository" + id_job, e);
        }
    }

    public boolean evaluates() {
        return true;
    }

    public boolean isUnconditional() {
        return true;
    }
    
    public void Change(String Colours, String Chart) throws Exception,FileNotFoundException, TransformerException {
        File xmlFile = new File("D:\\Users\\Public\\Documents\\HPCC Systems\\ECL\\MY\\visualizations\\google_charts\\files\\"+Chart.toLowerCase()+".xslt");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);
        NodeList nList = document.getElementsByTagName("xsl:template");

    	Node nNode = nList.item(1);         	
    	if (nNode.getNodeType() == Node.ELEMENT_NODE) {    			
    		Element eElement = (Element) nNode;
    		for(int i = 0; i<eElement.getElementsByTagName("xsl:text").getLength();i++){
    			//
    			String S = ",0.6);var options = {"+Colours+"};var "+Chart.toLowerCase()+" = new google.visualization."+Chart+"(document.getElementById('";
    			System.out.println(eElement.getElementsByTagName("xsl:text").item(3).getTextContent());
    			eElement.getElementsByTagName("xsl:text").item(3).setTextContent(S);
    		}
    			
    	}

    	Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        

        
        StreamResult result = new StreamResult(new PrintWriter(new FileOutputStream(xmlFile, false)));
        DOMSource source = new DOMSource(document);
        transformer.transform(source, result);
    }

}
