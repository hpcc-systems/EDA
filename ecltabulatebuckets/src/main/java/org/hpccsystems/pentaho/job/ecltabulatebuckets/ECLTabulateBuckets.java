/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.ecltabulatebuckets;

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
public class ECLTabulateBuckets extends ECLJobEntry{//extends JobEntryBase implements Cloneable, JobEntryInterface {
	


	private String DatasetName = "";
	private java.util.List people = new ArrayList();
	private java.util.List rows = new ArrayList();
	private java.util.List layers = new ArrayList();
	private String label ="";
	private String outputName ="";
	private String persist = "";
	private String defJobName = "";
	
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
	
	public void setLayers(java.util.List layers){
		this.layers = layers;
	}
	
	public java.util.List getLayers(){
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
        	String ecl = "";int cnt = 0;String layer = "    ";String smoking = " ";String group = "";String layerRec = "";
        	String field = "";String value = "";
        	String buck = "";int i = 0;
        	
        	for(Iterator it = rows.iterator(); it.hasNext();){
        		Player S = (Player) it.next();
        		if(i!=rows.size()-1){        			
        			field += "\'"+S.getFirstName()+"\',";
        			value += "LEFT."+S.getFirstName()+",";
        			if(S.getBuckets().equals("0"))
        				buck += "1,";
        			else
        				buck += S.getBuckets()+",";
        		}
        		else{
        			field += "\'"+S.getFirstName()+"\'";
        			value += "LEFT."+S.getFirstName();
        			if(S.getBuckets().equals("0"))
        				buck += "1";
        			else
        				buck += S.getBuckets();
        		}
        		i++;
        	}
        	i = 0;
        	for(Iterator it = layers.iterator(); it.hasNext();){
        		Player S = (Player) it.next();
        		if(i < layers.size()-1){
        			layer += "SELF.Layer"+(i+1)+":=CHOOSE(COUNTER,LEFT."+S.getFirstName()+");";
        			group += "Layer"+(i+1)+",";
        		}
        		else{
        			layer += "SELF.Layer"+(i+1)+":=CHOOSE(COUNTER,LEFT."+S.getFirstName()+")";
        			group += "Layer"+(i+1);
        		}
        		smoking += "Smokin.Layer"+(i+1)+";\n";
        		layerRec += "STRING Layer"+(i+1)+";\n";
        		i++;
        	}
        	
        	ecl += "URec := RECORD\nUNSIGNED uid;\n"+this.getDatasetName()+";\nEND;\n";
        	ecl += "URec Trans("+this.getDatasetName()+" L, INTEGER C) := TRANSFORM\nSELF.uid := C;\nSELF := L;\nEND;\n"; 
        	ecl += "MyDS := PROJECT("+getDatasetName()+",Trans(LEFT,COUNTER));\n";
        	ecl += "NormRec:=RECORD\nINTEGER4 id;\nINTEGER4 number;\nSTRING field;\nREAL value;\nEND;\n";
        	ecl += "OutDS:=NORMALIZE(MyDS,"+rows.size()+",TRANSFORM(NormRec,SELF.id:=LEFT.uid;SELF.number:=COUNTER;SELF.field:=CHOOSE(COUNTER,"+field+");" +
        			"SELF.value:=CHOOSE(COUNTER,"+value+")));\n";
        	
        	if(layers.size()>0){
        		ecl += "LayerRec := RECORD\nINTEGER4 id;\n"+layerRec+"END;\n";
        		ecl += "LayerDS := NORMALIZE(MyDS,1,TRANSFORM(LayerRec,SELF.id:=LEFT.uid;"+layer+"));\n";
        	}
        	
        	ecl += "RankableField := RECORD\nOutDS;\nUNSIGNED pos:=0;\nEND;\n";
        	ecl += "T:=TABLE(SORT(OutDS,number,field,value),RankableField);\n";
        	ecl += "TYPEOF(T) add_rank(T le, UNSIGNED c):=TRANSFORM\nSELF.pos:=c;\nSELF:=le;\nEND;\n";
        	ecl += "P:=PROJECT(T,add_rank(LEFT,COUNTER));\n";
        	ecl += "RS:=RECORD\nSeq:=MIN(GROUP,P.pos);\nP.number;\nEND;\n";
        	ecl += "Splits:= TABLE(P,RS,number,FEW);\n";
        	ecl += "TYPEOF(T) to(P le, Splits ri):=TRANSFORM\nSELF.pos:=1+le.pos-ri.Seq;\nSELF:=le;\nEND;\n";
        	ecl += "outfile:= JOIN(P,Splits,LEFT.number=RIGHT.number,to(LEFT,RIGHT),LOOKUP);\n";
        	
        	ecl += "N:=COUNT("+getDatasetName()+");\n";
        	ecl += "SET OF INTEGER buckets := ["+buck+"];\n";
 
        	ecl += "NewTab:=DEDUP(outfile, LEFT.field=RIGHT.field AND LEFT.value=RIGHT.value);\n";
        	
        	
        	ecl += "NewRec:=RECORD\nNewTab;\nREAL bucket;\nEND;\n";
        	ecl += "NewRec Tr(NewTab L, INTEGER C) := TRANSFORM\nSELF.bucket := ROUNDUP(L.pos*buckets[L.number]/(N+1))-1;\nSELF := L;\nEND;\n";
        	ecl += "Tab := PROJECT(NewTab,Tr(LEFT,COUNTER));\n";
        	//ecl += "Answer := JOIN(Tab,OutFile, LEFT.field = RIGHT.field AND LEFT.value = RIGHT.value);\n";
        	//ecl += "OUTPUT(Answer,NAMED('Buckets'));\n";
        	ecl += "Rec1 := RECORD\nTab.value;\nTab.bucket;\nTab.field;\nTab.number;\nEND;\n";
        	ecl += "Tab1 := TABLE(Tab,Rec1);\n";
        	ecl += "Tab2 := DEDUP(JOIN(Outfile,Tab1,LEFT.field = RIGHT.field AND LEFT.value = RIGHT.value),LEFT.field = RIGHT.field AND LEFT.pos = RIGHT.pos);\n";
        	ecl += "TabRec := RECORD\nINTEGER id;\nREAL left_bucket;\nREAL right_bucket;\nSTRING left_field;\nREAL left_value;\nSTRING right_field;\nREAL right_value;\nEND;\n";
        	ecl += "TabRec rt(Tab2 L, Tab2 R) := TRANSFORM\nSELF.id := L.id;\nSELF.left_bucket := L.bucket;\nSELF.right_bucket := R.bucket;\n" +
        		"SELF.left_field := L.field;SELF.left_value := L.value;\nSELF.right_field := R.field;\nSELF.right_value := R.value;\nEND;\n";
        	ecl += "Jhakaas := JOIN(Tab2,Tab2,LEFT.id = RIGHT.id AND LEFT.field < RIGHT.field ,rt(LEFT,RIGHT));\n";
        	
        	if(layers.size()>0){
        		ecl += "Smokin := JOIN(Jhakaas,LayerDS,LEFT.id = RIGHT.id, FULL OUTER);\n";
        		ecl += "MyRec := RECORD\nSmokin.left_bucket;\nSmokin.right_bucket;\nSmokin.left_field;\nSmokin.right_field;\n" +
        				smoking +
        				"INTEGER cnt := COUNT(GROUP);\nEND;\n";
        		ecl += "MyTable := TABLE(Smokin,MyRec,left_bucket,right_bucket,left_field,right_field,"+group+");\n";
        		if(persist.equalsIgnoreCase("true")){
            		if(outputName != null && !(outputName.trim().equals(""))){
            			ecl += "OUTPUT(MyTable"+",,'~"+outputName+"::tabulateBuckets', __compressed__, overwrite,NAMED('TabulateBuckets'))"+";\n";
            		}else{
            			ecl += "OUTPUT(MyTable"+",,'~"+defJobName+"::tabulateBuckets', __compressed__, overwrite,NAMED('TabulateBuckets'))"+";\n";
            		}
            	}
            	else{
            		ecl += "OUTPUT(MyTable,NAMED('TabulateBuckets'));\n";
            	}
        		//ecl += "OUTPUT(MyTable,THOR);\n";
        	}
        	else{
        	      	
	        	ecl += "MyRec := RECORD\nJhakaas.left_bucket;\nJhakaas.right_bucket;\nJhakaas.left_field;\nJhakaas.right_field;\nINTEGER cnt := COUNT(GROUP);\nEND;\n";
	        	ecl += "MyTable := SORT(TABLE(Jhakaas,MyRec,left_bucket,right_bucket,left_field,right_field),left_field,right_field);\n";
	        	
	        	if(persist.equalsIgnoreCase("true")){
            		if(outputName != null && !(outputName.trim().equals(""))){
            			ecl += "OUTPUT(MyTable"+",,'~"+outputName+"::tabulateBuckets', __compressed__, overwrite,NAMED('TabulateBuckets'))"+";\n";
            		}else{
            			ecl += "OUTPUT(MyTable"+",,'~"+defJobName+"::tabulateBuckets', __compressed__, overwrite,NAMED('TabulateBuckets'))"+";\n";
            		}
            	}
            	else{
            		ecl += "OUTPUT(MyTable,NAMED('TabulateBuckets'));\n";
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
            return result;
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
    		out +=  p.getFirstName()+","+p.getBuckets();
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
        		String[] S = strLine[i].split(",");
        		Player P = new Player();
        		P.setFirstName(S[0]);
        		if(S.length>1)
        			P.setBuckets(S[1]);
        		else
        			P.setBuckets(" ");
        		rows.add(P);
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
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "people")) != null)
                openPeople(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "people")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "rows")) != null)
                openRows(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "rows")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "layers")) != null)
                openLayers(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "layers")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "label")) != null)
                setLabel(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "label")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outputName")) != null)
                setOutputName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outputName")));
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
        retval += "		<people><![CDATA[" + this.savePeople() + "]]></people>" + Const.CR;
        retval += "		<rows><![CDATA[" + this.saveRows() + "]]></rows>" + Const.CR;
        retval += "		<layers><![CDATA[" + this.saveLayers() + "]]></layers>" + Const.CR;
        retval += "		<label><![CDATA[" + label + "]]></label>" + Const.CR;
        retval += "		<outputName><![CDATA[" + outputName + "]]></outputName>" + Const.CR;
        retval += "		<persist_Output_Checked><![CDATA[" + persist + "]]></persist_Output_Checked>" + Const.CR;
        retval += "		<defJobName><![CDATA[" + defJobName + "]]></defJobName>" + Const.CR;
        return retval;

    }

    public void loadRep(Repository rep, ObjectId id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
            throws KettleException {
        try {
        	
        	if(rep.getStepAttributeString(id_jobentry, "datasetName") != null)
                DatasetName = rep.getStepAttributeString(id_jobentry, "datasetName"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "people") != null)
                this.openPeople(rep.getStepAttributeString(id_jobentry, "people")); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "rows") != null)
                this.openRows(rep.getStepAttributeString(id_jobentry, "rows")); //$NON-NLS-1$
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
            rep.saveStepAttribute(id_job, getObjectId(), "people", this.savePeople()); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "rows", this.saveRows()); //$NON-NLS-1$
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
