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
 * @author KeshavS [:P]
 */
public class ECLFrequency extends ECLJobEntry{//extends JobEntryBase implements Cloneable, JobEntryInterface {
	
	private String Name = "";
	private String desc = "";
	private String DatasetName = "";
	private String normList = "";
	private String logicalName = "";
	private String sortlist = "";
	private String tablename = "";
	private RecordList recordList = new RecordList();
	
	public RecordList getRecordList() {
        return recordList;
    }

    public void setRecordList(RecordList recordList) {
        this.recordList = recordList;
    }
    
	public String getName(){
		return Name;
	}
    
	public void setName(String Name){
		this.Name = Name;
	}
	
	public String getdesc(){
		return desc;
	}
    
	public void setdesc(String desc){
		this.desc = desc;
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
    
	public String getlogicalName(){
		return logicalName;
	}
    
	public void setlogicalName(String logicalName){
		this.logicalName = logicalName;
	}
	
	public String getsortlist(){
		return sortlist;
	}
	
	public void setsortlist(String sortlist){
		this.sortlist = sortlist;
	}
	
	public String gettablename(){
		return tablename;
	}
	
	public void settablename(String tablename){
		this.tablename = tablename;
	}

	/*public String fieldsValid(RecordList recordList2) {
		// TODO Auto-generated method stub
		return null;
	}*/
    @Override
    public Result execute(Result prevResult, int k) throws KettleException {
    	
    	
        Result result = prevResult;
        if(result.isStopped()){
        	
        }
        else{
	        String format = ""; String frequency = "";String[] norm = this.normList.split(",");
	        
	        for(int i = 0; i < norm.length; i++){
	        	Table freq = new Table();
	        	logBasic(this.normList);
		        freq.setName(gettablename()+Integer.toString(i));
		        freq.setRecordset(this.DatasetName);
		        if(!norm[i].equals(""))
		        	freq.setExpression(norm[i]);
		        format = this.DatasetName+"."+norm[i]+";\nfre"+Integer.toString(i)+":= COUNT(GROUP);";
		        freq.setFormat(format);
		        format = new String();
		        if(this.getsortlist().equals("NO") || this.getsortlist().equals("")){
		        	freq.setSize("FEW");
		        	frequency += freq.ecl() + "OUTPUT("+gettablename()+Integer.toString(i)+");\n";
		        }
		        
		        if(this.getsortlist().equals("COLUMN")){
		        	if(this.getdesc().equals("YES")){
		        		freq.setSize("FEW");
		        		frequency += freq.ecl() +gettablename()+Integer.toString(i)+"s:="+"SORT("+gettablename()+Integer.toString(i)+ ",-"+norm[i]+");\n";
		        		frequency +="OUTPUT("+gettablename()+Integer.toString(i)+"s);\n";
		        	}
		        	else
		        		frequency += freq.ecl() + "OUTPUT("+gettablename()+Integer.toString(i)+");\n";
		        }
		        else{
		        	if(this.getsortlist().equals("VALUE")){
		        		if(this.getdesc().equals("YES")){
		        			freq.setSize("FEW");
		        			frequency += freq.ecl() +gettablename()+Integer.toString(i)+"s:="+ "SORT("+gettablename()+Integer.toString(i)+ ",-fre"+Integer.toString(i)+");\n";
		        			frequency +="OUTPUT("+gettablename()+Integer.toString(i)+"s);\n";
		        		}
		        		else{
		        			frequency += freq.ecl() +gettablename()+Integer.toString(i)+"s:="+ "SORT("+gettablename()+Integer.toString(i)+ ",fre"+Integer.toString(i)+");\n";
		        			frequency += "OUTPUT("+gettablename()+Integer.toString(i)+"s);\n";
		        		}
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
	        /*
	        String eclCode = "";
	        if (list == null) {
	            list = new ArrayList();
	        } else {
	            
	            for (int i = 0; i < list.size(); i++) {
	                RowMetaAndData rowData = (RowMetaAndData) list.get(i);
	                String code = rowData.getString("ecl", null);
	                if (code != null) {
	                    eclCode += code;
	                }
	            }
	            logBasic("{Frequency Job} ECL Code =" + eclCode);
	        }
	        */
	        result.setRows(list);
	        result.setLogText("ECLFrequency executed, ECL code added");
        }
        return result;
    }
    
    public String saveRecordList(){
        String out = "";
        ArrayList list = recordList.getRecords();
        Iterator<RecordBO> itr = list.iterator();
        boolean isFirst = true;
        while(itr.hasNext()){
            if(!isFirst){out+="|";}
            
            out += itr.next().toCSV();
            isFirst = false;
        }
        return out;
    }
    
    public void openRecordList(String in){
        String[] strLine = in.split("[|]");
        
        int len = strLine.length;
        if(len>0){
            recordList = new RecordList();
            //System.out.println("Open Record List");
            for(int i =0; i<len; i++){
                //System.out.println("++++++++++++" + strLine[i]);
                //this.recordDef.addRecord(new RecordBO(strLine[i]));
                RecordBO rb = new RecordBO(strLine[i]);
                //System.out.println(rb.getColumnName());
                recordList.addRecordBO(rb);
            }
        }
    }
 
    
    public RecordList ArrayListToRecordList(ArrayList<String[]> in){
    
    	RecordList recordList = null;
    	/*
    	 					column[0] = "";//label            THIS BLOCK WAS ALREADY COMMENTED OUT
                        	column[1] = "";//type
                        	column[2] = "";//value
                        	column[3] = "";//size
                        	column[4] = "";//maxsize
    	 */
        
       if(in.size()>0){
            recordList = new RecordList();
            for(int i =0; i<in.size(); i++){
                RecordBO rb = new RecordBO();
                rb.setColumnName(in.get(i)[0]);
                //rb.setColumnType(in.get(i)[1].replaceAll("\\d+",""));//replaces digit with "" so we get STRING/INTEGER etc
                //System.out.println("Letters: " + x.replaceAll("\\d+[_]*",""));
                rb.setColumnType(in.get(i)[1].replaceAll("\\d+[_]*",""));//replaces digit with "" so we get STRING/INTEGER etc
                
                //rb.setColumnWidth(in.get(i)[1].replaceAll("\\D+",""));//replace non digit with "" so we get just number 
                //System.out.println("Numbers: " + x.replaceAll("[^0-9_]+",""));
                rb.setColumnWidth(in.get(i)[1].replaceAll("[^0-9_]+",""));//replace non digit with "" so we get just number 
                
                rb.setDefaultValue(in.get(i)[2]);
                recordList.addRecordBO(rb);
            }
        }
        return recordList;
    }
    
    @Override
    public void loadXML(Node node, List<DatabaseMeta> list, List<SlaveServer> list1, Repository rpstr) throws KettleXMLException {
        try {
            super.loadXML(node, list, list1);
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "name")) != null)
                setDatasetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "name")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")) != null)
                setDatasetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "sortlist")) != null)
                setsortlist(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "sortlist")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "tablename")) != null)
                settablename(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "tablename")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "desc")) != null)
                setdesc(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "desc")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "normList")) != null)
                setnormList(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "normList")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "recordList")) != null)
                openRecordList(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "recordList")));
            
        } catch (Exception e) {
            throw new KettleXMLException("ECL Dataset Job Plugin Unable to read step info from XML node", e);
        }

    }

    public String getXML() {
        String retval = "";
        
        retval += super.getXML();
        retval += "		<name><![CDATA[" + Name + "]]></name>" + Const.CR;
        retval += "		<desc><![CDATA[" + desc + "]]></desc>" + Const.CR;
        retval += "		<sortlist><![CDATA[" + sortlist + "]]></sortlist>" + Const.CR;
        retval += "		<tablename><![CDATA[" + tablename + "]]></tablename>" + Const.CR;
        retval += "		<normList><![CDATA[" + this.getnormList() + "]]></normList>" + Const.CR;
        retval += "		<recordList><![CDATA[" + this.saveRecordList() + "]]></recordList>" + Const.CR;
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
            
            if(rep.getStepAttributeString(id_jobentry, "sortlist") != null)
            	sortlist = rep.getStepAttributeString(id_jobentry, "sortlist"); //$NON-NLS-1$
            
            if(rep.getStepAttributeString(id_jobentry, "tablename") != null)
            	tablename = rep.getStepAttributeString(id_jobentry, "tablename"); //$NON-NLS-1$
            
            if(rep.getStepAttributeString(id_jobentry, "desc") != null)
            	desc = rep.getStepAttributeString(id_jobentry, "desc"); //$NON-NLS-1$
            
            if(rep.getStepAttributeString(id_jobentry, "normList") != null)
                this.setnormList(rep.getStepAttributeString(id_jobentry, "normList")); //$NON-NLS-1$
            
            if(rep.getStepAttributeString(id_jobentry, "recordList") != null)
                this.openRecordList(rep.getStepAttributeString(id_jobentry, "recordList")); //$NON-NLS-1$

            
        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }

    public void saveRep(Repository rep, ObjectId id_job) throws KettleException {
        try {
        	rep.saveStepAttribute(id_job, getObjectId(), "datasetName", DatasetName); //$NON-NLS-1$

        	rep.saveStepAttribute(id_job, getObjectId(), "sortlist", sortlist); //$NON-NLS-1$
        	
        	rep.saveStepAttribute(id_job, getObjectId(), "tablename", tablename); //$NON-NLS-1$
        	
        	rep.saveStepAttribute(id_job, getObjectId(), "Name", Name); //$NON-NLS-1$
        	
        	rep.saveStepAttribute(id_job, getObjectId(), "desc", desc); //$NON-NLS-1$
        	
        	rep.saveStepAttribute(id_job, getObjectId(), "normList", this.getnormList()); //$NON-NLS-1$
        	
            rep.saveStepAttribute(id_job, getObjectId(), "recordList", this.saveRecordList()); //$NON-NLS-1$
            
            
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
