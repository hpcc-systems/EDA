/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.eclpercentilebuckets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
import org.w3c.dom.Node;
import org.hpccsystems.ecljobentrybase.*;

/**
 *
 * @author KeshavS
 */
public class ECLPercentileBuckets extends ECLJobEntry{//extends JobEntryBase implements Cloneable, JobEntryInterface {
	
	private String Name = "";
	private String Ties = "";
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
	
	public String getTies(){
		return Ties;
	}
    
	public void setTies(String Ties){
		this.Ties = Ties;
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
        	String ecl = "";String field = "";String value = "";String[] norm = normList.split("-");String buck = "";
        	String denorm = "";String myrec = "";String mytrans = "";
        	for(int i = 0; i<norm.length; i++){
        		String[] S = norm[i].split(",");
        		if(i!=norm.length-1){        			
        			field += "\'"+S[0]+"\',";
        			value += "LEFT."+S[0]+",";
        			buck += S[1]+",";
        		}
        		else{
        			field += "\'"+S[0]+"\'";
        			value += "LEFT."+S[0];
        			buck += S[1];
        		}
        		denorm += "REAL "+S[0]+"bucket;\n";
        		myrec += "SELF."+S[0]+"bucket:=0;\n";
        		mytrans += "SELF."+S[0]+":=IF(C="+(i+1)+",R.value,L."+S[0]+");\nSELF."+S[0]+"bucket:=IF(C="+(i+1)+",R.bucket,L."+S[0]+"bucket);\n";
        	}
        	
        	ecl += "URec := RECORD\nUNSIGNED uid;\n"+this.getDatasetName()+";\nEND;\n";
        	ecl += "URec Trans("+this.getDatasetName()+" L, INTEGER C) := TRANSFORM\nSELF.uid := C;\nSELF := L;\nEND;\n"; 
        	ecl += "MyDS := PROJECT("+getDatasetName()+",Trans(LEFT,COUNTER));\n";
        	
        	ecl += "NormRec:=RECORD\nINTEGER4 id;\nINTEGER4 number;\nSTRING field;\nREAL value;\nEND;\n";
        	ecl += "OutDS:=NORMALIZE(MyDS,"+norm.length+",TRANSFORM(NormRec,SELF.id:=LEFT.uid;\nSELF.number:=COUNTER;SELF.field:=CHOOSE(COUNTER,"+field+");" +
        			"SELF.value:=CHOOSE(COUNTER,"+value+")));\n";
        	
        	ecl += "RankableField := RECORD\nOutDS;\nUNSIGNED pos:=0;\nEND;\n";
        	ecl += "T:=TABLE(SORT(OutDS,number,field,value),RankableField);\n";
        	ecl += "TYPEOF(T) add_rank(T le, UNSIGNED c):=TRANSFORM\nSELF.pos:=c;\nSELF:=le;\nEND;\n";
        	ecl += "P:=PROJECT(T,add_rank(LEFT,COUNTER));\n";
        	ecl += "RS:=RECORD\nSeq:=MIN(GROUP,P.pos);\nP.number;\nEND;\n";
        	ecl += "Splits:= TABLE(P,RS,number,FEW);\n";
        	ecl += "TYPEOF(T) to(P le, Splits ri):=TRANSFORM\nSELF.pos:=1+le.pos-ri.Seq;\nSELF:=le;\nEND;\n";
        	ecl += "outfile:= JOIN(P,Splits,LEFT.number=RIGHT.number,to(LEFT,RIGHT),LOOKUP);\n";
        	
        	ecl += "N:=COUNT(MyDS);\n";
        	ecl += "SET OF INTEGER buckets := ["+buck+"];\n";
 
        	if(getTies().equals("HI")){
        		ecl += "TYPEOF(outfile) tra(Outfile L, Outfile R) := TRANSFORM\nSELF.pos := R.pos;\nSELF := L;\nEND;\n";
        		ecl += "newTab := ROLLUP(Outfile,LEFT.field = RIGHT.field AND LEFT.value = RIGHT.value,tra(LEFT,RIGHT));\n";
        	}
        	
        	else{
        		ecl += "NewTab:=DEDUP(outfile, LEFT.field=RIGHT.field AND LEFT.value=RIGHT.value);\n";
        	}
        	
        	ecl += "NewRec:=RECORD\nNewTab;\nREAL bucket;\nEND;\n";
        	ecl += "NewRec Tr(NewTab L, INTEGER C) := TRANSFORM\nSELF.bucket := ROUNDUP(L.pos*buckets[L.number]/(N+1))-1;\nSELF := L;\nEND;\n";
        	ecl += "Tab := PROJECT(NewTab,Tr(LEFT,COUNTER));\n";
        	ecl += "Rec1 := RECORD\nTab.value;\nTab.bucket;\nTab.field;\nTab.number;\nEND;\n";
        	ecl += "Tab1 := TABLE(Tab,Rec1);\n";
        	ecl += "Tab2 := DEDUP(JOIN(Outfile,Tab1,LEFT.field = RIGHT.field AND LEFT.value = RIGHT.value),LEFT.field = RIGHT.field AND LEFT.pos = RIGHT.pos);\n";
        	ecl += "Tab3 := SORT(Tab2,id,number);\n";
        	ecl += "new_record:=RECORD\nMyDS;\n"+denorm+"END;\n";
        	ecl += "new_record transfo(MyDS L, INTEGER C) := TRANSFORM\n" + myrec +
        			"SELF:=L;\nEND;\n";
        	
        	ecl += "Orig := PROJECT(MyDS,transfo(LEFT,COUNTER));\n";
        	ecl += "new_record denorm(Orig L, Tab3 R, INTEGER C):=TRANSFORM\n"+ mytrans;
        	ecl += "SELF := L;\nEND;\n";
        	ecl += "Denormed:=DENORMALIZE(Orig,Tab3,LEFT.uid=RIGHT.id,denorm(LEFT,RIGHT,COUNTER));\n";
        	ecl += "OUTPUT(Denormed,NAMED('Buckets'));\n";
        	ecl += "Reco := RECORD\nTab2.Field;\nTab2.bucket;\nminval := MIN(GROUP,Tab3.value);\nmaxval := MAX(GROUP,Tab3.value);\nEND;\n";
        	ecl += "TabRec := TABLE(Tab2,Reco,field,bucket);\n";
        	for(int i = 0;i <norm.length; i++){
        		String[] S = norm[i].split(",");
        		ecl += "OUTPUT(TabRec(field='"+S[0]+"'),NAMED('"+S[0]+"Buckets_min_max'));\n";
        	}
        	
        	
        	
	        logBasic("PercentileBuckets Job =" + ecl); 
	        
	        result.setResult(true);
	        
	        RowMetaAndData data = new RowMetaAndData();
	        data.addValue("ecl", Value.VALUE_TYPE_STRING, ecl);
	        
	        
	        List list = result.getRows();
	        list.add(data);
	        String eclCode = parseEclFromRowData(list);
	        result.setRows(list);
	        result.setLogText("ECLPercentileBuckets executed, ECL code added");
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
    		out +=  p.getFirstName()+"-"+p.getBuckets();
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
        		String[] S = strLine[i].split("-");
        		Player P = new Player();
        		P.setFirstName(S[0]);
        		P.setBuckets(S[1]);
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

            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "Ties")) != null)
                setTies(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "Ties")));
            
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
        retval += "		<Ties><![CDATA[" + Ties + "]]></Ties>" + Const.CR;
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

            if(rep.getStepAttributeString(id_jobentry, "Ties") != null)
            	Ties = rep.getStepAttributeString(id_jobentry, "Ties"); //$NON-NLS-1$
            
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
        	
        	rep.saveStepAttribute(id_job, getObjectId(), "Ties", Ties); //$NON-NLS-1$
        	
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
    
}
