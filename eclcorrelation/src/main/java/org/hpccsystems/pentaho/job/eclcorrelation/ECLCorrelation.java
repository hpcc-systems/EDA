/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.eclcorrelation;

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
public class ECLCorrelation extends ECLJobEntry{//extends JobEntryBase implements Cloneable, JobEntryInterface {
    
    private String datasetName = "";
    private java.util.List fields = new ArrayList();
    private String method = "";
    private String fieldList = "";
    
    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getFieldList() {
        return fieldList;
    }

    public void setFieldList(String fieldList) {
        this.fieldList= fieldList;
    }
    
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

	public void setFields(java.util.List fields){
		this.fields = fields;
	}
	
	public java.util.List getFields(){
		return fields;
	}

    @Override
    public Result execute(Result prevResult, int k) throws KettleException {
    	Result result = prevResult;
        if(result.isStopped()){
        	return result;
        }
        else{
        	String[] fieldNames = fieldList.split(",");String field = "";
        	String normlist = "";
        	for(int i = 0; i<fieldNames.length; i++){
        		if(i!=fieldNames.length-1){
        			field += "\'"+fieldNames[i]+"\',";
        			normlist += "LEFT."+fieldNames[i]+",";
        		}
        		else{
        			field += "\'"+fieldNames[i]+"\'";
        			normlist += "LEFT."+fieldNames[i];
        		}
        	}
			String ecl = "URecCorr := RECORD\nUNSIGNED uid;\n"+this.datasetName+";\nEND;\n";
        	ecl += "URecCorr TransCorr("+this.datasetName+" L, INTEGER C) := TRANSFORM\nSELF.uid := C;\nSELF := L;\nEND;\n"; 
        	ecl += "MyDSCorr := PROJECT("+datasetName+",TransCorr(LEFT,COUNTER));\n";
        	
        	ecl += "NumFieldCorr := RECORD\nUNSIGNED id;\nUNSIGNED4 number;\nREAL8 value;STRING field;\nEND;\n";
        	ecl += "OutDSCorr := NORMALIZE(MyDSCorr,"+fieldNames.length+", TRANSFORM(NumFieldCorr,SELF.id:=LEFT.uid,SELF.number:=COUNTER;" +
        			"SELF.field:=CHOOSE(COUNTER,"+field+");SELF.value:=CHOOSE(COUNTER,"+normlist+")));\n";
        	
        	ecl += "RankableFieldCorr := RECORD\nOutDSCorr;\nUNSIGNED pos:=0;\nEND;\n";
        	ecl += "TCorr:=TABLE(SORT(OutDSCorr,Number,field,Value),RankableFieldCorr);\n";
        	ecl += "TYPEOF(TCorr) add_rank_corr(TCorr le, UNSIGNED c):=TRANSFORM\nSELF.pos:=c;\nSELF:=le;\nEND;\n";
        	ecl += "PCorr:=PROJECT(TCorr,add_rank_corr(LEFT,COUNTER));\n";
        	ecl += "RSCorr:=RECORD\nSeq:=MIN(GROUP,PCorr.pos);\nPCorr.number;\nEND;\n";
        	ecl += "SplitsCorr := TABLE(PCorr,RSCorr,number,FEW);\n";
        	ecl += "TYPEOF(TCorr) toCorr(PCorr le, SplitsCorr ri):=TRANSFORM\nSELF.pos:=1+le.pos-ri.Seq;\nSELF:=le;\nEND;\n";
        	ecl += "outfileCorr := JOIN(PCorr,SplitsCorr,LEFT.number=RIGHT.number,toCorr(LEFT,RIGHT),LOOKUP);\n";
        	
        	ecl += "modeRecCorr:=RECORD\noutfileCorr.number;\noutfileCorr.value;\noutfileCorr.pos;\noutfileCorr.field;\nvals:=COUNT(GROUP);\nEND;\n";
        	ecl += "MTableCorr:=TABLE(outfileCorr,modeRecCorr,number,field,value);\n";
        	ecl += "newRecCorr:=RECORD\nMTableCorr.number;\nMTableCorr.value;\nMTableCorr.field;\npo:=(MTableCorr.pos*MtableCorr.vals + ((MtableCorr.vals-1)*MtableCorr.vals/2))/MtableCorr.vals;\nEND;\n";
        	ecl += "newTableCorr := TABLE(MTableCorr,newRecCorr);\n";
        	ecl += "TestTabCorr := JOIN(outfileCorr,newTableCorr,LEFT.number = RIGHT.number AND LEFT.value = RIGHT.value);\n";
        	
        	
        	ecl += "MyRecCorr:=RECORD\nTestTabCorr;\nEND;\n";
        	ecl += "T1Corr:=TABLE(TestTabCorr,MyRecCorr,id,number,field);\n";
        	
        	ecl += "SingleForm := RECORD\nT1Corr.number;\nT1Corr.field;\nREAL8 meanP := AVE(GROUP,T1Corr.value);\nREAL8 sdP := SQRT(VARIANCE(GROUP,T1Corr.value));\nREAL8 meanS := AVE(GROUP,T1Corr.po);\nREAL8 sdS := SQRT(VARIANCE(GROUP,T1Corr.po));\nEND;\n"; 
        	ecl += "single := TABLE(T1Corr,SingleForm,number,field,FEW);\n";
        	ecl += "PairRec := RECORD\nUNSIGNED4 left_number;\nUNSIGNED4 right_number;\nSTRING left_field;\nSTRING right_field;\nREAL8 xyP;\nREAL8 xyS;\nEND;\n";
        	ecl += "PairRec product(T1Corr L, T1Corr R) := TRANSFORM\nSELF.left_number := L.number;\nSELF.right_number := R.number;\nSELF.left_field:=L.field;\nSELF.right_field:=R.field;\nSELF.xyP := L.value*R.value;\nSELF.xyS := L.po*R.po;\nEND;\n";
        	ecl += "pairs := JOIN(T1Corr,T1Corr,LEFT.id=RIGHT.id AND LEFT.number<RIGHT.number,product(LEFT,RIGHT));\n";
        	
        	ecl += "PairAccum := RECORD\npairs.left_number;\npairs.right_number;\npairs.left_field;\npairs.right_field;\ne_xyS := SUM(GROUP,pairs.xyS);\ne_xyP := SUM(GROUP,pairs.xyP);\nEND;\n";
        	
        	ecl += "exys := TABLE(pairs,PairAccum,left_number,right_number,left_field,right_field,FEW);\n";
        	ecl += "with_x := JOIN(exys,single,LEFT.left_number = RIGHT.number,LOOKUP);\n";
        	
        	if(this.method.equalsIgnoreCase("Pearson")){
        	
	        	ecl += "PearRec := RECORD\nUNSIGNED left_number;\nUNSIGNED right_number;\nSTRING left_field;\nSTRING right_field;\nREAL8 Pearson;\nEND;\nnCorr := COUNT(MyDSCorr);\n";
	        	ecl += "PearRec Tran(with_x L, single R) := TRANSFORM\nSELF.Pearson := (L.e_xyP - nCorr*L.meanP*R.meanP)/(nCorr*L.sdP*R.sdP);\nSELF := L;\nEND;\n";
	        	ecl += "pears := JOIN(with_x,single,LEFT.right_number=RIGHT.number,Tran(LEFT,RIGHT),LOOKUP);\n";
	        	ecl += "OUTPUT(TABLE(pears,{left_field,right_field,Pearson}),NAMED('Pearson'));\n";
        	}
        	else if(this.method.equalsIgnoreCase("Spearman")){
	        	ecl += "SPearRec := RECORD\nUNSIGNED left_number;\nUNSIGNED right_number;\nSTRING left_field;\nSTRING right_field;\nREAL8 Spearman;\nEND;\nnCorr := COUNT(MyDSCorr);\n";
	        	ecl += "SPearRec Tran(with_x L, single R) := TRANSFORM\nSELF.Spearman := (L.e_xyS - nCorr*L.meanS*R.meanS)/(nCorr*L.sdS*R.sdS);\nSELF := L;\nEND;\n";
	        	ecl += "Spears := JOIN(with_x,single,LEFT.right_number=RIGHT.number,Tran(LEFT,RIGHT),LOOKUP);\n";
	        	ecl += "OUTPUT(TABLE(Spears,{left_field,right_field,Spearman}),NAMED('Spearman'));\n";

        	}
        	
        	logBasic(ecl);
        	
        	result.setResult(true);
	        
	        RowMetaAndData data = new RowMetaAndData();
	        data.addValue("ecl", Value.VALUE_TYPE_STRING, ecl);
	        
	        
	        List list = result.getRows();
	        list.add(data);
	        String eclCode = parseEclFromRowData(list);
	        result.setRows(list);
	        result.setLogText("ECLCorrelation executed, ECL code added");
        	return result;
        }
    }
    
    public String saveFields(){
    	String out = "";
    	
    	Iterator it = fields.iterator();
    	boolean isFirst = true;
    	while(it.hasNext()){
    		if(!isFirst){out+="|";}
    		Cols p = (Cols) it.next();
    		out +=  p.getFirstName();
            isFirst = false;
    	}
    	return out;
    }

    public void openFields(String in){
        String[] strLine = in.split("[|]");
        int len = strLine.length;
        if(len>0){
        	fields = new ArrayList();
        	for(int i = 0; i<len; i++){
        		String S = strLine[i];
        		Cols P = new Cols();
        		P.setFirstName(S);
        		fields.add(P);
        	}
        }
    }

    
    
    
    
    @Override
    public void loadXML(Node node, List<DatabaseMeta> list, List<SlaveServer> list1, Repository rpstr) throws KettleXMLException {
        try {
            super.loadXML(node, list, list1);
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")) != null)
                setDatasetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "method")) != null)
                setMethod(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "method")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "fields")) != null)
                openFields(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "fields")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "fieldList")) != null)
                setFieldList(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "fieldList")));

            
        } catch (Exception e) {
            throw new KettleXMLException("ECL Dataset Job Plugin Unable to read step info from XML node", e);
        }

    }

    public String getXML() {
        String retval = "";
        
        retval += super.getXML();
      
        retval += "		<method><![CDATA[" + method + "]]></method>" + Const.CR;
        retval += "		<fieldList><![CDATA[" + fieldList + "]]></fieldList>" + Const.CR;
        retval += "		<fields><![CDATA[" + this.saveFields() + "]]></fields>" + Const.CR;
        retval += "		<dataset_name><![CDATA[" + datasetName + "]]></dataset_name>" + Const.CR;

        
        return retval;

    }

    public void loadRep(Repository rep, ObjectId id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
            throws KettleException {
        try {
            if(rep.getStepAttributeString(id_jobentry, "datasetName") != null)
                datasetName = rep.getStepAttributeString(id_jobentry, "datasetName"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "method") != null)
                method = rep.getStepAttributeString(id_jobentry, "method"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "fields") != null)
                this.openFields(rep.getStepAttributeString(id_jobentry, "fields")); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "fieldList") != null)
                fieldList = rep.getStepAttributeString(id_jobentry, "fieldList"); //$NON-NLS-1$

        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }

    public void saveRep(Repository rep, ObjectId id_job) throws KettleException {
        try {
            rep.saveStepAttribute(id_job, getObjectId(), "datasetName", datasetName); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "fields", this.saveFields()); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "method", method); //$NON-NLS-1$
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
