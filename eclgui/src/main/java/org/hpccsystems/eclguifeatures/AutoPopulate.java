/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.eclguifeatures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.pentaho.di.core.encryption.Encr;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.regex.*;

import org.hpccsystems.recordlayout.*;


/**
 *
 * @author ChambeJX
 */
public class AutoPopulate {
    
    private String[] dataSets;
    private int nodeIndex;
    private String[] recordSets;
    private Node datasetNode = null;//used internally to store the node in the load record list functions
    
    private RecordList datasetFields = new RecordList();

    public void testExpression(List<JobEntryCopy> jobs){
    	
    	//to test call
    	//ap.testExpression(this.jobMeta.getJobCopies());
    	try{
    		HashMap ds = parseDefExpressionBuilder(jobs);
    		Iterator it = ds.entrySet().iterator();
    		while(it.hasNext()){
    			Map.Entry m = (Map.Entry)it.next();
    			//System.out.println("DataSet: " + m.getKey());
    			String[] fields = (String[])m.getValue();
    			
    			for(int i=0; i<fields.length; i++ ){
    				//System.out.println("---- " + fields[i]);
    			}
    		}
    		
    	}catch (Exception e){
    		System.out.println("Failed Expression Test");
    		System.out.println(e);
 		    e.printStackTrace();
    	}
    }
    
    public HashMap parseDefExpressionBuilder(List<JobEntryCopy> jobs) throws Exception{
    	return parseDefExpressionBuilder(jobs,"");
    }
    public HashMap parseDefExpressionBuilder(List<JobEntryCopy> jobs, String datasetName) throws Exception{

        HashMap ds = new HashMap();
        String attributeName = "eclIsDef";
        String attributeValue = "true";
        
        Object[] jec = jobs.toArray();
        int k = 0;
        for(int j = 0; j<jec.length; j++){
            if(!((JobEntryCopy)jec[j]).getName().equalsIgnoreCase("START") && !((JobEntryCopy)jec[j]).getName().equalsIgnoreCase("OUTPUT") && !((JobEntryCopy)jec[j]).getName().equalsIgnoreCase("SUCCESS")){
            	
            	
	               //System.out.println("----Loading Dataset: " + ((JobEntryCopy)jec[j]).getName());
            	   String xml = ((JobEntryCopy)jec[j]).getXML();
	               NodeList nl = (XMLHandler.loadXMLString(xml)).getChildNodes(); 
	               for (int temp = 0; temp < nl.getLength(); temp++){
	                   Node nNode = nl.item(temp);
	                   
	                   
	                   NodeList children;
	                   Node childnode;
	                   String defValue = null;
	                   String type = null;
	                  
	                   children=nNode.getChildNodes();
	                   
	                   for (int i=0;i<children.getLength();i++)
	                   {
	                	   try{
		                	  
		                	   childnode=children.item(i);
		                	   if(childnode != null){
		                		   if(childnode.getAttributes() != null){
					                   Node attribute = childnode.getAttributes().getNamedItem(attributeName);
					                   if (attribute!=null && attributeValue.equals(attribute.getTextContent())){
					                	   
					                	   defValue = XMLHandler.getNodeValue(childnode);
					                	  
					                	   if(defValue != null && !defValue.equalsIgnoreCase("null")){
					                		  
					                		   if(datasetName.equals("") || defValue.equalsIgnoreCase(datasetName)){
					                			   //System.out.println("NODE_VALUE: " + defValue);
					                			   ds.put(defValue, fieldsByDataset(defValue, jobs));
					                		   }
					                		   k++;
					                	   }else{
					                		   //System.out.println("NODE_VALUE: IS NULL");
					                	   }
					                   }
		                		   }
		                	   }
	                	   }catch (Exception exc){
	                		   System.out.println("Failed to Read XML");
	                		   //System.out.println(exc);
	                		   //exc.printStackTrace();
	                	   }
	
	                   }
	               }
            	
            }
        }
        return ds;
    }
    
    
    public String[] parseDefinitions(List<JobEntryCopy> jobs, String attributeName, String attributeValue) throws Exception{
        //System.out.println(" ------------ parseDataSet ------------- ");
        String datasets[] = null;
        ArrayList<String> adDS = new ArrayList<String>();
      
        
        Object[] jec = jobs.toArray();

        int k = 0;

        for(int j = 0; j<jec.length; j++){
            //System.out.println("Node(i): " + j + " | " +((JobEntryCopy)jec[j]).getName());

            if(!((JobEntryCopy)jec[j]).getName().equalsIgnoreCase("START") && !((JobEntryCopy)jec[j]).getName().equalsIgnoreCase("OUTPUT") && !((JobEntryCopy)jec[j]).getName().equalsIgnoreCase("SUCCESS")){
                //System.out.println("Node(k): " + k);
                
                //adDS.add((String)((JobEntryCopy)jec[j]).getName());
                String xml = ((JobEntryCopy)jec[j]).getXML();
                //System.out.println(xml);
                
               NodeList nl = (XMLHandler.loadXMLString(xml)).getChildNodes(); 
               for (int temp = 0; temp < nl.getLength(); temp++){
                   Node nNode = nl.item(temp);
                   
                   
                   NodeList children;
                   Node childnode;
                   String defValue = null;
                   String type = null;
                  
                   children=nNode.getChildNodes();
                   
                   for (int i=0;i<children.getLength();i++)
                   {
                	   try{
	                	  
	                	   childnode=children.item(i);
	                	   if(childnode != null){
	                		   if(childnode.getAttributes() != null){
				                   Node attribute = childnode.getAttributes().getNamedItem(attributeName);
				                   if (attribute!=null && attributeValue.equals(attribute.getTextContent())){
				                	   
				                	   defValue = XMLHandler.getNodeValue(childnode);
				                	  
				                	   if(defValue != null && !defValue.equalsIgnoreCase("null")){
				                		   //System.out.println("NODE_VALUE: " + defValue);
				                		   adDS.add((String)defValue);
				                		   k++;
				                		   
				                	   }else{
				                		   //System.out.println("NODE_VALUE: IS NULL");
				                	   }
				                   }
	                		   }
	                	   }
                	   }catch (Exception exc){
                		   System.out.println("Failed to Read XML");
                		   //System.out.println(exc);
                		   //exc.printStackTrace();
                	   }

                   }
               }
            }
        }
        //saving the loop code using arraylists
        datasets = adDS.toArray(new String[k]);
        return datasets;
    }
    
    public String[] parseAllDefinitions(List<JobEntryCopy> jobs) throws Exception{
        //String datasets1[] =  parseDefinitions(jobs,"eclIsDef","true");
        //String datasets2[] =  parseDefinitions(jobs,"eclIsGraphable","true");
        
        //return merge(datasets1,datasets2);
    	return parseDefinitions(jobs,"eclIsDef","true");
    }
    
    public String[] parseGraphableDefinitions(List<JobEntryCopy> jobs) throws Exception{
    	return parseDefinitions(jobs,"eclIsGraphable","true");
    }
    
    public String[] parseUnivariate(List<JobEntryCopy> jobs) throws Exception{
    	return parseDefinitions(jobs,"eclIsUniv","true");
    }
    
    public String[] parseDatasetsRecordsets(List<JobEntryCopy> jobs) throws Exception{
    	String datasets1[] = parseDefinitions(jobs, "eclType", "dataset");
    	String datasets2[] = parseDefinitions(jobs, "eclType", "recordset");
    	
    	return merge(datasets1,datasets2);
    	
    	
    }
    public String[] parseDatasets(List<JobEntryCopy> jobs) throws Exception{
    	return parseDefinitions(jobs, "eclType", "dataset");
    }
    
    public String[] parseRecordsets(List<JobEntryCopy> jobs) throws Exception{
    	return parseDefinitions(jobs, "eclType", "recordset");
    	
    }
    public String[] parseRecords(List<JobEntryCopy> jobs) throws Exception{
    	return parseDefinitions(jobs, "eclType", "record");
    	
    }
    
    
    public String[] merge(String[] x, String[] y) {

        String[] merged = new String[x.length + y.length];
        int m = 0;
        for(int i = 0; i<x.length;i++) {
            merged[m] = (String)x[i];
            m++;
        }
        for(int i = 0; i<y.length;i++) {
            merged[m] = (String)y[i];
            m++;
        }
        return merged;
    }
        		
        		
   
    
    /*
     * Gets the Dataset fields from all existing nodes
     * this function uses fieldsByDatasetList to build a List<String>
     * then it converts that to a String[] for use in the dialog classes
     * 
     */
    public String[] fieldsByDataset(String datasetName,List<JobEntryCopy> jobs)throws Exception{
        //System.out.println("***fieldsByDataset***");
        String datasets[] = new String[1];
        ArrayList<String> adDS = new ArrayList<String>();
        this.fieldsByDatasetList(adDS, datasetName,jobs);
        datasets = adDS.toArray(new String[adDS.size()]);
        return datasets;
    }
    
   
    
    public String getType(List<JobEntryCopy> jobs, String datasetValue) throws KettleXMLException{
    	String type = "";
    	String attributeName = "eclIsDef";
    	String attributeValue = "true";
    			
    	//find the dataset declaration from the value in the select
    	
    	
    	 //System.out.println(" ------------ parseDataSet ------------- ");
        String datasets[] = null;
        ArrayList<String> adDS = new ArrayList<String>();
      
        
        Object[] jec = jobs.toArray();

        int k = 0;

        for(int j = 0; j<jec.length; j++){
           // System.out.println("Node(i): " + j + " | " +((JobEntryCopy)jec[j]).getName());

            if(!((JobEntryCopy)jec[j]).getName().equalsIgnoreCase("START") && !((JobEntryCopy)jec[j]).getName().equalsIgnoreCase("OUTPUT") && !((JobEntryCopy)jec[j]).getName().equalsIgnoreCase("SUCCESS")){
                System.out.println("Node(k): " + k);
                
                adDS.add((String)((JobEntryCopy)jec[j]).getName());
                String xml = ((JobEntryCopy)jec[j]).getXML();
                //System.out.println(xml);
                
               NodeList nl = (XMLHandler.loadXMLString(xml)).getChildNodes(); 
               for (int temp = 0; temp < nl.getLength(); temp++){
                   Node nNode = nl.item(temp);
                   
                   
                   NodeList children;
                   Node childnode;
                   String defValue = null;
                  
                  
                   children=nNode.getChildNodes();
                   String tType = XMLHandler.getNodeValue(
                           XMLHandler.getSubNode(nNode, "type")
                           );
                   for (int i=0;i<children.getLength();i++)
                   {
                	   
                	   try{
	                	   childnode=children.item(i);
	                	   
	                	   if(childnode != null){
	                		   if(childnode.getAttributes() != null){
	                			   
	                			 //need to make sure its a def
		                		   //eclIsDef","true"
				                   Node attribute = childnode.getAttributes().getNamedItem(attributeName);
				                   if (attribute!=null && attributeValue.equals(attribute.getTextContent())){
				                	   //System.out.println(childnode.getNodeName());
				                	   defValue = XMLHandler.getNodeValue(childnode);
				                	  
				                	   if(defValue != null){
				                		   //System.out.println("---------------NODE_VALUE: " + defValue);

				                		   
				                		   
				                		   if(defValue.equals(datasetValue)){
				                			   //System.out.println("Verify that " + defValue + " = " + datasetValue);
				                			   //System.out.println("-------------Yes----------" + tType);
				                    		   type = tType;
				                    		   this.datasetNode = nNode;
				                    		   nodeIndex = i;
				                    		   k++;
				                    		   //to save execution time lets exit on the first find as it is most likely to be the one we want
				                    		   
				                    		  //System.out.println("~~~~~~~~~~~~~type: " + type);
				                    		   return type;
				                    	   }
				                		   
				                	   }else{
				                		   System.out.println("NODE_VALUE: IS NULL");
				                	   }
				                   }
	                		   }
	                	   }
                	   }catch (Exception exc){
                		   System.out.println("Failed to Read XML");
                		   //System.out.println(exc);
                		   //exc.printStackTrace();
                	   }

                   }
                   
               }
            }
        }
        //saving the loop code using arraylists
        
        
        
    	return type;
    }
    
    public void buildRecordList(String in){
        String[] strLine = in.split("[|]");
        int len = strLine.length;
        if(len>0){
            datasetFields = new RecordList();
            //System.out.println("Open Record List");
            for(int i =0; i<len; i++){
                RecordBO rb = new RecordBO(strLine[i]);
                datasetFields.addRecordBO(rb);
            }
        }
    }
    
    public void getRecordListFromECLDataset(Node node, ArrayList<String> adDS, String datasetName,List<JobEntryCopy> jobs) throws KettleXMLException{
    	//ok we need to loop through fields and identify the one that is recordList and populate adDS
    	String columns = XMLHandler.getNodeValue(
                XMLHandler.getSubNode(node, "recordList"));
    	String[] colArr = columns.split("[|]");
    	 int len = colArr.length;
    	 if(len>0){
    		 buildRecordList(columns);
    		 //System.out.println("Len: " + len);
             for(int i =0; i<len; i++){
            	 //get just the name
            	 String[] fieldArr = colArr[i].split("[,]");
            	 if(fieldArr.length>1){
            		 //System.out.println(fieldArr[0]);
            		 adDS.add(fieldArr[0]);
            	 }
            	 
             }
    	 }
    }

    private void fieldsByParent(String thisType, String parentField, ArrayList<String> adDS, String datasetName,List<JobEntryCopy> jobs) throws Exception{
    	//look up entry and then look up parent and call fieldsByDatasetList
    	
    	String parentName = XMLHandler.getNodeValue(
                XMLHandler.getSubNode(datasetNode, parentField));
    	//String test = XMLHandler.getNodeValue(
        //        XMLHandler.getSubNode(datasetNode, "left_recordset"));
    	//System.out.println("ParentName ("+parentField+"): " + parentName);
    	fieldsByDatasetList(adDS,parentName,jobs);
    	
    }
    
    /*
     * Gets the Dataset fields from all existing nodes
     * This uses recursion to travel up from joins etc to the 
     * def of the datasets
     */
    public void fieldsByDatasetList(ArrayList<String> adDS, String datasetName,List<JobEntryCopy> jobs)throws Exception{
        //System.out.println(" ------------ fieldsByDatasetList ------------ ");
        Object[] jec = jobs.toArray();
        Node node = null;
        
        String type = getType(jobs, datasetName);
        //datasetNode is set in getType
        node = datasetNode;
        //System.out.println("Type for "+ datasetName +": " + type);
        //place any outliers above the else will catch all that has a single parent with filed name recordset
        if(type != null && type.equalsIgnoreCase("ECLCount")){ 
        	fieldsByParent("ECLCount","recordset", adDS,datasetName,jobs);
        }else if(type != null && type.equalsIgnoreCase("ECLJoin")){ 
            //need to pull from both parents
        	//left_recordset
        	//right_recordset
        	
        	//copy node so we have it for the next
        	Node tnode = datasetNode.cloneNode(true);
        	fieldsByParent("ECLJoin","left_recordset", adDS,datasetName,jobs);
        	
        	//copy node back to datasetNode so we can continue
        	datasetNode = tnode.cloneNode(true);
        	fieldsByParent("ECLJoin","right_recordset", adDS,datasetName,jobs);
	
        }else if(type != null && type.equalsIgnoreCase("ECLSort")){
        	fieldsByParent("ECLSort","dataset_name", adDS,datasetName,jobs);
        }else if(type != null && type.equalsIgnoreCase("ECLProject")){
        	fieldsByParent("ECLProject","inRecordName", adDS,datasetName,jobs);
        }else if(type != null && type.equalsIgnoreCase("ECLDistribute")){
        	fieldsByParent("ECLDistribute","dataset_name", adDS,datasetName,jobs);
        }else if(type != null && type.equalsIgnoreCase("ECLMerge")){
        	fieldsByParent("ECLMerge","recordsetSet", adDS,datasetName,jobs);
        }else if(type != null && type.equalsIgnoreCase("ECLFilter")){
        	fieldsByParent("ECLFilter","inRecordName", adDS,datasetName,jobs);	
        //need to add in statments for all the ML funtions
        }else if(type != null && type.equalsIgnoreCase("ECLML_FromField")){
        	
        	if(node != null){
        		//System.out.println("We have fromField");
        		String parentNodeName = getRecordListFromField(node, adDS, datasetName,jobs);
        		fieldsByDatasetList(adDS,parentNodeName,jobs);
        	}
        	fieldsByParent("ECLMerge","recordsetSet", adDS,datasetName,jobs);	
        	
        }else if(type != null && type.equalsIgnoreCase("ECLDataset")){
        	//System.out.println("-------------GETTING RECORD LIST---------------");
        	//System.out.println("Type: " + type);
        	if(node != null){
        		//System.out.println("We have Node");
        		getRecordListFromECLDataset(node, adDS, datasetName,jobs);
        	}
        }else if(type != null && !type.equals("")){
        	fieldsByParent(type,"recordset", adDS,datasetName,jobs);
        	//add additional types here
        }
       
    }
    
    public String getRecordListFromField(Node node, ArrayList<String> adDS, String datasetName,List<JobEntryCopy> jobs) throws KettleXMLException{
    	//use Layout Record -- fromType
    	//find where fromType isDef and then get the record def
    	String parentNodeName = "";
    	String attributeName = "eclType";
    	String attributeValue = "record";
    	
    	String recordName = XMLHandler.getNodeValue(
                XMLHandler.getSubNode(node, "fromType"));
    	//use recordName to find parent
    	Node parentNode = null;
    	
    	Object[] jec = jobs.toArray();

        int k = 0;

        for(int j = 0; j<jec.length; j++){
            //System.out.println("Node(i): " + j + " | " +((JobEntryCopy)jec[j]).getName());

            if(!((JobEntryCopy)jec[j]).getName().equalsIgnoreCase("START") && !((JobEntryCopy)jec[j]).getName().equalsIgnoreCase("OUTPUT") && !((JobEntryCopy)jec[j]).getName().equalsIgnoreCase("SUCCESS")){
               String xml = ((JobEntryCopy)jec[j]).getXML();
                
               NodeList nl = (XMLHandler.loadXMLString(xml)).getChildNodes(); 
               for (int temp = 0; temp < nl.getLength(); temp++){
                   Node nNode = nl.item(temp);

                   NodeList children;
                   Node childnode;
                   String defValue = null;

                   children=nNode.getChildNodes();
                   
                   for (int i=0;i<children.getLength();i++)
                   {
                	   
                	   try{
	                	   childnode=children.item(i);
	                	   
	                	   if(childnode != null){
	                		   if(childnode.getAttributes() != null){
	                			   
	                			 //need to make sure its a def
		                		   //eclIsDef","true"
				                   Node attribute = childnode.getAttributes().getNamedItem(attributeName);
				                   if (attribute!=null && attributeValue.equals(attribute.getTextContent())){
				                	   //System.out.println(childnode.getNodeName());
				                	   defValue = XMLHandler.getNodeValue(childnode);
				                	  
				                	   if(defValue != null){
				                		   if(defValue.equals(recordName)){
				                    		   k++;
				                    		   parentNode = nNode;
				                    	   }
				                		   
				                	   }else{
				                		   //System.out.println("NODE_VALUE: IS NULL");
				                	   }
				                   }
	                		   }
	                	   }
                	   }catch (Exception exc){
                		   System.out.println("Failed to Read XML");
                		   //System.out.println(exc);
                		   //exc.printStackTrace();
                	   }

                   }//for
                   //ok check to see if we have node
                   if(parentNode != null){
                	   //we have parent node now get name
                	   String pAttributeName = "eclType";
                   	   String pAttributeValue1 = "recordset";
                   	   String pAttributeValue2 = "dataset";
                   	   
                	   children=parentNode.getChildNodes();
                       
                       for (int i=0;i<children.getLength();i++)
                       {
                    	   
                    	   try{
    	                	   childnode=children.item(i);
    	                	   
    	                	   if(childnode != null){
    	                		   if(childnode.getAttributes() != null){
    	                			   
    	                			 //need to make sure its a def
    		                		   //eclIsDef","true"
    				                   Node attribute = childnode.getAttributes().getNamedItem(pAttributeName);
    				                   if (attribute!=null && (pAttributeValue1.equals(attribute.getTextContent()) || pAttributeValue2.equals(attribute.getTextContent()))){
    				                	   //System.out.println(childnode.getNodeName());
    				                	   defValue = XMLHandler.getNodeValue(childnode);
    				                	  
    				                	   if(defValue != null){
    				                    		   k++; 
    				                    		   //parent node name found;
    				                    		   parentNodeName = defValue;
    				                    		   return parentNodeName;
    				                	   }else{
    				                		   //System.out.println("NODE_VALUE: IS NULL");
    				                	   }
    				                   }
    	                		   }
    	                	   }
                    	   }catch (Exception exc){
                    		   System.out.println("Failed to Read XML");
                    		   //System.out.println(exc);
                    		   //exc.printStackTrace();
                    	   }

                       }//for
                	   
                   }
                   
                   
               }
               
               
               
               
               
            }
        }
        
        return parentNodeName;
    }
    
     /*
     * Gets the Dataset fields from all existing nodes
     * This uses recursion to travel up from joins etc to the 
     * def of the datasets
     */
    public void fieldsByDatasetList2(ArrayList<String> adDS, String datasetName,List<JobEntryCopy> jobs)throws Exception{
       // System.out.println(" ------------ fieldsByDatasetList ------------ ");
        Object[] jec = jobs.toArray();

        int k = 0;

        if(jec != null){


            for(int j = 0; j<jec.length; j++){
                //System.out.println("Node(i): " + j + " | " +((JobEntryCopy)jec[j]).getName());

                if(!((JobEntryCopy)jec[j]).getName().equalsIgnoreCase("START") && !((JobEntryCopy)jec[j]).getName().equalsIgnoreCase("OUTPUT") && !((JobEntryCopy)jec[j]).getName().equalsIgnoreCase("SUCCESS")){
                    //System.out.println("Node(k): " + k);

                    //adDS.add((String)((JobEntryCopy)jec[j]).getName());
                    String xml = ((JobEntryCopy)jec[j]).getXML();
                    //System.out.println(xml);

                   NodeList nl = (XMLHandler.loadXMLString(xml)).getChildNodes(); 
                   for (int temp = 0; temp < nl.getLength(); temp++){
                       Node nNode = nl.item(temp);
                       String name = XMLHandler.getNodeValue(
                           XMLHandler.getSubNode(nNode, "name")
                           );

                       String dataset = XMLHandler.getNodeValue(
                           XMLHandler.getSubNode(nNode, "dataset_name")
                           );

                       String type = XMLHandler.getNodeValue(
                           XMLHandler.getSubNode(nNode, "type")
                           );

                       String record = XMLHandler.getNodeValue(
                           XMLHandler.getSubNode(nNode, "record_name")
                           );
                       String recordset = XMLHandler.getNodeValue(
                           XMLHandler.getSubNode(nNode, "recordset_name")
                           );
                      /* System.out.println("XMLParse Compare Val: "+ datasetName + "~~~~~~~~~");
                       System.out.println("XMLParse Type: " + type);
                       System.out.println("XMLParse Recordset: " + recordset);
                       System.out.println("XMLParse record: " + record);
                       System.out.println("XMLParse Dataset: " + dataset);
                       System.out.println("XMLParse name: " + name);*/
                       if(type.equals("ECLDataset") && 
                               ((dataset!=null && dataset.equals(datasetName)) || 
                               (recordset != null && recordset.equals(datasetName)) || (record != null && record.equals(datasetName)))){
                            //System.out.println("---------------------ECLDataset----");
                            String def = XMLHandler.getNodeValue(
                           XMLHandler.getSubNode(nNode, "record_def")
                           );
                            if(def != null && !def.equals("")){
                                this.chunkFormat(def,adDS);

                            }



                       }else if(type.equals("ECLJoin") && 
                               (
                                (dataset != null && dataset.equals(datasetName)) || 
                                (recordset != null && recordset.equals(datasetName)) || 
                                (record != null && record.equals(datasetName))
                               )
                               ){
                           //if it is a sort
                           //well we need to look at the parents so we call this funtion recursivelly
                           //leftRecordSet
                            //System.out.println("------------------------ECLJoin----");

                           String leftRS = XMLHandler.getNodeValue(
                           XMLHandler.getSubNode(nNode, "left_recordset") );
                           String rightRS = XMLHandler.getNodeValue(
                           XMLHandler.getSubNode(nNode, "right_recordset") );


                           this.fieldsByDatasetList(adDS, leftRS, jobs);
                           this.fieldsByDatasetList(adDS, rightRS, jobs);



                       }else if(type.equals("ECLProject") && 
                               ((dataset!=null && dataset.equals(datasetName)) || 
                               (recordset != null && recordset.equals(datasetName)) || (record != null && record.equals(datasetName)))){
                           // System.out.println("---------------------ECLProject----");

                           String def = XMLHandler.getNodeValue(
                           XMLHandler.getSubNode(nNode, "outRecordFormat")
                           );
                            if(def != null && !def.equals("")){
                                this.chunkFormat(def,adDS);

                            }
                           //outRecordFormat
                       }
                       //will need to add logic for each type




                       //dataset_name
                       //name
                       //type
                       //record_name
                   }

                }

                //System.out.println(((JobEntryCopy)jec[j]).getXML());

            }
        }
        System.out.println("finished parsing");
    }
    
    private void chunkFormat(String def, List<String> adDS){
         if(def != null && !def.equals("")){
           // System.out.println("has def");
            String[] pieces = def.split("\n");
            
            for (int p = pieces.length - 1; p >= 0; p--) {
             //  System.out.println("" + (String)pieces[p].trim());
               
               Pattern pattern = Pattern.compile("[ ;]+",Pattern.CASE_INSENSITIVE);
               
               String peice = (String)pieces[p].trim();
               
               String[] chunks = pattern.split(peice);
              // System.out.println("Chunks lenght " + chunks.length);
               if(chunks.length >= 2 && chunks[1]!=null){
                    adDS.add(chunks[1]);
               }
            }

        }
    }
    
    private String[] mergeArray(String[] a1, String[] a2){
        //System.out.println(" ------------ mergeArray ------------- ");
        
        String[] out = new String[a1.length+a2.length];
      
        System.arraycopy(a1, 0, out, 0, a1.length);
        System.arraycopy(a1, 0, out, a1.length, a2.length);
        
        return out;
    }


    
    public String getGlobalVariableEncrypted(List<JobEntryCopy> jobs, String ofType) throws Exception{
    	String pass = getGlobalVariable(jobs,ofType);
    	if(pass == null || pass.equalsIgnoreCase("")){
			return "";
		}else{
			return Encr.decryptPassword(pass);
		}
    	
    }
    public String getGlobalVariable(List<JobEntryCopy> jobs, String ofType) throws Exception{
       // System.out.println(" ------------ getGlobalVariable ------------- ");
        String datasets[] = null;
        ArrayList<String> adDS = new ArrayList<String>();
        String out = "";
        
        Object[] jec = jobs.toArray();

        int k = 0;
        
        if(jec != null){
            

            for(int j = 0; j<jec.length; j++){
                //System.out.println("Node(i): " + j + " | " +((JobEntryCopy)jec[j]).getName());

                //if( ((JobEntryCopy)jec[j]).getName().equalsIgnoreCase("ECLGlobalVariables") ){
                    //System.out.println("Node(k): " + k);

                    //adDS.add((String)((JobEntryCopy)jec[j]).getName());
                    String xml = ((JobEntryCopy)jec[j]).getXML();
                    //System.out.println(xml);

                   NodeList nl = (XMLHandler.loadXMLString(xml)).getChildNodes(); 
                   for (int temp = 0; temp < nl.getLength(); temp++){
                       Node nNode = nl.item(temp);
                       
                       String type = XMLHandler.getNodeValue(
                           XMLHandler.getSubNode(nNode, "type")
                           );
                       //System.out.println("Type: " + type);
                       if(type.equalsIgnoreCase("ECLGlobalVariables")){
                           if(ofType.equalsIgnoreCase("server_ip")){
                                out = XMLHandler.getNodeValue(
                                    XMLHandler.getSubNode(nNode, "server_ip")
                                );
                           }
                           if(ofType.equalsIgnoreCase("server_port")){
                                out = XMLHandler.getNodeValue(
                                    XMLHandler.getSubNode(nNode, "server_port")
                                );
                           }
                           if(ofType.equalsIgnoreCase("landing_zone")){
                                out = XMLHandler.getNodeValue(
                                    XMLHandler.getSubNode(nNode, "landing_zone")
                                );
                           }
                          
                           if(ofType.equalsIgnoreCase("cluster")){
                                out = XMLHandler.getNodeValue(
                                    XMLHandler.getSubNode(nNode, "cluster")
                                );
                           }
                          
                           if(ofType.equalsIgnoreCase("jobName")){
                                out = XMLHandler.getNodeValue(
                                    XMLHandler.getSubNode(nNode, "jobName")
                                );
                           }
                          
                           if(ofType.equalsIgnoreCase("maxReturn")){
                              out = XMLHandler.getNodeValue(
                                  XMLHandler.getSubNode(nNode, "maxReturn")
                              );
                           }
                          
                           if(ofType.equalsIgnoreCase("eclccInstallDir")){
                                out = XMLHandler.getNodeValue(
                                    XMLHandler.getSubNode(nNode, "eclccInstallDir")
                                );
                           }
                          
                           if(ofType.equalsIgnoreCase("mlPath")){
                                out = XMLHandler.getNodeValue(
                                    XMLHandler.getSubNode(nNode, "mlPath")
                                );
                           }
                          
                           if(ofType.equalsIgnoreCase("includeML")){
                                out = XMLHandler.getNodeValue(
                                    XMLHandler.getSubNode(nNode, "includeML")
                                );
                           }
                           if(ofType.equalsIgnoreCase("user_name")){
                              out = XMLHandler.getNodeValue(
                                  XMLHandler.getSubNode(nNode, "user_name")
                              );
                           }
                          
                           if(ofType.equalsIgnoreCase("password")){
                              out = XMLHandler.getNodeValue(
                                  XMLHandler.getSubNode(nNode, "password")
                              );
                           }      

                           if(ofType.equalsIgnoreCase("SALTPath")){
                              out = XMLHandler.getNodeValue(
                                  XMLHandler.getSubNode(nNode, "SALTPath")
                              );
                           }
                        
                           if(ofType.equalsIgnoreCase("includeSALT")){
                              out = XMLHandler.getNodeValue(
                                  XMLHandler.getSubNode(nNode, "includeSALT")
                              );
                           }
                           if(ofType.equalsIgnoreCase("compileFlags")){
                        	   System.out.println("--fetch compile flags");
	                            out = XMLHandler.getNodeValue(
	                                XMLHandler.getSubNode(nNode, "compileFlags")
	                            );
                           }  
                                  

                       }else if(type.equalsIgnoreCase("ECLExecute")){
                    	   if(ofType.equalsIgnoreCase("file_name")){
                               out = XMLHandler.getNodeValue(
                                   XMLHandler.getSubNode(nNode, "file_name")
                               );
                          }
                       }
                      
                       
                   }

               // }


            }
            //saving the loop code using arraylists
            datasets = adDS.toArray(new String[k]);

        }
        if(out == null){
        	out = "";
        }
        return out;

    }
    
    
    /*
     * Gets the Dataset fields from all existing nodes
     * This uses recursion to travel up from joins etc to the 
     * def of the datasets
     */
    public String getDatasetsField(String fieldName, String datasetName,List<JobEntryCopy> jobs)throws Exception{
       // System.out.println(" ------------ fieldsByDatasetList " + fieldName + " " + datasetName + " ------------ ");
        //Object[] jec = jobs.toArray();
        //Node node = null;
        //RecordList recordList = null;
        String type = getType(jobs, datasetName);
        //System.out.println("----------- Type: " + type);
        //datasetNode is set in getType
        //return datasetNode;
        //System.out.println(datasetNode.toString());
        String recordName = XMLHandler.getNodeValue(
                XMLHandler.getSubNode(datasetNode, fieldName));
       // System.out.println("END ------------ fieldsByDatasetList " + fieldName + " " + datasetName + " ------------ ");
        return recordName;
    }
    
    /*
     * Gets the Dataset fields from all existing nodes
     * This uses recursion to travel up from joins etc to the 
     * def of the datasets
     */
    public RecordList rawFieldsByDataset(String datasetName,List<JobEntryCopy> jobs)throws Exception{
        //System.out.println(" ------------ fieldsByDatasetList ------------ ");
        Object[] jec = jobs.toArray();
        Node node = null;
        RecordList recordList = null;
        String type = getType(jobs, datasetName);
        //datasetNode is set in getType
        node = datasetNode;
        if(type != null && type.equalsIgnoreCase("ECLDataset")){
        	//System.out.println("-------------GETTING RECORD LIST---------------");
        	System.out.println("Type: " + type);
        	if(node != null){
        		
        		recordList = fetchFieldsRecordListByDataset(node);
        	}
        }
        return recordList;
       
    }
    
    public RecordList fetchFieldsRecordListByDataset(Node node){
    	String columns = XMLHandler.getNodeValue(
                XMLHandler.getSubNode(node, "recordList"));
    	String[] strLine = columns.split("[|]");
    	RecordList recordList = null;
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
        
        return recordList;
    }
    
    
    
    public ArrayList<String> getLogicalFileName(List<JobEntryCopy> jobs) throws Exception{
    	
    	ArrayList<String> logicalFileNames = new ArrayList<String>();
         System.out.println(" ------------ getLogicalFileName ------------- ");
         String datasets[] = null;
         String fieldName = "logical_file_name";//spray
         
         Object[] jec = jobs.toArray();

         int k = 0;
         
         if(jec != null){
             

             for(int j = 0; j<jec.length; j++){
                    String xml = ((JobEntryCopy)jec[j]).getXML();
                    NodeList nl = (XMLHandler.loadXMLString(xml)).getChildNodes(); 
                    for (int temp = 0; temp < nl.getLength(); temp++){
                        Node nNode = nl.item(temp);
                        
                        String type = XMLHandler.getNodeValue(
                            XMLHandler.getSubNode(nNode, "type")
                            );
                        System.out.println("Type: " + type);
                        if(type.equalsIgnoreCase("ECLSprayFile")){
                            	System.out.println("Logical Lookup: ");
                                 logicalFileNames.add(XMLHandler.getNodeValue(
                                     XMLHandler.getSubNode(nNode, fieldName)
                                 ));
                            
                        }
                    }
             }

         }
         return logicalFileNames;

     }
    
    
    public boolean hasNodeofType(List<JobEntryCopy> jobs, String ofType) throws Exception{
        // System.out.println(" ------------ " + ofTYpe + " ------------- ");
    	 boolean isType = false;
         Object[] jec = jobs.toArray();
         if(jec != null){
            
             for(int j = 0; j<jec.length; j++){
                    String xml = ((JobEntryCopy)jec[j]).getXML();
                    NodeList nl = (XMLHandler.loadXMLString(xml)).getChildNodes(); 
                    for (int temp = 0; temp < nl.getLength(); temp++){
                        Node nNode = nl.item(temp);
                        
                        String type = XMLHandler.getNodeValue(
                            XMLHandler.getSubNode(nNode, "type")
                            );
                        //System.out.println("Type: " + type);
                        if(type.equalsIgnoreCase(ofType)){
                        	isType = true;
                            return isType;//no need to keep looping;
                        }
                    }
             }
         }
         return isType;

     }
    
    public ArrayList<String[]> compileFlagsToArrayList(String compileFlags){
    	//System.out.println("-- Testing custom Flag add in -- ");
    	ArrayList<String[]> flags = new ArrayList<String[]>();
    	
    	//break string on line break
    	String[] strLine = compileFlags.split("\r?\n");
    	//break key,value on first space
    	
    	for(int i =0; i< strLine.length; i++){
    		//System.out.println("--loop iteration " + i);
    		String str = strLine[i];
    		String[] pair = new String[2];
    		if(str.contains(" ")){
    			//System.out.println("---- Has Key:value pair");
	    		String key = str.substring(0,str.indexOf(' '));
	    		String val = str.substring(str.indexOf(' ')+1);
	    		pair[0] = key;
	    		pair[1] = val;
	    		//System.out.println("Key: " + key + " value: " + val);
    		}else{
    			//System.out.println("---- Has Key Only " + str);
	    		pair[0] = str;
	    		pair[1] = "";
    		}
    		
    		flags.add(pair);
    	}
    	
    	return flags;
    }
    
    public static void main(String[] args){
    	AutoPopulate ap = new AutoPopulate();
    	System.out.println("Single Line Test");
    	String compileFlags = "-I /home/ubuntu/DeepGlance";
    	ArrayList<String[]> compileFlagsAL = ap.compileFlagsToArrayList(compileFlags);
    	
    	for(int i = 0; i<compileFlagsAL.size(); i++){
    		if(compileFlagsAL.get(i).length == 2){
    			if(!compileFlagsAL.get(i)[0].equals("")){
    				System.out.println("Flag: " + compileFlagsAL.get(i)[0]);
    			}
    			if(!compileFlagsAL.get(i)[1].equals("")){
    				System.out.println("Value: " + compileFlagsAL.get(i)[1]);
    			}
    		}
    	}
    	System.out.println("_______________________");
    	System.out.println("Multiple Line Test");
    	compileFlags = "-I /home/ubuntu/DeepGlance\r\n-I /ho me/ubu ntu/DeepGlance\r\n-O\r\n";
    	compileFlagsAL = ap.compileFlagsToArrayList(compileFlags);
    	
    	for(int i = 0; i<compileFlagsAL.size(); i++){
    		if(compileFlagsAL.get(i).length == 2){
    			if(!compileFlagsAL.get(i)[0].equals("")){
    				System.out.println("Flag: " + compileFlagsAL.get(i)[0]);
    			}
    			if(!compileFlagsAL.get(i)[1].equals("")){
    				System.out.println("Value: " + compileFlagsAL.get(i)[1]);
    			}
    		}
    	}
    }
    
}
