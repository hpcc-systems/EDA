/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.ecloutliers;

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
public class ECLOutliers extends ECLJobEntry{//extends JobEntryBase implements Cloneable, JobEntryInterface {
	
	private String Name = "";	
	private String DatasetName = "";
	private java.util.List people = new ArrayList();
	private String resultDataset = "";
	
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
    	
    	
        Result result = prevResult;
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
        }
        
    }
    
    public String savePeople(){
    	String out = "";
    	
    	Iterator it = people.iterator();
    	boolean isFirst = true;
    	while(it.hasNext()){
    		if(!isFirst){out+="|";}
    		Player p = (Player) it.next();
    		out +=  p.getFirstName()+"-"+p.getMin()+"-"+p.getMax();
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
        		if(S.length == 2){
        			P.setMin(S[1]);
        			P.setMax("");
        		}
        		else if(S.length == 3){
        			P.setMin(S[1]);
                	P.setMax(S[2]);
        		}
        		else if(S.length == 1){
        			P.setMin("");
        			P.setMax("");
        		}
        		
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
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "people")) != null)
                openPeople(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "people")));

            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "resultdataset")) != null)
                setresultDatasetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "resultdataset")));

        } catch (Exception e) {
            throw new KettleXMLException("ECL Dataset Job Plugin Unable to read step info from XML node", e);
        }

    }

    public String getXML() {
        String retval = "";
        
        retval += super.getXML();
        retval += "		<name><![CDATA[" + Name + "]]></name>" + Const.CR;
        retval += "		<people><![CDATA[" + this.savePeople() + "]]></people>" + Const.CR;
        retval += "		<dataset_name><![CDATA[" + DatasetName + "]]></dataset_name>" + Const.CR;
        retval += "		<resultdataset eclIsGraphable=\"true\" eclType=\"dataset\"><![CDATA[" + resultDataset + "]]></resultdataset>" + Const.CR;
        
        return retval;

    }

    public void loadRep(Repository rep, ObjectId id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
            throws KettleException {
        try {
        	if(rep.getStepAttributeString(id_jobentry, "Name") != null)
                Name = rep.getStepAttributeString(id_jobentry, "Name"); //$NON-NLS-1$
        	
        	if(rep.getStepAttributeString(id_jobentry, "datasetName") != null)
                DatasetName = rep.getStepAttributeString(id_jobentry, "datasetName"); //$NON-NLS-1$

            if(rep.getStepAttributeString(id_jobentry, "people") != null)
                this.openPeople(rep.getStepAttributeString(id_jobentry, "people")); //$NON-NLS-1$
            
            if(rep.getStepAttributeString(id_jobentry, "resultdataset") != null)
                resultDataset = rep.getStepAttributeString(id_jobentry, "resultdataset"); //$NON-NLS-1$

        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }

    public void saveRep(Repository rep, ObjectId id_job) throws KettleException {
        try {
        	rep.saveStepAttribute(id_job, getObjectId(), "datasetName", DatasetName); //$NON-NLS-1$
        	
        	rep.saveStepAttribute(id_job, getObjectId(), "Name", Name); //$NON-NLS-1$
        	
            rep.saveStepAttribute(id_job, getObjectId(), "people", this.savePeople()); //$NON-NLS-1$

            rep.saveStepAttribute(id_job, getObjectId(), "resultdataset", resultDataset); //$NON-NLS-1$

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
