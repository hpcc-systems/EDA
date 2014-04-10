/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.ecloutliers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hpccsystems.javaecl.Filter;
import org.hpccsystems.javaecl.Table;
import org.hpccsystems.mapper.MainMapperForOutliers;
import org.hpccsystems.mapper.MapperRecordList;
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
public class ECLOutliers extends ECLJobEntry{//extends JobEntryBase implements Cloneable, JobEntryInterface {
	
	private String Name = "";	
	private String DatasetName = "";
	private java.util.List people = new ArrayList();
	private String resultDataset = "";
	public String filterStatement = "";
	private MapperRecordList mapperRecList = new MapperRecordList();
	private List rules = new ArrayList();
	
	public List<String> getRulesList() {
		return rules;
	}

	public void setRulesList(List<String> rulesList) {
		this.rules = rulesList;
	}

	public MapperRecordList getMapperRecList() {
		return mapperRecList;
	}

	public void setMapperRecList(MapperRecordList mapperRecList) {
		this.mapperRecList = mapperRecList;
	}
		
	public String getFilterStatement() {
		return filterStatement;
	}
	
	public void setFilterStatement(String filterStatement) {
		this.filterStatement = filterStatement;
	}
	
    public String getresultDatasetName() {
        return resultDataset;
    }

    public void setresultDatasetName(String resultDataset) {
        this.resultDataset = resultDataset;
    }
	
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

	public String getDatasetName(){
		return DatasetName;
	}
    
	public void setDatasetName(String DatasetName){
		this.DatasetName = DatasetName;
	}
	
    @Override
    public Result execute(Result prevResult, int k) throws KettleException {
    	
        /*Result result = prevResult;
        if(result.isStopped()){
        	return result;
        }
        else{
        	String ecl = "";String filter = "";boolean flag = true;int j = 0;
        	for(Iterator it = people.iterator(); it.hasNext();){
        		Player P = (Player) it.next();
        		if(P.getMin().equals("") && !P.getMax().equals("")){
        			if(!flag){filter += " OR ";}
        			filter += "("+P.getFirstName()+"<="+P.getMax()+")";
        			flag = false;        			
        		}
        		else if(P.getMax().equals("") && !P.getMin().equals("")){
        			if(!flag){filter += " OR ";}
        			filter += "("+P.getFirstName()+">="+P.getMin()+")";        				
        			flag = false;
        		}
        		else if(!P.getMin().equals("") && !P.getMax().equals("")){
        			if(!flag){filter += " OR ";}
        			filter += "("+P.getFirstName()+" BETWEEN "+P.getMin()+" AND "+P.getMax()+")";
        			flag = false;
        		}        		        	
        		else if(P.getMin().equals("") && P.getMax().equals("")){
        			filter += "";
        			flag = false;
        			j++;
        		}
        	}
        	if(j != people.size())
        		ecl += getDatasetName()+"_Filtered:="+getDatasetName()+"("+filter+");\n";
        	else
        		ecl += getDatasetName()+"_Filtered:="+getDatasetName()+";\n";
        	
	        logBasic("Outliers plugin =" + ecl + " " + j); 
	        
	        result.setResult(true);
	        
	        RowMetaAndData data = new RowMetaAndData();
	        data.addValue("ecl", Value.VALUE_TYPE_STRING, ecl);
	        
	        
	        List list = result.getRows();
	        list.add(data);
	        String eclCode = parseEclFromRowData(list);
	        result.setRows(list);
	        result.setLogText("ECLPercentileBuckets executed, ECL code added");
	        return result;
        }*/
    	
    	Result result = modifyResults(prevResult);
        if(result.isStopped()){
        	return result;
 		}
        Filter filter = new Filter();
        filter.setName(this.getName());

        filter.setInRecordName(this.getDatasetName());
       
        filter.setFilterStatement(this.getFilterStatement());
        
        String ecl = filter.ecl();

        logBasic("{Project Job} Execute = " + ecl);
        
        logBasic("{Project Job} Previous =" + result.getLogText());
        
        ecl += "OUTPUT(Outliers);";
        
        result.setResult(true);
        
        RowMetaAndData data = new RowMetaAndData();
        data.addValue("ecl", Value.VALUE_TYPE_STRING, ecl);
        
        List list = result.getRows();
        list.add(data);
        String eclCode = parseEclFromRowData(list);
        
        result.setRows(list);
        return result;
    }
    
    public String saveRules(){
    	String out = "";
    	rules = getRulesList();
    	   
    	Iterator it = rules.iterator();
    	boolean isFirst = true;
    	while(it.hasNext()){
    		if(!isFirst){
    			out+="|";
    		}
    		String p = (String)it.next();
    		out +=  p;
            isFirst = false;
    	}
    	return out;
    }

    public void openRules(String in){
        String[] strLine = in.split("\\|");
        int len = strLine.length;
        if(len>0){
        	rules = new ArrayList();
        	for(int i = 0; i<len; i++){
        		String S = strLine[i];
        		rules.add(S);
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
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "rules")) != null)
                openRules(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "rules")));

            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "resultdataset")) != null)
                setresultDatasetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "resultdataset")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "filterStatement")) != null)
            	setFilterStatement(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "filterStatement")));
            
            
        } catch (Exception e) {
            throw new KettleXMLException("ECL Dataset Job Plugin Unable to read step info from XML node", e);
        }

    }

    public String getXML() {
        String retval = "";
        
        retval += super.getXML();
        retval += "		<name><![CDATA[" + Name + "]]></name>" + Const.CR;
        retval += "		<rules eclIsDef=\"true\" eclType=\"rules\"><![CDATA[" + this.saveRules() + "]]></rules>" + Const.CR;
        retval += "		<dataset_name><![CDATA[" + DatasetName + "]]></dataset_name>" + Const.CR;
        retval += "		<resultdataset eclIsGraphable=\"true\" eclType=\"dataset\"><![CDATA[" + resultDataset + "]]></resultdataset>" + Const.CR;
        retval += "		<filterStatement><![CDATA[" + this.filterStatement + "]]></filterStatement>" + Const.CR;
        
        return retval;

    }

    public void loadRep(Repository rep, ObjectId id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
            throws KettleException {
        try {
        	if(rep.getStepAttributeString(id_jobentry, "Name") != null)
                Name = rep.getStepAttributeString(id_jobentry, "Name"); //$NON-NLS-1$
        	
        	if(rep.getStepAttributeString(id_jobentry, "datasetName") != null)
                DatasetName = rep.getStepAttributeString(id_jobentry, "datasetName"); //$NON-NLS-1$

            if(rep.getStepAttributeString(id_jobentry, "rules") != null)
                this.openRules(rep.getStepAttributeString(id_jobentry, "rules")); //$NON-NLS-1$
            
            if(rep.getStepAttributeString(id_jobentry, "resultdataset") != null)
                resultDataset = rep.getStepAttributeString(id_jobentry, "resultdataset"); //$NON-NLS-1$
            
            if(rep.getStepAttributeString(id_jobentry, "filterStatement") != null)
            	filterStatement = rep.getStepAttributeString(id_jobentry, "filterStatement"); //$NON-NLS-1$

        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }

    public void saveRep(Repository rep, ObjectId id_job) throws KettleException {
        try {
        	rep.saveStepAttribute(id_job, getObjectId(), "datasetName", DatasetName); //$NON-NLS-1$
        	
        	rep.saveStepAttribute(id_job, getObjectId(), "Name", Name); //$NON-NLS-1$
        	
            rep.saveStepAttribute(id_job, getObjectId(), "rules", this.saveRules()); //$NON-NLS-1$

            rep.saveStepAttribute(id_job, getObjectId(), "resultdataset", resultDataset); //$NON-NLS-1$
            
            rep.saveStepAttribute(id_job, getObjectId(), "filterStatement", this.filterStatement); //$NON-NLS-1$

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
