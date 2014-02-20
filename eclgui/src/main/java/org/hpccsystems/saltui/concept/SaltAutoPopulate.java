package org.hpccsystems.saltui.concept;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SaltAutoPopulate {

	public ConceptEntryList entryList = new ConceptEntryList();
	//public ConceptRuleList fieldTypeList = new ConceptRuleList();
	
	
	  public  String[] getRule(List<JobEntryCopy> jobs, String datasetName, String columnName) throws Exception{
	        //System.out.println(" ------------ parseDataSet ------------- ");
		    //find the node "Salt Hygiene"
		    //get the rule;
		  
	        String attributeName = "datasetName";
		  	String datasets[] = null;
	        ArrayList<String> adDS = new ArrayList<String>();
	        
	        
	        Object[] jec = jobs.toArray();

	        int k = 0;
	        
	        //loop through the spoon job objects
	        for(int j = 0; j<jec.length; j++){
	        	//if its a salt hygiene lets get the info
	        	//System.out.println(((JobEntryCopy)jec[j]).getName());
	        	//System.out.println("TYPE: " + ((JobEntryCopy)jec[j]).getTypeDesc());
	            if(((JobEntryCopy)jec[j]).getTypeDesc().equalsIgnoreCase("SALT Hygiene")){
	            	//System.out.println("this is a hygiene node for : " + datasetName);
	            	//System.out.println("TYPE: " + ((JobEntryCopy)jec[j]).getTypeDesc());
	               String xml = ((JobEntryCopy)jec[j]).getXML();
	                
	               //System.out.println(xml);
	               //we need to make sure this hygiene rule is for our dataset
	               
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
					                    System.out.println("iterate through nodes");
					                  // if (attribute!=null && datasetName.equals(attribute.getTextContent())){
					                	   //this should mean this is both a hygine node and references the required dataset
					                	   
					                    
					                       String nodeName = childnode.getNodeName();
					                       System.out.println("attribute: " + nodeName);
					                       defValue = XMLHandler.getNodeValue(childnode);
					                	  
					                       if(defValue != null && !defValue.equalsIgnoreCase("null")){
						                	   //need entryList
						                       if(nodeName.equalsIgnoreCase("entryList")){
						                    	   openEntryList(defValue);
						                       }  
						                       
						                       //		fieldTypeList
						                       if(nodeName.equalsIgnoreCase("fieldTypeList")){
						                    	   
						                    	   openFieldTypeList(defValue);
						                       }
					                       
					                       }
					                	 
					                	   /*
					                	   if(defValue != null && !defValue.equalsIgnoreCase("null")){
					                		   System.out.println("**NODE_VALUE: " + defValue);
					                		   adDS.add((String)defValue);
					                		   k++;
					                		   
					                	   }else{
					                		   System.out.println("**NODE_VALUE: IS NULL");
					                	   }
					                	   */
					                   //}
		                		   }
		                	   }
	                	   }catch (Exception exc){
	                		   System.out.println("Failed to Read XML");
	                		   System.out.println(exc);
	                		   //exc.printStackTrace();
	                	   }

	                   }
	               }
	            }//end if
	        }
	        //saving the loop code using arraylists
	        datasets = adDS.toArray(new String[k]);
	        //System.out.println(" ------------ end parseDataSet ------------- ");
	        return datasets;
	    }
	  
	  
	  public  void openFieldTypeList(String in){
	        String[] strLine = in.split("[|]");
	        int len = strLine.length;
	        /*
	        if(len>0){
	            fieldTypeList = new ConceptRuleList();
	            for(int i =0; i<len; i++){
	                ConceptRuleBO ft = new ConceptRuleBO(strLine[i]);
	                ft.fromCSV(strLine[i]);
	                fieldTypeList.add(ft);
	            }
	        }
	        */
	        //fieldTypes
	    }
	  
	  public  void openEntryList(String in){
	        String[] strLine = in.split("[|]");
	        int len = strLine.length;
	        if(len>0){
	            entryList = new ConceptEntryList();
	            for(int i =0; i<len; i++){
	                ConceptEntryBO eb = new ConceptEntryBO(strLine[i]);
	                entryList.addEntryBO(eb);
	            }
	        }
	        
	        //fieldTypes
	    }
}
