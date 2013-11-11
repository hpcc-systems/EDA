/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.ecltabulate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hpccsystems.javaecl.Rollup;
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
import org.w3c.dom.Node;
import org.hpccsystems.ecljobentrybase.*;

/**
 *
 * @author KeshavS
 */
public class ECLTabulate extends ECLJobEntry{//extends JobEntryBase implements Cloneable, JobEntryInterface {
	


	private String DatasetName = "";
	private java.util.List people = new ArrayList();
	private java.util.List rows = new ArrayList();
	private java.util.List cols = new ArrayList();
	private java.util.List layers = new ArrayList();
	private ArrayList<String> settings = new ArrayList<String>();
	
	public void setPeople(java.util.List people){
		this.people = people;
	}
	
	public java.util.List getPeople(){
		return people;
	}
	
	public void setRows(java.util.List rows){
		this.rows = rows;
	}
	
	public java.util.List getRows(){
		return rows;
	}
	
	public void setCols(java.util.List cols){
		this.cols = cols;
	}
	
	public java.util.List getCols(){
		return cols;
	}
	
	public void setLayers(java.util.List layers){
		this.layers = layers;
	}
	
	public java.util.List getLayers(){
		return layers;
	}
	
	public void setSettings(ArrayList<String> settings){
		this.settings = settings;
	}
	
	public ArrayList<String> getSettings(){
		return settings;
	}

	public String getDatasetName(){
		return DatasetName;
	}
    
	public void setDatasetName(String DatasetName){
		this.DatasetName = DatasetName;
	}
	
    @Override
    public Result execute(Result prevResult, int k) throws KettleException {
    	
    	
        Result result = prevResult;
        if(result.isStopped()){
        	return result;
        }
        else{
        	String ecl = "";int cnt = 0;String layer = "    ";String rowlayer = " ";String sort = " ";
        	for(Iterator itL = layers.iterator(); itL.hasNext();){
        		Player sl = (Player) itL.next();
        		String Sl = sl.getFirstName();
        		layer += this.DatasetName+"."+Sl+";\n";
        		if(layers.indexOf(sl) == layers.size()-1 ){
        			rowlayer += "LEFT."+Sl+"=RIGHT."+Sl;
        			sort += Sl;
        		}
        		else{
        			rowlayer += "LEFT."+Sl+"=RIGHT."+Sl+" AND ";
        			sort += Sl+",";
        			
        		}
        	}
        	for(Iterator itR = rows.iterator(); itR.hasNext();){
        		Player sr =  (Player) itR.next();
        		String Sr = sr.getFirstName();
        		for(Iterator itC = cols.iterator(); itC.hasNext();){
        			Player sc = (Player) itC.next();
        			String Sc = sc.getFirstName();
        			ecl += "MyRec"+cnt+":=RECORD\n"+layer+this.getDatasetName()+"."+Sr+";\n"+this.getDatasetName()+"."+Sc+";\n";
        			ecl += "cnt:=COUNT(GROUP);\n";
        			ecl += "END;\n";
        			if(sort.equals(" "))
        				ecl += "MyTab"+cnt+":=TABLE("+this.getDatasetName()+",MyRec"+cnt+","+Sr+","+Sc+");\n";
        			else
        				ecl += "MyTab"+cnt+":=TABLE("+this.getDatasetName()+",MyRec"+cnt+","+Sr+","+Sc+","+sort+");\n";
        			if(settings.contains("count"))
        				ecl += "OUTPUT(CHOOSEN(MyTab"+cnt+",200),NAMED('CountTable"+cnt+"'));\n";
        			if(settings.contains("rows") || settings.contains("cols")){        				
        				String s = "";
        				String r = "";
        				
        				if(settings.contains("rows")) 
        					{s = Sr;r = "Row";}
        				else if(settings.contains("cols"))
        					{s = Sc;r = "Col";}
        				if(sort.equals(" "))
        					ecl += "sortedTab"+cnt+":=SORT(MyTab"+cnt+","+s+");\n";
        				else
        					ecl += "sortedTab"+cnt+":=SORT(MyTab"+cnt+","+s+","+sort+");\n";
        				Rollup roll = new Rollup();
        				String S = "Rolled"+Integer.toString(cnt);
        				roll.setName(S);
        				roll.setRecordset("sortedTab"+cnt);
        				roll.setRecordFormat("sortedTab"+Integer.toString(cnt));
        				roll.setTransform("SELF.cnt:=L.cnt+R.cnt;\nSELF:=L;\n");
        				roll.setTransformName("RollThem"+Integer.toString(cnt));
        				if(rowlayer.equals(" "))
        					roll.setCondition("LEFT."+s+"=RIGHT."+s);
        				else
        					roll.setCondition("LEFT."+s+"=RIGHT."+s+" AND "+rowlayer);
        				ecl += roll.ecl();
        				ecl += "MyORec"+cnt+":=RECORD\n"+S+"."+s+";\n";
        				for(Iterator itL = layers.iterator(); itL.hasNext();){
        					Player sl = (Player) itL.next();
        	        		String Sl = sl.getFirstName();
        	        		ecl += S+"."+Sl+";\n";
        				}
        				ecl += "totcnt:="+S+".cnt;\nEND;\n";
        				ecl += "Tab1"+cnt+":=TABLE("+S+",MyORec"+cnt+");\n";
        				if(rowlayer.equals(" "))
        					ecl += "Tab2"+cnt+":=JOIN(MyTab"+cnt+",Tab1"+cnt+",LEFT."+s+"=RIGHT."+s+");\n";
        				else
        					ecl += "Tab2"+cnt+":=JOIN(MyTab"+cnt+",Tab1"+cnt+",LEFT."+s+"=RIGHT."+s+" AND "+rowlayer+");\n";
        				ecl += r+"Rec"+cnt+":=RECORD\nTab2"+cnt+";\nREAL "+r+"Pct;\nEND;\n";
        				ecl += r+"Rec"+cnt+" trans"+cnt+"(Tab2"+cnt+" L):=TRANSFORM\n";
        				ecl += "SELF."+r+"Pct:=IF((L.cnt/L.totcnt)>= 1, 100, (L.cnt/L.totcnt)*100);\nSELF:=L;\nEND;\n";
        				ecl += r+"Table"+cnt+":=PROJECT(Tab2"+cnt+",trans"+cnt+"(LEFT));\n";
        				ecl += "OUTPUT(CHOOSEN("+r+"Table"+cnt+",200),NAMED(\'"+r+"Percentages"+cnt+"\'));";
        			}
        			cnt = cnt + 1;
        		}
        		
        	}
        	result.setResult(true);
            
            RowMetaAndData data = new RowMetaAndData();
            data.addValue("ecl", Value.VALUE_TYPE_STRING, ecl);
            
            
            List list = result.getRows();
            list.add(data);
            String eclCode = parseEclFromRowData(list);
            result.setRows(list);
            result.setLogText("ECLTabulate executed, ECL code added");
        }
        
        return result;
    }
    
    public String saveSettings(){
    	String out = "";
    	
    	Iterator<String> it = settings.iterator();
    	boolean isFirst = true;
    	while(it.hasNext()){
    		if(!isFirst){out+="|";}
    		String p =  it.next();
    		out +=  p;
            isFirst = false;
    	}
    	return out;
    }
    public void openSettings(String in){
        String[] strLine = in.split("[|]");
        int len = strLine.length;
        if(len>0){
        	settings = new ArrayList<String>();
        	for(int i = 0; i<len; i++){
        		settings.add(strLine[i]);
        	}
        }
    }

    
    public String savePeople(){
    	String out = "";
    	
    	Iterator it = people.iterator();
    	boolean isFirst = true;
    	while(it.hasNext()){
    		if(!isFirst){out+="|";}
    		Player p = (Player) it.next();
    		out +=  p.getFirstName();
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
        		String S = strLine[i];
        		Player P = new Player();
        		P.setFirstName(S);
        		
        		people.add(P);
        	}
        }
    }
    public String saveRows(){
    	String out = "";
    	
    	Iterator it = rows.iterator();
    	boolean isFirst = true;
    	while(it.hasNext()){
    		if(!isFirst){out+="|";}
    		Player p = (Player) it.next();
    		out +=  p.getFirstName();
            isFirst = false;
    	}
    	return out;
    }

    public void openRows(String in){
        String[] strLine = in.split("[|]");
        int len = strLine.length;
        if(len>0){
        	rows = new ArrayList();
        	for(int i = 0; i<len; i++){
        		String S = strLine[i];
        		Player P = new Player();
        		P.setFirstName(S);
        		
        		rows.add(P);
        	}
        }
    }
    public String saveCols(){
    	String out = "";
    	
    	Iterator it = cols.iterator();
    	boolean isFirst = true;
    	while(it.hasNext()){
    		if(!isFirst){out+="|";}
    		Player p = (Player) it.next();
    		out +=  p.getFirstName();
            isFirst = false;
    	}
    	return out;
    }

    public void openCols(String in){
        String[] strLine = in.split("[|]");
        int len = strLine.length;
        if(len>0){
        	cols = new ArrayList();
        	for(int i = 0; i<len; i++){
        		String S = strLine[i];
        		Player P = new Player();
        		P.setFirstName(S);
        		
        		cols.add(P);
        	}
        }
    }
    public String saveLayers(){
    	String out = "";
    	
    	Iterator it = layers.iterator();
    	boolean isFirst = true;
    	while(it.hasNext()){
    		if(!isFirst){out+="|";}
    		Player p = (Player) it.next();
    		out +=  p.getFirstName();
            isFirst = false;
    	}
    	return out;
    }

    public void openLayers(String in){
        String[] strLine = in.split("[|]");
        int len = strLine.length;
        if(len>0){
        	layers = new ArrayList();
        	for(int i = 0; i<len; i++){
        		String S = strLine[i];
        		Player P = new Player();
        		P.setFirstName(S);
        		
        		layers.add(P);
        	}
        }
    }
    
    @Override
    public void loadXML(Node node, List<DatabaseMeta> list, List<SlaveServer> list1, Repository rpstr) throws KettleXMLException {
        try {
            super.loadXML(node, list, list1);
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")) != null)
                setDatasetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "settings")) != null)
                openSettings(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "settings")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "people")) != null)
                openPeople(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "people")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "rows")) != null)
                openRows(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "rows")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "cols")) != null)
                openCols(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "cols")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "layers")) != null)
                openLayers(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "layers")));
            
        } catch (Exception e) {
            throw new KettleXMLException("ECL Dataset Job Plugin Unable to read step info from XML node", e);
        }

    }

    public String getXML() {
        String retval = "";
        
        retval += super.getXML();
        retval += "		<dataset_name ><![CDATA[" + DatasetName + "]]></dataset_name>" + Const.CR;
        retval += "		<people><![CDATA[" + this.savePeople() + "]]></people>" + Const.CR;
        retval += "		<settings><![CDATA[" + this.saveSettings() + "]]></settings>" + Const.CR;
        retval += "		<rows><![CDATA[" + this.saveRows() + "]]></rows>" + Const.CR;
        retval += "		<cols><![CDATA[" + this.saveCols() + "]]></cols>" + Const.CR;
        retval += "		<layers><![CDATA[" + this.saveLayers() + "]]></layers>" + Const.CR;
        return retval;

    }

    public void loadRep(Repository rep, ObjectId id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
            throws KettleException {
        try {
        	
        	if(rep.getStepAttributeString(id_jobentry, "datasetName") != null)
                DatasetName = rep.getStepAttributeString(id_jobentry, "datasetName"); //$NON-NLS-1$
        	if(rep.getStepAttributeString(id_jobentry, "settings") != null)
                this.openSettings(rep.getStepAttributeString(id_jobentry, "settings")); //$NON-NLS-1$
            
            if(rep.getStepAttributeString(id_jobentry, "people") != null)
                this.openPeople(rep.getStepAttributeString(id_jobentry, "people")); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "rows") != null)
                this.openRows(rep.getStepAttributeString(id_jobentry, "rows")); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "cols") != null)
                this.openCols(rep.getStepAttributeString(id_jobentry, "cols")); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "layers") != null)
                this.openLayers(rep.getStepAttributeString(id_jobentry, "layers")); //$NON-NLS-1$

            
        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }

    public void saveRep(Repository rep, ObjectId id_job) throws KettleException {
        try {
        	rep.saveStepAttribute(id_job, getObjectId(), "datasetName", DatasetName); //$NON-NLS-1$
        	rep.saveStepAttribute(id_job, getObjectId(), "settings", this.saveSettings()); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "people", this.savePeople()); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "rows", this.saveRows()); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "cols", this.saveCols()); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "layers", this.saveLayers()); //$NON-NLS-1$
            
            
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
    
}
