/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.eclunivariate;

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
//import org.hpccsystems.pentaho.job.eclunivariate.Cols;


/**
 *
 * @author KeshavS
 */
public class ECLUnivariate extends ECLJobEntry{//extends JobEntryBase implements Cloneable, JobEntryInterface {
    
    private String datasetName = "";
    private String logicalFileName = "";
    private java.util.List people = new ArrayList();
    private String checkList = "";
    private String fieldList = "";
    
	public void setPeople(java.util.List people){
		this.people = people;
	}
	
	public java.util.List getPeople(){
		return people;
	}

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getCheckList() {
        return checkList;
    }

    public void setCheckList(String checkList) {
        this.checkList = checkList;
    }
    
    public String getFieldList() {
        return fieldList;
    }

    public void setFieldList(String fieldList) {
        this.fieldList= fieldList;
    }
    
    public String getlogicalFileName() {
        return logicalFileName;
    }

    public void setlogicalFileName(String logicalFileName) {
        this.logicalFileName = logicalFileName;
    }
    
    @Override
    public Result execute(Result prevResult, int k) throws KettleException {
    	Result result = prevResult;
        if(result.isStopped()){
        	return result;
        }
        else{
        	String[] check = getCheckList().split(",");
        	String[] fieldNames = fieldList.split(",");
        	String normlist = "";int cnt = 0;
        	String List = "";
        	for(int i = 0; i<fieldNames.length; i++){
        		if(i!=fieldNames.length-1){
        			normlist += "LEFT."+fieldNames[i]+",";
        			List += "\'"+fieldNames[i]+"\',";
        		}
        		else{
        			normlist += "LEFT."+fieldNames[i];
        			List += "\'"+fieldNames[i]+"\'";
        		}
        	}
			String ecl = "URec := RECORD\nUNSIGNED uid;\n"+this.datasetName+";\nEND;\n";
        	ecl += "URec Trans("+this.datasetName+" L, INTEGER C) := TRANSFORM\nSELF.uid := C;\nSELF := L;\nEND;\n"; 
        	ecl += "MyDS := PROJECT("+datasetName+",Trans(LEFT,COUNTER));\n";
        	
        	ecl += "NumField := RECORD\nUNSIGNED id;\nSTRING field;\nREAL8 value;\nEND;\n";
        	ecl += "OutDS := NORMALIZE(MyDS,"+fieldNames.length+", TRANSFORM(NumField,SELF.id:=LEFT.uid,SELF.field:=CHOOSE(COUNTER,"+List+");SELF.value:=CHOOSE" +
        			"(COUNTER,"+normlist+")));\n";
        	ecl += "SingleField := RECORD\nOutDS.field;\n";
        	if(check[0].equals("true"))
        		{ecl += "mean:=AVE(GROUP,OutDS.value);\n";cnt++;}
        	if(check[3].equals("true"))
        		{ecl += "Sd:=SQRT(VARIANCE(GROUP,OutDS.value));\n";cnt++;}
        	if(check[4].equals("true"))
        		{ecl += "Maxval:=MAX(GROUP,OutDS.value);\n";cnt++;}
        	if(check[5].equals("true"))
        		{ecl += "Minval:=MIN(GROUP,OutDS.value);\n";cnt++;}
        	ecl += "END;\n";
    		if(cnt>0)
        		ecl += "SingleUni := TABLE(OutDS,SingleField,field);\n";
        	
        	
        	if(check[1].equals("true") || check[2].equals("true")){
        	// this can be reused
        		ecl += "n := COUNT(MyDS);\n";
	        	ecl += "RankableField := RECORD\nOutDS;\nUNSIGNED pos:=0;\nEND;\n";
	        	ecl += "T:=TABLE(SORT(OutDS,field,Value),RankableField);\n";
	        	ecl += "TYPEOF(T) add_rank(T le, UNSIGNED c):=TRANSFORM\nSELF.pos:=c;\nSELF:=le;\nEND;\n";
	        	ecl += "P:=PROJECT(T,add_rank(LEFT,COUNTER));\n";
	        	ecl += "RS:=RECORD\nSeq:=MIN(GROUP,P.pos);\nP.field;\nEND;\n";
	        	ecl += "Splits := TABLE(P,RS,field,FEW);\n";
	        	ecl += "TYPEOF(T) to(P le, Splits ri):=TRANSFORM\nSELF.pos:=1+le.pos-ri.Seq;\nSELF:=le;\nEND;\n";
	        	ecl += "outfile := JOIN(P,Splits,LEFT.field=RIGHT.field,to(LEFT,RIGHT),LOOKUP);\n";
	        	if(check[1].equals("true")){
	        		ecl += "MyT := TABLE(outfile,{field;SET OF UNSIGNED poso := IF(n%2=0,[n/2,n/2 + 1],[(n+1)/2]);},field);\n";
	        		ecl += "MedianValues:=JOIN(outfile,MyT,LEFT.field=RIGHT.field AND LEFT.pos IN RIGHT.poso);\n";
	        		ecl += "MedianTable := TABLE(MedianValues,{field;Median := AVE(GROUP, MedianValues.value);},field);\n";
	        		if(cnt == 0)
	        			ecl += "OUTPUT(MedianTable,NAMED('Median'));\n";
	        		else{
	        			ecl += "UniStats := JOIN(SingleUni,Mediantable,LEFT.field = RIGHT.field);\n";
	        			ecl += "OUTPUT(UniStats,NAMED('UniVariateStats'));\n";
	        		}
	        	}
	        	
	        	if(check[2].equals("true")){
	        		ecl += "MTable := TABLE(outfile,{field;value;vals := COUNT(GROUP);},field,value);\n";
	        		ecl += "modT := TABLE(MTable,{field;cnt:=MAX(GROUP,vals)},field);\n";
	        		ecl += "Modes:=JOIN(MTable,ModT,LEFT.field=RIGHT.field AND LEFT.vals=RIGHT.cnt);\n";
	        		ecl += "ModeTable := TABLE(Modes,{field;mode:=value;cnt});\n";
	        		ecl += "OUTPUT(ModeTable,NAMED('Mode'));\n";
	        	}
	        	
        	}
        	if(cnt>0 && check[1].equals("false"))
        		ecl += "OUTPUT(SingleUni,NAMED('UnivariateStats'));\n";
        	

        	
        	result.setResult(true);
	        
	        RowMetaAndData data = new RowMetaAndData();
	        data.addValue("ecl", Value.VALUE_TYPE_STRING, ecl);
	        
	        
	        List list = result.getRows();
	        list.add(data);
	        String eclCode = parseEclFromRowData(list);
	        result.setRows(list);
	        result.setLogText("ECLUnivariate executed, ECL code added");
        	return result;

        }
    }
    public String savePeople(){
    	String out = "";
    	
    	Iterator it = people.iterator();
    	boolean isFirst = true;
    	while(it.hasNext()){
    		if(!isFirst){out+="|";}
    		Cols p = (Cols) it.next();
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
        		Cols P = new Cols();
        		P.setFirstName(S);
        		people.add(P);
        	}
        }
    }

    
    @Override
    public void loadXML(Node node, List<DatabaseMeta> list, List<SlaveServer> list1, Repository rpstr) throws KettleXMLException {
        try {
            super.loadXML(node, list, list1);
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")) != null)
                setDatasetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "checklist")) != null)
                setCheckList(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "checklist")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "logical_file_name")) != null)
                setlogicalFileName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "logical_file_name")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "people")) != null)
                openPeople(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "people")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "fieldList")) != null)
                setFieldList(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "fieldList")));
            

        } catch (Exception e) {
            throw new KettleXMLException("ECL Dataset Job Plugin Unable to read step info from XML node", e);
        }

    }

    public String getXML() {
        String retval = "";
        
        retval += super.getXML();
        retval += "		<people><![CDATA[" + this.savePeople() + "]]></people>" + Const.CR;
        retval += "		<fieldList><![CDATA[" + fieldList + "]]></fieldList>" + Const.CR;
        retval += "		<logical_file_name><![CDATA[" + logicalFileName + "]]></logical_file_name>" + Const.CR;
        retval += "		<checklist><![CDATA[" + checkList + "]]></checklist>" + Const.CR;
        retval += "		<dataset_name><![CDATA[" + datasetName + "]]></dataset_name>" + Const.CR;		
        //<dataset_name eclIsDef=\"true\" eclType=\"dataset\"><![CDATA[" + datasetName + "]]></dataset_name>" + Const.CR;
        return retval;

    }

    public void loadRep(Repository rep, ObjectId id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
            throws KettleException {
        try {
            if(rep.getStepAttributeString(id_jobentry, "datasetName") != null)
                datasetName = rep.getStepAttributeString(id_jobentry, "datasetName"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "checklist") != null)
                checkList = rep.getStepAttributeString(id_jobentry, "checklist"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "logicalFileName") != null)
                logicalFileName = rep.getStepAttributeString(id_jobentry, "logicalFileName"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "people") != null)
                this.openPeople(rep.getStepAttributeString(id_jobentry, "people")); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "fieldList") != null)
                fieldList = rep.getStepAttributeString(id_jobentry, "fieldList"); //$NON-NLS-1$
        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }

    public void saveRep(Repository rep, ObjectId id_job) throws KettleException {
        try {
            rep.saveStepAttribute(id_job, getObjectId(), "datasetName", datasetName); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "checklist", checkList); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "logicalFileName", logicalFileName); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "people", this.savePeople()); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "fieldList", fieldList); //$NON-NLS-1$
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
