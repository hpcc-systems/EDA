/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.eclfrequency;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hpccsystems.javaecl.GetHeader;
import org.hpccsystems.javaecl.Header;
import org.hpccsystems.javaecl.Table;
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
public class ECLFrequency extends ECLJobEntry{//extends JobEntryBase implements Cloneable, JobEntryInterface {
	
	private String Name = "";
	private String Sort = "";
	private String DatasetName = "";
	private String normList = "";
	private String tablename = "";
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
	public String getSort(){
		return Sort;
	}
    
	public void setSort(String Sort){
		this.Sort = Sort;
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
    
	public String gettablename(){
		return tablename;
	}
	
	public void settablename(String tablename){
		this.tablename = tablename;
	}

    @Override
    public Result execute(Result prevResult, int k) throws KettleException {
    	
    	
        Result result = prevResult;
        if(result.isStopped()){
        	
        }
        else{
        	//String sort = Sort;
	        String format = ""; String frequency = "";String[] norm = this.normList.split("-");
	        for(int i = 0; i < norm.length; i++){
	        	Table freq = new Table();
	        	String[] cols = norm[i].split(",");
		        freq.setName(gettablename()+Integer.toString(i));
		        freq.setRecordset(this.DatasetName);
		        
		        freq.setExpression(cols[0]);
		        
		        format = this.DatasetName+"."+cols[0]+";\nfre"+Integer.toString(i)+":= COUNT(GROUP);";
		        
		        freq.setFormat(format);
		        
		        format = new String();
		        if(getSort().equals("NO") || getSort().equals("")){
		        	freq.setSize("FEW");
		        	frequency += freq.ecl() + "OUTPUT("+gettablename()+Integer.toString(i)+",NAMED(\'"+cols[0]+"\'));\n";
		        }
		        else{
		        if(cols[2].equals("NAME")){
			       	if(cols[1].equals("DESC")){
			       		freq.setSize("FEW");
			       		frequency += freq.ecl() +gettablename()+Integer.toString(i)+"s:="+"SORT("+gettablename()+Integer.toString(i)+ ",-"+cols[0]+");\n";
			       		frequency +="OUTPUT("+gettablename()+Integer.toString(i)+"s,NAMED(\'"+cols[0]+"\'));\n";
			       	}
			       	else{
			       		if(cols[1].equals("ASC"))
			       			frequency += freq.ecl() + "OUTPUT("+gettablename()+Integer.toString(i)+",NAMED(\'"+cols[0]+"\'));\n";
			       		else{
			       			freq.setSize("FEW");
				        	frequency += freq.ecl() + "OUTPUT("+gettablename()+Integer.toString(i)+",NAMED(\'"+cols[0]+"\'));\n";
			       		}
			       	}
			       	
			       }
			       else if(cols[2].equals("VALUE")){
			       		if(cols[1].equals("DESC")){
			       			freq.setSize("FEW");
			       			frequency += freq.ecl() +gettablename()+Integer.toString(i)+"s:="+ "SORT("+gettablename()+Integer.toString(i)+ ",-fre"+Integer.toString(i)+");\n";
			       			frequency +="OUTPUT("+gettablename()+Integer.toString(i)+"s,NAMED(\'"+cols[0]+"\'));\n";
			       		}
			       		else{
			       			if(cols[1].equals("ASC")){
			       				freq.setSize("FEW");
			       				frequency += freq.ecl() +gettablename()+Integer.toString(i)+"s:="+ "SORT("+gettablename()+Integer.toString(i)+ ",fre"+Integer.toString(i)+");\n";
			       				frequency += "OUTPUT("+gettablename()+Integer.toString(i)+"s,NAMED(\'"+cols[0]+"\'));\n";
			       			}
			       			else{
				       			freq.setSize("FEW");
					        	frequency += freq.ecl() + "OUTPUT("+gettablename()+Integer.toString(i)+",NAMED(\'"+cols[0]+"\'));\n";
				       		}
			       		}
			       	}
			        else{
			        	freq.setSize("FEW");
					   	frequency += freq.ecl() + "OUTPUT("+gettablename()+Integer.toString(i)+",NAMED(\'"+cols[0]+"\'));\n";
			        }
		        }
	        }
	        
	
	        logBasic("Frequency Job =" + frequency);//{Dataset Job} 
	        
	        result.setResult(true);
	        
	        RowMetaAndData data = new RowMetaAndData();
	        data.addValue("ecl", Value.VALUE_TYPE_STRING, frequency);
	        
	        
	        List list = result.getRows();
	        list.add(data);
	        String eclCode = parseEclFromRowData(list);
	        result.setRows(list);
	        result.setLogText("ECLFrequency executed, ECL code added");
        }
        return result;
    }
    
    public String savePeople(){
    	String out = "";
    	
    	Iterator it = people.iterator();
    	boolean isFirst = true;
    	while(it.hasNext()){
    		if(!isFirst){out+="|";}
    		Player p = (Player) it.next();
    		out +=  p.getFirstName()+","+p.getSort().toString();
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
        		String[] S = strLine[i].split(",");
        		Player P = new Player();
        		P.setFirstName(S[0]);
        		P.setSort(Integer.parseInt(S[1]));
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
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "tablename")) != null)
                settablename(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "tablename")));

            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "Sort")) != null)
                setSort(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "Sort")));
            
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
        retval += "		<Sort eclIsDef=\"true\" eclType=\"frequency\"><![CDATA[" + Sort + "]]></Sort>" + Const.CR;
        retval += "		<tablename><![CDATA[" + tablename + "]]></tablename>" + Const.CR;
        retval += "		<normList><![CDATA[" + this.getnormList() + "]]></normList>" + Const.CR;
        retval += "		<dataset_name eclIsDef=\"true\" eclType=\"frequency\"><![CDATA[" + DatasetName + "]]></dataset_name>" + Const.CR;
        
        return retval;

    }

    public void loadRep(Repository rep, ObjectId id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
            throws KettleException {
        try {
        	if(rep.getStepAttributeString(id_jobentry, "Name") != null)
                Name = rep.getStepAttributeString(id_jobentry, "Name"); //$NON-NLS-1$
        	
        	if(rep.getStepAttributeString(id_jobentry, "datasetName") != null)
                DatasetName = rep.getStepAttributeString(id_jobentry, "datasetName"); //$NON-NLS-1$
            
            if(rep.getStepAttributeString(id_jobentry, "tablename") != null)
            	tablename = rep.getStepAttributeString(id_jobentry, "tablename"); //$NON-NLS-1$

            if(rep.getStepAttributeString(id_jobentry, "Sort") != null)
            	Sort = rep.getStepAttributeString(id_jobentry, "Sort"); //$NON-NLS-1$
            
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

        	rep.saveStepAttribute(id_job, getObjectId(), "tablename", tablename); //$NON-NLS-1$
        	
        	rep.saveStepAttribute(id_job, getObjectId(), "Sort", Sort); //$NON-NLS-1$
        	
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
