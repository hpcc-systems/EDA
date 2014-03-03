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
	private String DatasetNameOriginal = "";
	private java.util.List people = new ArrayList();
	private String Test = "";
	private String FilePath;
	private String height = "";
	private String width = "";
	private String size = "";
	
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

	public String getSize(){
		return size;
	}
    
	public void setSize(String size){
		this.size = size;
	}

	public String getHeight(){
		return height;
	}
    
	public void setHeight(String height){
		this.height = height;
	}
	
	public String getWidth(){
		return width;
	}
    
	public void setWidth(String width){
		this.width = width;
	}
	
	public String getTest(){
		return Test;
	}
    
	public void setTest(String Test){
		this.Test = Test;
	}

	public String getFilePath(){
		return FilePath;
	}
    
	public void setFilePath(String FilePath){
		this.FilePath = FilePath;
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

	public String getDatasetNameOriginal(){
		return DatasetNameOriginal;
	}
    
	public void setDatasetNameOriginal(String DatasetNameOriginal){
		this.DatasetNameOriginal = DatasetNameOriginal;
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
        		String[] path = null;
        		String[] flag = FilePath.split("-");
        		for(int i = 0; i<flag.length; i++){
        			if(flag[i].startsWith("manifest")){
        				path = flag[i].split("\"");
        			} 
        		}
        		//String[] path = FilePath.split("\"");
        		logBasic(path[1]);//.replaceAll("manifest.xml", "")); 
        		Player axis = new Player();
        		String haxis = "hAxis: {title: '";
        		String Colours = "colors : ["; String fields = ""; String graph = "";String h = ""; String w = "";
        		if(getHeight().equals(""))
        			h = "400";
        		else
        			h = getHeight();
        		if(getWidth().equals(""))
        			w = "600";
        		else w = getWidth();
        		String size = "height: "+h+"px; width:"+w+"px;";//height: 400px; width: 600px;
	        	
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
	        			if(this.getDatasetName().equals(""))	        				
	        				fields += this.getDatasetNameOriginal()+"."+S[0]+";\n";
	        			else
	        				fields += this.getDatasetName()+"."+S[0]+";\n";
	        		}
	        	}
	        	else{
	        		for(Iterator it = people.iterator(); it.hasNext();){
		        		Player P = (Player) it.next();
		        		if(P.getColor() == 1)
		        			axis = P;        				
		        	}
		        	people.remove(axis);
		        	people.add(0,axis);
		        	
		        	boolean f = true;int i = 0;
		        	for(Iterator it = people.iterator(); it.hasNext();){
		        		if(f){
		        			Player p = (Player) it.next();
		        			if(this.getDatasetName().equals(""))
		        				fields += this.getDatasetNameOriginal()+"."+p.getFirstName()+";\n";
		        			else
		        				fields += this.getDatasetName()+"."+p.getFirstName()+";\n";
		        			i++;
		        			f = false;
		        			haxis += p.getFirstName().toUpperCase()+"', titleTextStyle:{color: 'Red', fontName:'Arial', fontSize:18, italic:0}},";
		        			continue;
		        		}
		        		Player P = (Player) it.next();
		        		if(this.getDatasetName().equals(""))
	        				fields += this.getDatasetNameOriginal()+"."+P.getFirstName()+";\n";
	        			else
	        				fields += this.getDatasetName()+"."+P.getFirstName()+";\n";
		        		if(i != people.size() - 1)
		        			Colours += "\""+ColorOption.INSTANCES[P.getColor()]+"\",";
		        		else
		        			Colours += "\""+ColorOption.INSTANCES[P.getColor()]+"\"]";
		        		i++;
		        	}	
		        	
			        try {
			        	logBasic(haxis+Colours + getTyp());        	
						Change(haxis+Colours, size, this.getTyp(), path[1].replaceAll("manifest.xml", ""));
						logBasic("File Changed?");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        	
	        	}
        	
	        	graph += "MyRec:=RECORD\n"+fields+"END;\n";
	        	if(this.getDatasetName().equals(""))
	        		graph += "MyTable:=TABLE("+getDatasetNameOriginal()+",MyRec);\n";
	        	else
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
    		out +=  p.getFirstName()+","+p.getColor().toString()+","+p.getTy();
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
        		P.setColor(Integer.parseInt(S[1]));
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
                setName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "name")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "Test")) != null)
                setTest(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "Test")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "height")) != null)
                setHeight(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "height")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "width")) != null)
                setWidth(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "width")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "Size")) != null)
                setSize(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "Size")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "FilePath")) != null)
                setFilePath(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "FilePath")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")) != null)
                setDatasetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name_original")) != null)
                setDatasetNameOriginal(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name_original")));

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
        retval += "		<Test><![CDATA[" + Test + "]]></Test>" + Const.CR;
        retval += "		<height><![CDATA[" + height + "]]></height>" + Const.CR;
        retval += "		<width><![CDATA[" + width + "]]></width>" + Const.CR;
        retval += "		<Size><![CDATA[" + size + "]]></Size>" + Const.CR;
        retval += "		<FilePath><![CDATA[" + FilePath + "]]></FilePath>" + Const.CR;
        retval += "		<normList><![CDATA[" + this.getnormList() + "]]></normList>" + Const.CR;
        retval += "		<dataset_name><![CDATA[" + DatasetName + "]]></dataset_name>" + Const.CR;
        retval += "		<dataset_name_original><![CDATA[" + DatasetNameOriginal + "]]></dataset_name_original>" + Const.CR;
        
        return retval;

    }

    public void loadRep(Repository rep, ObjectId id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
            throws KettleException {
        try {
        	if(rep.getStepAttributeString(id_jobentry, "Name") != null)
                Name = rep.getStepAttributeString(id_jobentry, "Name"); //$NON-NLS-1$
        	
        	if(rep.getStepAttributeString(id_jobentry, "Test") != null)
        		Test = rep.getStepAttributeString(id_jobentry, "Test"); //$NON-NLS-1$
        	
        	if(rep.getStepAttributeString(id_jobentry, "height") != null)
        		height = rep.getStepAttributeString(id_jobentry, "height"); //$NON-NLS-1$
        	
        	if(rep.getStepAttributeString(id_jobentry, "width") != null)
        		width = rep.getStepAttributeString(id_jobentry, "width"); //$NON-NLS-1$
        	
        	if(rep.getStepAttributeString(id_jobentry, "Size") != null)
        		size = rep.getStepAttributeString(id_jobentry, "Size"); //$NON-NLS-1$

        	if(rep.getStepAttributeString(id_jobentry, "FilePath") != null)
        		FilePath = rep.getStepAttributeString(id_jobentry, "FilePath"); //$NON-NLS-1$

        	if(rep.getStepAttributeString(id_jobentry, "datasetName") != null)
                DatasetName = rep.getStepAttributeString(id_jobentry, "datasetName"); //$NON-NLS-1$
        	
        	if(rep.getStepAttributeString(id_jobentry, "datasetNameOriginal") != null)
                DatasetNameOriginal = rep.getStepAttributeString(id_jobentry, "datasetNameOriginal"); //$NON-NLS-1$

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
        	
        	rep.saveStepAttribute(id_job, getObjectId(), "datasetNameOriginal", DatasetNameOriginal); //$NON-NLS-1$
        	
        	rep.saveStepAttribute(id_job, getObjectId(), "Typ", Typ); //$NON-NLS-1$
        	
        	rep.saveStepAttribute(id_job, getObjectId(), "Test", Test); //$NON-NLS-1$
        	
        	rep.saveStepAttribute(id_job, getObjectId(), "width", width); //$NON-NLS-1$
        	
        	rep.saveStepAttribute(id_job, getObjectId(), "height", height); //$NON-NLS-1$
        	
        	rep.saveStepAttribute(id_job, getObjectId(), "Size", size); //$NON-NLS-1$
        	
        	rep.saveStepAttribute(id_job, getObjectId(), "FilePath", FilePath); //$NON-NLS-1$
        	
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
    
    public void Change(String Colours, String size, String Chart, String path) throws Exception,FileNotFoundException, TransformerException {
        File xmlFile = new File(path+Chart.toLowerCase()+".xslt");//"D:\\Users\\Public\\Documents\\HPCC Systems\\ECL\\MY\\visualizations\\google_charts\\files\\"

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);
        NodeList nList = document.getElementsByTagName("xsl:template");
        Node n = nList.item(0);
        if (n.getNodeType() == Node.ELEMENT_NODE) {
        	Element eElement1 = (Element) n;
        	
    		System.out.println(eElement1.getElementsByTagName("div").item(0).getAttributes().getNamedItem("style").getNodeValue()); 
    		eElement1.getElementsByTagName("div").item(0).getAttributes().getNamedItem("style").setNodeValue(size);
        }
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
