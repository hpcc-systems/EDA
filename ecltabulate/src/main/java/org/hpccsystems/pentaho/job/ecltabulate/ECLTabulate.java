/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.ecltabulate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	private ArrayList<Player> people = new ArrayList<Player>();
	private ArrayList<Player> rows = new ArrayList<Player>();
	private ArrayList<Player> cols = new ArrayList<Player>();
	private ArrayList<Player> layers = new ArrayList<Player>();
	private ArrayList<String> settings = new ArrayList<String>();
	
	private String label ="";
	private String outputName ="";
	private String persist = "";
	private String defJobName = "";
	public static int ct = 0;
	
	
	public ArrayList<String> getSettings() {
		return settings;
	}

	public void setSettings(ArrayList<String> settings) {
		this.settings = settings;
	}

	public String getDefJobName() {
		return defJobName;
	}

	public void setDefJobName(String defJobName) {
		this.defJobName = defJobName;
	}
	
	public String getPersistOutputChecked() {
		return persist;
	}

	public void setPersistOutputChecked(String persist) {
		this.persist = persist;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getOutputName() {
		return outputName;
	}

	public void setOutputName(String outputName) {
		this.outputName = outputName;
	}
	
	public void setPeople(ArrayList<Player> people){
		this.people = people;
	}
	
	public ArrayList<Player> getPeople(){
		return people;
	}
	
	public void setRows(ArrayList<Player> rows){
		this.rows = rows;
	}
	
	public ArrayList<Player> getRows(){
		return rows;
	}
	
	public void setCols(ArrayList<Player> cols){
		this.cols = cols;
	}
	
	public ArrayList<Player> getCols(){
		return cols;
	}
	
	public void setLayers(ArrayList<Player> layers){
		this.layers = layers;
	}
	
	public ArrayList<Player> getLayers(){
		return layers;
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
        	String ecl = "";int cnt = 0;String layer = "";String group = "";String join ="";        	
        	for(Iterator<Player> itL = layers.iterator(); itL.hasNext();){
        		Player sl = (Player) itL.next();
        		String Sl = sl.getFirstName();
        		layer += this.DatasetName+"."+Sl+";\n";
        		if(layers.indexOf(sl) == layers.size()-1 ){
        			join += "LEFT."+Sl+" = RIGHT."+Sl;
        			group += Sl;        		
        		}
        		else{
        			join += "LEFT."+Sl+" = RIGHT."+Sl+" AND ";
        			group += Sl+",";        			
        		}
        	}
        	for(Iterator<Player> itR = rows.iterator(); itR.hasNext();){
        		String transform = "";
        		Player sr =  (Player) itR.next();
        		String Sr = sr.getFirstName();
        		int percent = 0;
        		if(layer!="")
        			ecl += "MyRec"+cnt+":=RECORD\nUNSIGNED id := 1;\n"+layer+this.getDatasetName()+"."+Sr+";\n";
        		else
        			ecl += "MyRec"+cnt+":=RECORD\nUNSIGNED id := 1;\n"+this.getDatasetName()+"."+Sr+";\n";
        		for(Iterator<Player> itC = cols.iterator(); itC.hasNext();){
        			Player sc = (Player) itC.next();
        			String Sc = sc.getFirstName();
        			int op = sc.getOP();
        			if(!settings.isEmpty())
        				percent = 1;
        			
        			switch(op){
        			case 0: 
        				ecl += "cnt_"+Sc+" := COUNT(GROUP);\n";
        				if(percent > 0)
        					ecl += "DECIMAL5_2 "+Sc+"_Percent := 0;\n";
        				break;
        			case 1:
        				ecl += "add_"+Sc+" := SUM(GROUP,(REAL)"+this.getDatasetName()+"."+Sc+");\n";
        				if(percent > 0)
        					ecl += "DECIMAL5_2 "+Sc+"_Percent := 0;\n";
        				break;
        			case 2:
        				ecl += "Mean_"+Sc+" := AVE(GROUP,(REAL)"+this.getDatasetName()+"."+Sc+");\n";
        				if(percent > 0)
        					ecl += "DECIMAL5_2 "+Sc+"_Percent := 0;\n";
        				break;
        			case 3:
        				ecl += "Var_"+Sc+" := VARIANCE(GROUP,(REAL)"+this.getDatasetName()+"."+Sc+");\n";
        				if(percent > 0)
        					ecl += "DECIMAL5_2 "+Sc+"_Percent := 0;\n";
        				break;
        			case 4:
        				ecl += "Sd_"+Sc+" := SQRT(VARIANCE(GROUP,(REAL)"+this.getDatasetName()+"."+Sc+"));\n";
        				if(percent > 0)
        					ecl += "DECIMAL5_2 "+Sc+"_Percent := 0;\n";
        				break;
        			case 5:
        				ecl += "maximum_"+Sc+":= MAX(GROUP,(REAL)"+this.getDatasetName()+"."+Sc+");\n";
        				if(percent > 0)
        					ecl += "DECIMAL5_2 "+Sc+"_Percent := 0;\n";
        				break;
        			case 6:
        				ecl += "minimum_"+Sc+" := MIN(GROUP,(REAL)"+this.getDatasetName()+"."+Sc+");\n";
        				if(percent > 0)
        					ecl += "DECIMAL5_2 "+Sc+"_Percent := 0;\n";
        				break;
        			}        			
        		}
        		ecl += "END;\n";
    			if(group != "")
    				ecl += "MyTab"+cnt+":=TABLE("+this.getDatasetName()+",MyRec"+cnt+","+group+","+Sr+");\n";
    			else
    				ecl += "MyTab"+cnt+":=TABLE("+this.getDatasetName()+",MyRec"+cnt+","+Sr+");\n";
    			
        		if(settings.contains("parent")){
        			transform = "";
        			if(layer!="")
        				ecl += "MyNewRecParent"+cnt+" := RECORD\n"+layer;
        			else
        				ecl += "MyNewRecParent"+cnt+" := RECORD\n"+this.getDatasetName()+"."+Sr+";\n";
        			for(Iterator<Player> itC = cols.iterator(); itC.hasNext();){
            			Player sc = (Player) itC.next();
            			String Sc = sc.getFirstName();
            			int op = sc.getOP();
            			switch(op){
            			case 0: 
            				ecl += "cnt_layers_"+Sc+" := COUNT(GROUP);\n";
            				transform += "SELF."+Sc+"_Percent := (100*(L.cnt_"+Sc+"/L.cnt_layers_"+Sc+"));\n";
            				break;
            			case 1:
            				ecl += "add_layers_"+Sc+" := SUM(GROUP,(REAL)"+this.getDatasetName()+"."+Sc+");\n";    
            				transform += "SELF."+Sc+"_Percent := (100*(L.add_"+Sc+"/L.add_layers_"+Sc+"));\n";
            				break;
            			case 2:
            				ecl += "Mean_layers_"+Sc+" := AVE(GROUP,(REAL)"+this.getDatasetName()+"."+Sc+");\n";   
            				transform += "SELF."+Sc+"_Percent := (100*(L.Mean_"+Sc+"/L.Mean_layers_"+Sc+"));\n";
            				break;
            			case 3:
            				ecl += "Var_layers_"+Sc+" := VARIANCE(GROUP,(REAL)"+this.getDatasetName()+"."+Sc+");\n";  
            				transform += "SELF."+Sc+"_Percent := (100*(L.Var_"+Sc+"/L.Var_layers_"+Sc+"));\n";
            				break;
            			case 4:
            				ecl += "Sd_layers_"+Sc+" := SQRT(VARIANCE(GROUP,(REAL)"+this.getDatasetName()+"."+Sc+"));\n";  
            				transform += "SELF."+Sc+"_Percent := (100*(L.Sd_"+Sc+"/L.Sd_layers_"+Sc+"));\n";
            				break;
            			case 5:
            				ecl += "maximum_layers_"+Sc+" := MAX(GROUP,(REAL)"+this.getDatasetName()+"."+Sc+");\n";   
            				transform += "SELF."+Sc+"_Percent := (100*(L.maximum_"+Sc+"/L.maximum_layers_"+Sc+"));\n";
            				break;
            			case 6:
            				ecl += "minimum_layers_"+Sc+" := MIN(GROUP,(REAL)"+this.getDatasetName()+"."+Sc+");\n";  
            				transform += "SELF."+Sc+"_Percent := (100*(L.minimum_"+Sc+"/L.minimum_layers_"+Sc+"));\n";         				
            				break;
            			}
        			}
        			ecl += "END;\n";
        			if(layer!=""){
        				ct++;
	        			ecl += "ParentTable"+cnt+" := TABLE("+this.getDatasetName()+",MyNewRecParent"+cnt+","+group+");\n";        			
	        			ecl += "withTotal"+cnt+" := JOIN(MyTab"+cnt+",ParentTable"+cnt+","+join+");\n";
	        			ecl += "MyRec"+cnt+" Trans"+cnt+"(withTotal"+cnt+" L, INTEGER C) := TRANSFORM\n";
	        			ecl += transform;
	        			ecl += "SELF := L;\nEND;\n";
	        			ecl += "PercentTable"+cnt+" := PROJECT(withTotal"+cnt+",trans"+cnt+"(LEFT,COUNTER));\n";
	        			//ecl += "OUTPUT(PercentTable"+cnt+",NAMED('WithParentPercentage"+cnt+"'));\n";
	        			if(persist.equalsIgnoreCase("true")){
        	        		if(outputName != null && !(outputName.trim().equals(""))){
        	        			ecl += "OUTPUT((PercentTable"+cnt+")"+",,'~eda::"+outputName+"::tabulate_parent', __compressed__, overwrite,NAMED('WithParentPercentage"+cnt+"'))"+";\n";
        	        		}else{
        	        			ecl += "OUTPUT((PercentTable"+cnt+")"+",,'~eda::"+defJobName+"::tabulate_parent', __compressed__, overwrite,NAMED('WithParentPercentage"+cnt+"'))"+";\n";
        	        		}
        	        	}
        	        	else{
        	        		ecl += "OUTPUT((PercentTable"+cnt+"),NAMED('WithParentPercentage"+cnt+"'));\n";
        	        	}
	        		}
        			else{
        				ct++;
        				ecl += "ParentTable"+cnt+" := TABLE("+this.getDatasetName()+",MyNewRecParent"+cnt+","+Sr+");\n";        			
	        			ecl += "withTotal"+cnt+" := JOIN(MyTab"+cnt+",ParentTable"+cnt+",LEFT."+Sr+"=RIGHT."+Sr+");\n";
	        			ecl += "MyRec"+cnt+" Trans"+cnt+"(withTotal"+cnt+" L, INTEGER C) := TRANSFORM\n";
	        			ecl += transform;
	        			ecl += "SELF := L;\nEND;\n";
	        			ecl += "PercentTable"+cnt+" := PROJECT(withTotal"+cnt+",trans"+cnt+"(LEFT,COUNTER));\n";
	        			//ecl += "OUTPUT(PercentTable"+cnt+",NAMED('WithParentPercentage"+cnt+"'));\n";
	        			if(persist.equalsIgnoreCase("true")){
        	        		if(outputName != null && !(outputName.trim().equals(""))){
        	        			ecl += "OUTPUT((PercentTable"+cnt+")"+",,'~eda::"+outputName+"::tabulate_parent', __compressed__, overwrite,NAMED('WithParentPercentage"+cnt+"'))"+";\n";
        	        		}else{
        	        			ecl += "OUTPUT((PercentTable"+cnt+")"+",,'~eda::"+defJobName+"::tabulate_parent', __compressed__, overwrite,NAMED('WithParentPercentage"+cnt+"'))"+";\n";
        	        		}
        	        	}
        	        	else{
        	        		ecl += "OUTPUT((PercentTable"+cnt+"),NAMED('WithParentPercentage"+cnt+"'));\n";
        	        	}
        			}
        			
        		}
        		if(settings.contains("total")){
        			transform = "";
        			ct++;
        			ecl += "MyNewRecGrand"+cnt+" := RECORD\nUNSIGNED id := 1;\n";
        			for(Iterator<Player> itC = cols.iterator(); itC.hasNext();){
            			Player sc = (Player) itC.next();
            			String Sc = sc.getFirstName();
            			int op = sc.getOP();
            			switch(op){
            			case 0: 
            				ecl += "cnt_layers_"+Sc+" := COUNT(GROUP);\n";
            				transform += "SELF."+Sc+"_Percent := (100*(L.cnt_"+Sc+"/L.cnt_layers_"+Sc+"));\n";
            				break;
            			case 1:
            				ecl += "add_layers_"+Sc+" := SUM(GROUP,(REAL)"+this.getDatasetName()+"."+Sc+");\n";    
            				transform += "SELF."+Sc+"_Percent := (100*(L.add_"+Sc+"/L.add_layers_"+Sc+"));\n";
            				break;
            			case 2:
            				ecl += "Mean_layers_"+Sc+" := AVE(GROUP,(REAL)"+this.getDatasetName()+"."+Sc+");\n";   
            				transform += "SELF."+Sc+"_Percent := (100*(L.Mean_"+Sc+"/L.Mean_layers_"+Sc+"));\n";
            				break;
            			case 3:
            				ecl += "Var_layers_"+Sc+" := VARIANCE(GROUP,(REAL)"+this.getDatasetName()+"."+Sc+");\n";  
            				transform += "SELF."+Sc+"_Percent := (100*(L.Var_"+Sc+"/L.Var_layers_"+Sc+"));\n";
            				break;
            			case 4:
            				ecl += "Sd_layers_"+Sc+" := SQRT(VARIANCE(GROUP,(REAL)"+this.getDatasetName()+"."+Sc+"));\n";  
            				transform += "SELF."+Sc+"_Percent := (100*(L.Sd_"+Sc+"/L.Sd_layers_"+Sc+"));\n";
            				break;
            			case 5:
            				ecl += "maximum_layers_"+Sc+" := MAX(GROUP,(REAL)"+this.getDatasetName()+"."+Sc+");\n";   
            				transform += "SELF."+Sc+"_Percent := (100*(L.maximum_"+Sc+"/L.maximum_layers_"+Sc+"));\n";
            				break;
            			case 6:
            				ecl += "minimum_layers_"+Sc+" := MIN(GROUP,(REAL)"+this.getDatasetName()+"."+Sc+");\n";  
            				transform += "SELF."+Sc+"_Percent := (100*(L.minimum_"+Sc+"/L.minimum_layers_"+Sc+"));\n";         				
            				break;
            			}
        			}
        			ecl += "END;\n";
        			ecl += "GrandTable"+cnt+" := TABLE("+this.getDatasetName()+",MyNewRecGrand"+cnt+");\n";        			
        			ecl += "withGrandTotal"+cnt+" := JOIN(MyTab"+cnt+",GrandTable"+cnt+",LEFT.id=RIGHT.id);\n";
        			ecl += "MyRec"+cnt+" TransTotal"+cnt+"(withGrandTotal"+cnt+" L, INTEGER C) := TRANSFORM\n";
        			ecl += transform;
        			ecl += "SELF := L;\nEND;\n";
        			ecl += "PercentGrandTable"+cnt+" := PROJECT(withGrandTotal"+cnt+",transTotal"+cnt+"(LEFT,COUNTER));\n";
        			//ecl += "OUTPUT(PercentGrandTable"+cnt+",NAMED('WithGrandTotalPercentage"+cnt+"'));\n";
        			if(persist.equalsIgnoreCase("true")){
    	        		if(outputName != null && !(outputName.trim().equals(""))){
    	        			ecl += "OUTPUT((PercentGrandTable"+cnt+")"+",,'~eda::"+outputName+"::tabulate_grand', __compressed__, overwrite,NAMED('WithGrandTotalPercentage"+cnt+"'))"+";\n";
    	        		}else{
    	        			ecl += "OUTPUT((PercentGrandTable"+cnt+")"+",,'~eda::"+defJobName+"::tabulate_grand', __compressed__, overwrite,NAMED('WithGrandTotalPercentage"+cnt+"'))"+";\n";
    	        		}
    	        	}
    	        	else{
    	        		ecl += "OUTPUT((PercentGrandTable"+cnt+"),NAMED('WithGrandTotalPercentage"+cnt+"'));\n";
    	        	}
        		}        		       		
        		if(!settings.contains("parent") && !settings.contains("total")){
        			ct++;
        			//ecl += "OUTPUT(MyTab"+cnt+",NAMED('"+Sr+"_crossTab'));\n";
        			if(persist.equalsIgnoreCase("true")){
    	        		if(outputName != null && !(outputName.trim().equals(""))){
    	        			ecl += "OUTPUT((MyTab"+cnt+")"+",,'~eda::"+outputName+"::tabulate', __compressed__, overwrite,NAMED('"+Sr+"_crossTab'))"+";\n";
    	        		}else{
    	        			ecl += "OUTPUT((MyTab"+cnt+")"+",,'~eda::"+defJobName+"::tabulate', __compressed__, overwrite,NAMED('"+Sr+"_crossTab'))"+";\n";
    	        		}
    	        	}
    	        	else{
    	        		ecl += "OUTPUT((MyTab"+cnt+"),NAMED('"+Sr+"_crossTab'));\n";
    	        	}
        		}
        		cnt = cnt + 1;
        		
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
    	
    	Iterator<Player> it = people.iterator();
    	boolean isFirst = true;
    	while(it.hasNext()){
    		if(!isFirst){out+="|";}
    		Player p = (Player) it.next();
    		out +=  p.getFirstName()+","+p.getOP();
            isFirst = false;
    	}
    	return out;
    }

    public void openPeople(String in){
        String[] strLine = in.split("[|]");
        int len = strLine.length;
        if(len>0){
        	people = new ArrayList<Player>();
        	for(int i = 0; i<len; i++){
        		String[] S = strLine[i].split(",");
        		Player P = new Player();
        		P.setFirstName(S[0]);
        		
        		P.setOP(0);
        		people.add(P);
        	}
        }
    }
    public String saveRows(){
    	String out = "";
    	
    	Iterator<Player> it = rows.iterator();
    	boolean isFirst = true;
    	while(it.hasNext()){
    		if(!isFirst){out+="|";}
    		Player p = (Player) it.next();
    		out +=  p.getFirstName()+","+p.getOP();
            isFirst = false;
    	}
    	return out;
    }

    public void openRows(String in){
        String[] strLine = in.split("[|]");
        int len = strLine.length;
        if(len>0){
        	rows = new ArrayList<Player>();
        	for(int i = 0; i<len; i++){
        		String[] S = strLine[i].split(",");
        		Player P = new Player();
        		P.setFirstName(S[0]);
        		P.setOP(0); 
        		
        		rows.add(P);
        	}
        }
    }
    public String saveCols(){
    	String out = "";
    	
    	Iterator<Player> it = cols.iterator();
    	boolean isFirst = true;
    	while(it.hasNext()){
    		if(!isFirst){out+="|";}
    		Player p = (Player) it.next();
    		out +=  p.getFirstName()+","+p.getOP();
            isFirst = false;
    	}
    	return out;
    }

    public void openCols(String in){
        String[] strLine = in.split("[|]");
        int len = strLine.length;
        if(len>0){
        	cols = new ArrayList<Player>();
        	for(int i = 0; i<len; i++){
        		String[] S = strLine[i].split(",");
        		Player P = new Player();
        		P.setFirstName(S[0]);
        		P.setOP(Integer.parseInt(S[1]));
        		
        		cols.add(P);
        	}
        }  
    }
    public String saveLayers(){
    	String out = "";
    	
    	Iterator<Player> it = layers.iterator();
    	boolean isFirst = true;
    	while(it.hasNext()){
    		if(!isFirst){out+="|";}
    		Player p = (Player) it.next();
    		out +=  p.getFirstName()+","+p.getOP();
            isFirst = false;
    	}
    	return out;
    }

    public void openLayers(String in){
        String[] strLine = in.split("[|]");
        int len = strLine.length;
        if(len>0){
        	layers = new ArrayList<Player>();
        	for(int i = 0; i<len; i++){
        		String[] S = strLine[i].split(",");
        		Player P = new Player();
        		P.setFirstName(S[0]);
        		P.setOP(0);
        		
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
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "output_name")) != null)
                setOutputName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "output_name")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "label")) != null)
                setLabel(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "label")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "persist_Output_Checked")) != null)
                setPersistOutputChecked(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "persist_Output_Checked")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "defJobName")) != null)
                setDefJobName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "defJobName")));
            
        } catch (Exception e) {
            throw new KettleXMLException("ECL Dataset Job Plugin Unable to read step info from XML node", e);
        }

    }

    public String getXML() {
        String retval = "";
        
        retval += super.getXML();
        retval += "		<dataset_name ><![CDATA[" + DatasetName + "]]></dataset_name>" + Const.CR;
        retval += "		<settings><![CDATA[" + this.saveSettings() + "]]></settings>" + Const.CR;
        retval += "		<people><![CDATA[" + this.savePeople() + "]]></people>" + Const.CR;
        retval += "		<rows><![CDATA[" + this.saveRows() + "]]></rows>" + Const.CR;
        retval += "		<cols><![CDATA[" + this.saveCols() + "]]></cols>" + Const.CR;
        retval += "		<layers><![CDATA[" + this.saveLayers() + "]]></layers>" + Const.CR;
        retval += "		<label><![CDATA[" + label + "]]></label>" + Const.CR;
        retval += "		<output_name><![CDATA[" + outputName + "]]></output_name>" + Const.CR;
        retval += "		<persist_Output_Checked><![CDATA[" + persist + "]]></persist_Output_Checked>" + Const.CR;
        retval += "		<defJobName><![CDATA[" + defJobName + "]]></defJobName>" + Const.CR;
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
            if(rep.getStepAttributeString(id_jobentry, "outputName") != null)
            	outputName = rep.getStepAttributeString(id_jobentry, "outputName"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "label") != null)
            	label = rep.getStepAttributeString(id_jobentry, "label"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "persist_Output_Checked") != null)
            	persist = rep.getStepAttributeString(id_jobentry, "persist_Output_Checked"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "defJobName") != null)
            	defJobName = rep.getStepAttributeString(id_jobentry, "defJobName"); //$NON-NLS-1$
            
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
            rep.saveStepAttribute(id_job, getObjectId(), "outputName", outputName);
        	rep.saveStepAttribute(id_job, getObjectId(), "label", label);
        	rep.saveStepAttribute(id_job, getObjectId(), "persist_Output_Checked", persist);
        	rep.saveStepAttribute(id_job, getObjectId(), "defJobName", defJobName);
            
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
