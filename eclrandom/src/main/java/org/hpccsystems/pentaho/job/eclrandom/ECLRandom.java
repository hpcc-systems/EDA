/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.eclrandom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hpccsystems.javaecl.Dataset;
import org.hpccsystems.javaecl.GetHeader;
import org.hpccsystems.javaecl.Header;
import org.hpccsystems.recordlayout.RecordBO;
import org.hpccsystems.recordlayout.RecordList;
import org.pentaho.di.cluster.SlaveServer;
import org.pentaho.di.compatibility.Value;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Result;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entry.JobEntryBase;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.w3c.dom.Node;
import org.hpccsystems.eclguifeatures.AutoPopulate;
import org.hpccsystems.ecljobentrybase.*;

/**
 *
 * @author ChambersJ
 */
public class ECLRandom extends ECLJobEntry{//extends JobEntryBase implements Cloneable, JobEntryInterface {
    
    private String datasetName = "";
    private String inrecordName = "";
    private String outrecordName = "";
    private String transform = "";
    private String resultDataset = "";

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getresultDatasetName() {
        return resultDataset;
    }

    public void setresultDatasetName(String resultDataset) {
        this.resultDataset = resultDataset;
    }

    
    public String getInRecordName() {
        return inrecordName;
    }

    public void setInRecordName(String inrecordName) {
        this.inrecordName = inrecordName;
    }

    @Override
    public Result execute(Result prevResult, int k) throws KettleException {
    	Result result = prevResult;
        if(result.isStopped()){
        	
        }
        else{
        	String project = "";
        	StringBuilder sb = new StringBuilder();
        	outrecordName = "MyOutRec";
        	transform = "MyTrans"; 
        	String outRecordFormat = "INTEGER rand;\n"+this.inrecordName+";\n";
        	String parameterName = "L";
        	String transformFormat = "SELF.rand := C;\n SELF :="+parameterName+";\n";

            sb.append(this.outrecordName).append(" := ").append(" RECORD \r\n");
            sb.append(outRecordFormat).append(" \r\n");
            sb.append("END; \r\n");

            sb.append(this.outrecordName).append(" ").append(this.transform).append("(").append(this.inrecordName).append(" ").append(parameterName);
            
            sb.append(", INTEGER C) := TRANSFORM \r\n");
            
            sb.append(transformFormat).append(" \r\n");
            sb.append("END; \r\n");
            
            sb.append(this.resultDataset).append(" := project(").append(datasetName).append(",").append(transform).append("(LEFT");
            
            sb.append(", RANDOM())) : PERSIST(\'~INTRO::KS::CLASS::RAND"+resultDataset+"\'); \r\n");
            
            project = sb.toString();
            logBasic("Random Job =" + project); 
	        
	        result.setResult(true);
	        
	        RowMetaAndData data = new RowMetaAndData();
	        data.addValue("ecl", Value.VALUE_TYPE_STRING, project);
	        
	        
	        List list = result.getRows();
	        list.add(data);
	        String eclCode = parseEclFromRowData(list);
	        result.setRows(list);
	        result.setLogText("ECLRandom executed, ECL code added");
        }
        
        return result;
    }
    @Override
    public void loadXML(Node node, List<DatabaseMeta> list, List<SlaveServer> list1, Repository rpstr) throws KettleXMLException {
        try {
            super.loadXML(node, list, list1);
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "inrecord_name")) != null)
                setInRecordName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "inrecord_name")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")) != null)
                setDatasetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "resultDataset")) != null)
                setresultDatasetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "resultDataset")));
            
        } catch (Exception e) {
            throw new KettleXMLException("ECL Dataset Job Plugin Unable to read step info from XML node", e);
        }

    }

    public String getXML() {
        String retval = "";
        
        retval += super.getXML();
      
        retval += "		<inrecord_name eclIsDef=\"true\" eclType=\"record\"><![CDATA[" + inrecordName + "]]></inrecord_name>" + Const.CR;
        
        retval += "		<dataset_name eclIsDef=\"true\" eclType=\"dataset\"><![CDATA[" + datasetName + "]]></dataset_name>" + Const.CR;
        retval += "		<resultdataset eclIsDef=\"true\" eclType=\"dataset\"><![CDATA[" + resultDataset + "]]></resultdataset>" + Const.CR;
        
        return retval;

    }

    public void loadRep(Repository rep, ObjectId id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
            throws KettleException {
        try {
            if(rep.getStepAttributeString(id_jobentry, "datasetName") != null)
                datasetName = rep.getStepAttributeString(id_jobentry, "datasetName"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "resultdataset") != null)
                resultDataset = rep.getStepAttributeString(id_jobentry, "resultdataset"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "inrecordName") != null)
                inrecordName = rep.getStepAttributeString(id_jobentry, "inrecordName"); //$NON-NLS-1$
        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }

    public void saveRep(Repository rep, ObjectId id_job) throws KettleException {
        try {
            rep.saveStepAttribute(id_job, getObjectId(), "datasetName", datasetName); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "resultdataset", resultDataset); //$NON-NLS-1$
            
            rep.saveStepAttribute(id_job, getObjectId(), "inrecordName", inrecordName); //$NON-NLS-1$
            
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
