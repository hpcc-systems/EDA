/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.ecljobentrybase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
//import org.hpccsystems.javaecl.Output;
import org.hpccsystems.javaecl.EclDirect;
import org.pentaho.di.cluster.SlaveServer;
import org.pentaho.di.compatibility.Value;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Result;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.job.entry.JobEntryBase;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.w3c.dom.Node;
import org.hpccsystems.javaecl.Column;
import java.io.*;
import org.hpccsystems.javaecl.ECLSoap;
import org.pentaho.di.ui.spoon.Spoon;
import org.pentaho.di.core.*;
import org.pentaho.di.core.gui.SpoonFactory;


import org.hpccsystems.eclguifeatures.*;
import org.hpccsystems.javaecl.Column;
import org.hpccsystems.javaecl.ECLSoap;
import org.hpccsystems.javaecl.EclDirect;

import org.hpccsystems.recordlayout.RecordBO;
import org.hpccsystems.recordlayout.RecordList;
import org.pentaho.di.job.JobMeta;

/**
 *
 * @author Chambers,Joseph
 */
public class ECLJobEntry extends JobEntryBase implements Cloneable, JobEntryInterface  {
    
	
	
	@Override
    public Result execute(Result prevResult, int k) throws KettleException {
return null;
}


	protected static HashMap<String,Integer> timesExecutedMap = new HashMap<String,Integer>();
	protected static HashMap<String,Boolean> validMap = new HashMap<String,Boolean>();
	protected static HashMap<String,Result> globalResultMap = new HashMap<String,Result>();
	protected static HashMap<String,Boolean> isWaitingMap = new HashMap<String,Boolean>();
	//public static boolean isValid = false;

@Override
    public void loadXML(Node node, List<DatabaseMeta> list, List<SlaveServer> list1, Repository rpstr) throws KettleXMLException {
            super.loadXML(node, list, list1);
    }


    public String getXML() {
        String retval = "";
        
        retval += super.getXML();
      

        return retval;

    }

    public void loadRep(Repository rep, ObjectId id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
            throws KettleException {

    }

    public void saveRep(Repository rep, ObjectId id_job) throws KettleException {

    }
    public String resultListToString(RecordList recordList){
    	return ECLJobEntry.resultListToStringStatic(recordList);
    }
    public static String resultListToStringStatic(RecordList recordList){
        String out = "";
        
        if(recordList != null){
            if(recordList.getRecords() != null && recordList.getRecords().size() > 0) {
                    System.out.println("Size: "+recordList.getRecords().size());
                    for (Iterator<RecordBO> iterator = recordList.getRecords().iterator(); iterator.hasNext();) {
                            RecordBO record = (RecordBO) iterator.next();
                        	String rLen = record.getColumnWidth();
        					if (rLen != null && rLen.trim().length() >0) {
                                if(record.getColumnName() != null && !record.getColumnName().equals("")){
                                    out += record.getColumnType()+rLen + " " + record.getColumnName();
                                    if(record.getDefaultValue().equalsIgnoreCase("null")){
                                    	//added so we can set null values
                                        out += ":= ''";
                                    }else if(record.getDefaultValue() != ""){
                                    	//added check to make non numeric be quoted ''
                                    	String regex = "((-|\\+)?[0-9]+(\\.[0-9]+)?)+";
                                    	if(record.getDefaultValue().matches(regex)){
                                    		out += ":= " + record.getDefaultValue();
                                    	}else{
                                    		out += ":= '" + record.getDefaultValue() + "'";
                                        }
                                    }
                                    out += ";\r\n";
                                 }
                            }else{
                                if(record.getColumnName() != null && !record.getColumnName().equals("")){
                                    out += record.getColumnType() + " " + record.getColumnName();
                                    //if(record.getDefaultValue() != ""){
                                    //    out += ":= " + record.getDefaultValue();
                                    //}
                                    
                                    if(record.getDefaultValue().equalsIgnoreCase("null")){
                                    	//added so we can set null values
                                        out += ":= ''";
                                    }else if(record.getDefaultValue() != ""){
                                    	//added check to make non numeric be quoted ''
                                    	String regex = "((-|\\+)?[0-9]+(\\.[0-9]+)?)+";
                                    	if(record.getDefaultValue().matches(regex)){
                                    		out += ":= " + record.getDefaultValue();
                                    	}else{
                                    		out += ":= '" + record.getDefaultValue() + "'";
                                        }
                                    }
                                    
                                    out += ";\r\n";
                                }
                            }
                            
                            
                    }
            }
        }
        
        return out;
    }
    
    public String parseEclFromRowData(List<RowMetaAndData> list){
    	return parseEclFromRowData(list,false);
    }
    public String parseEclFromRowData(List<RowMetaAndData> list, boolean isExecute){
    	 String eclCode = "";
         if (list == null) {
             list = new ArrayList<RowMetaAndData>();
         } else {

         	for (int i = 0; i < list.size(); i++) {
             	try{
             		boolean hasECL = false;
             		boolean hasStatus = false;
 	                RowMetaAndData rowData = (RowMetaAndData) list.get(i);
 	                RowMeta rowMeta = (RowMeta) rowData.getRowMeta();
 	                String[] fields = rowMeta.getFieldNames();
 	                for(int cnt = 0; cnt<fields.length; cnt++){
 	                	if(fields[cnt].equals("ecl")){
 	                		hasECL = true;
 	                	}
 	                	if(fields[cnt].equals("ecl_used")){
 	                		hasStatus = true;
 	                	}
 	                }
 	                if(hasECL){
 	                	
 		                String code = rowData.getString("ecl", null);
 		                String status = "false";
 		                if(hasStatus && isExecute){
 		                	status = rowData.getString("ecl_used", null);
 		                }
 		                if (code != null && status != null && !status.equals("true")) {
 		                	//need to check that code doesn't exist in eclCode... instring should work
 		                    eclCode += code;
 		                }
 		                if(isExecute){
 		                	//list.get(i).addValue("ecl_used", Value.VALUE_TYPE_STRING, "true");
 		                }
 	                }
             	}catch (Exception e){
             		//ecl doesn't exist skip it
             		//I can't find a way to check rowData if it exists
             		e.printStackTrace();
             	}
             }
             logBasic("{-- Job} Execute Code =" + eclCode);
         }
         
         return eclCode;
    }
    
    public Result modifyResults(Result inResult){
    	 String isMergePath = System.getProperty("multiPath");
         
         if(isMergePath != null && isMergePath.equals("true")){
        	 inResult = new Result();
         	 inResult.setResult(true);
         	 System.setProperty("multiPath", "false");
         }
        if(inResult != null){ 
        	inResult = waitForAllPaths(inResult);
        }
    	return inResult;
    }
    public void blankTrackingMaps(){
    	 System.out.println("----blanking tracking maps----");
    	 if(timesExecutedMap!=null){
    		 timesExecutedMap.remove(this.getName());
    	 }
    	 if(globalResultMap != null){
    		 globalResultMap.remove(this.getName());
    	 }
    	 if(isWaitingMap != null){
    		 isWaitingMap.remove(this.getName());
    	 }
    }
    public Result waitForAllPaths(Result result){
    	//TODO::: needs to handle unconditional outbound path even if that means throwing an error.
    	Result newResult = new Result();
    	if(result != null){
	    	
	    	JobMeta jobMeta = super.parentJob.getJobMeta();
	      	List<JobEntryCopy> jobs = jobMeta.getJobCopies();
	      	int numInbound =0;
	      	try{
		      	if(jobMeta != null){
		      		numInbound = jobMeta.findNrPrevJobEntries(jobMeta.findJobEntry(this.getName()));
		      	}
	      	}catch(Exception e){
	      		System.out.println(e);
	      	}
	      	
	      	if(numInbound>1){
		      	//numTimesExecuted++;
		      	if(ECLJobEntry.timesExecutedMap.containsKey(this.getName())){
		      		//timesExecutedMap.put(key, value) timesExecutedMap.get(this.getName())
		      		ECLJobEntry.timesExecutedMap.put(this.getName(), ECLJobEntry.timesExecutedMap.get(this.getName())+1);
		      	}else{
		      		ECLJobEntry.timesExecutedMap.put(this.getName(), 1);
		      	}
		      	
		      	//add is waiting to the map for tracking purposes.
		      	if(!ECLJobEntry.isWaitingMap.containsKey(this.getName())){
		      		//timesExecutedMap.put(key, value) timesExecutedMap.get(this.getName())
		      		ECLJobEntry.isWaitingMap.put(this.getName(), true);
		      	}
		      	
		      	System.out.println("{ECLJOBENTRY} " + this.getName() + " - numInbound: " + numInbound);
		      	System.out.println("{ECLJOBENTRY} " + this.getName() + " - TimesExecuted: " + ECLJobEntry.timesExecutedMap.get(this.getName()));
		      	
	      	
	      		RowMetaAndData data = new RowMetaAndData();
	            List list = result.getRows();
	            
	            if(!ECLJobEntry.globalResultMap.containsKey(this.getName())){
	            	ECLJobEntry.globalResultMap.put(this.getName(), new Result());
	            }
	            List listGlobal = ECLJobEntry.globalResultMap.get(this.getName()).getRows();
	            listGlobal.addAll(list);
	            result.setRows(listGlobal);
	            
		      	int numberOfPathsRemaining = numInbound-ECLJobEntry.timesExecutedMap.get(this.getName());
		      	System.out.println("{ECLJOBENTRY} " + this.getName() + " - numberOfPathsRemaining: " + numberOfPathsRemaining);
		        if(numberOfPathsRemaining != 0){
		        	result.setStopped(true);
		        	System.out.println("~waiting");
		        	ECLJobEntry.validMap.put(this.getName(), false);
		        	//globalResult = result;
		        	ECLJobEntry.globalResultMap.put(this.getName(),result);
		            int count = 0;
		            //while(ECLJobEntry.isWaitingMap.get(this.getName())){
		            while(count < 10000){
		                //add a delay for non primary path.
		                count++;
		            }
		            newResult = ECLJobEntry.globalResultMap.get(this.getName());
		            System.setProperty("multiPath", "true");
		        }else{
		        	//isValid = true;
		        	result.setStopped(false);
		            result.setResult(true);
		            ECLJobEntry.globalResultMap.put(this.getName(),result);
		            
		            ECLJobEntry.validMap.put(this.getName(), true);
		        	System.out.println("~notwaiting");
		            result.setStopped(false);
		            System.setProperty("multiPath", "false");
		            newResult = ECLJobEntry.globalResultMap.get(this.getName());
		            ECLJobEntry.isWaitingMap.put(this.getName(), false);
		            blankTrackingMaps();
		        }
		       
		        
		        
		    }else{
		    	newResult = result;
		    }
    	}
        return newResult;
    }
    
    public boolean evaluates() {
        //return isValid;
    	boolean isValid = true;
    	if(ECLJobEntry.validMap.containsKey(this.getName())){
    		isValid = ECLJobEntry.validMap.get(this.getName());
    	}
    	return isValid;
    }
    
    public boolean isUnconditional()
    {
      return false;
    }
   
    
}
