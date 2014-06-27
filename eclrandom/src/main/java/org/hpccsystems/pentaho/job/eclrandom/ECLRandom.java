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
 * @author KeshavS
 */
public class ECLRandom extends ECLJobEntry{//extends JobEntryBase implements Cloneable, JobEntryInterface {
    
    private String datasetName = "";
    private String outrecordName = "";
    private String transform = "";
    private String resultDataset = "";
    
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

    @Override
    public Result execute(Result prevResult, int k) throws KettleException {
    	Result result = prevResult;
        if(result.isStopped()){
        	return result;
        }
        else{
        	
        	String project = "";
        	StringBuilder sb = new StringBuilder();
        	outrecordName = "MyOutRec";
        	transform = "MyTrans"; 
        	String outRecordFormat = "UNSIGNED DECIMAL8_8 rand;\n"+this.getDatasetName()+";\n";
        	String parameterName = "L";
        	String transformFormat = "SELF.rand := C/4294967295;\n SELF :="+parameterName+";\n";

            sb.append(this.outrecordName).append(" := ").append(" RECORD \r\n");
            sb.append(outRecordFormat).append(" \r\n");
            sb.append("END; \r\n");

            sb.append(this.outrecordName).append(" ").append(this.transform).append("(").append(this.getDatasetName()).append(" ").append(parameterName);
            
            sb.append(", UNSIGNED4 C) := TRANSFORM \r\n");
            
            sb.append(transformFormat).append(" \r\n");
            sb.append("END; \r\n");
            
            sb.append(this.resultDataset).append(" := project(").append(datasetName).append(",").append(transform).append("(LEFT");
            
            sb.append(", RANDOM())); \r\n");
            
            project = sb.toString();
            if(persist.equalsIgnoreCase("true")){
        		if(outputName != null && !(outputName.trim().equals(""))){
        			project += "OUTPUT("+this.getDatasetName()+"_with_random"+",,'~eda::"+outputName+"::random', __compressed__, overwrite,NAMED('Random'))"+";\n";
        		}else{
        			project += "OUTPUT("+this.getDatasetName()+"_with_random"+",,'~eda::"+defJobName+"::random', __compressed__, overwrite,NAMED('Random'))"+";\n";
        		}
        	}
        	else{
        		project += "OUTPUT("+this.getDatasetName()+"_with_random,NAMED('Random'));\n";
        	}
           
            //project += "OUTPUT("+this.getDatasetName()+"_with_random,THOR);\n";
            logBasic("Random Job =" + project); 
	        
	        result.setResult(true);
	        
	        RowMetaAndData data = new RowMetaAndData();
	        data.addValue("ecl", Value.VALUE_TYPE_STRING, project);
	        
	        
	        List list = result.getRows();
	        list.add(data);
	        String eclCode = parseEclFromRowData(list);
	        result.setRows(list);
	        result.setLogText("ECLRandom executed, ECL code added");
	        return result;
        }
        
        
    }
    @Override
    public void loadXML(Node node, List<DatabaseMeta> list, List<SlaveServer> list1, Repository rpstr) throws KettleXMLException {
        try {
            super.loadXML(node, list, list1);
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")) != null)
                setDatasetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "resultDataset")) != null)
                setresultDatasetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "resultDataset")));
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
      
        retval += "		<dataset_name ><![CDATA[" + datasetName + "]]></dataset_name>" + Const.CR;
        retval += "		<resultdataset eclIsGraphable=\"true\"><![CDATA[" + resultDataset + "]]></resultdataset>" + Const.CR;
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
                datasetName = rep.getStepAttributeString(id_jobentry, "datasetName"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "resultdataset") != null)
                resultDataset = rep.getStepAttributeString(id_jobentry, "resultdataset"); //$NON-NLS-1$
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
            rep.saveStepAttribute(id_job, getObjectId(), "datasetName", datasetName); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "resultdataset", resultDataset); //$NON-NLS-1$
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
