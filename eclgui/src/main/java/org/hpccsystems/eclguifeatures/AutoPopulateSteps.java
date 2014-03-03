/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.eclguifeatures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.pentaho.di.core.xml.XMLHandler;
//import org.pentaho.di.job.entry.StepMeta;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.regex.*;

import org.pentaho.di.trans.step.StepMeta;

/**
 *
 * @author ChambeJX
 */
public class AutoPopulateSteps {
    
    private String[] dataSets;
    private String[] recordSets;
    
    
public static Map<String, List<String>> getDefFields(List<StepMeta> jobs){
	
		//each tree parent will be a job node
		//each tree child will be a field as defined with isdef=true
		
		Map<String, List<String>> mapFunctions = new TreeMap<String, List<String>>();
		List<String> arlListMath = new ArrayList<String>();
		arlListMath.add("child 1");
		arlListMath.add("child 2");
		arlListMath.add("child 3");
		arlListMath.add("child 4");
		arlListMath.add("child 5");
		arlListMath.add("child 6");
		
		mapFunctions.put("Root Item 0a", arlListMath);
		
		List<String> arlListLogical = new ArrayList<String>();
		arlListLogical.add("child 1");
		arlListLogical.add("child 2");
		
		mapFunctions.put("Root Item 1a", arlListLogical);
		
		List<String> arlListString = new ArrayList<String>();
		arlListString.add("c1");
		arlListString.add("c2");
		
		mapFunctions.put("Root Item 2a", arlListString);
		
		
		
		return mapFunctions;
	}
    
    public String[] parseDatasetsRecordsets(List<StepMeta> jobs) throws Exception{
        System.out.println(" ------------ parseDatasetsRecordsets ------------- ");
        String datasets[] = null;
        ArrayList<String> adDS = new ArrayList<String>();
      
        
        Object[] jec = jobs.toArray();

        int k = 0;

        for(int j = 0; j<jec.length; j++){
            //System.out.println("Node(i): " + j + " | " +((StepMeta)jec[j]).getName());

            if(!((StepMeta)jec[j]).getName().equalsIgnoreCase("START") && !((StepMeta)jec[j]).getName().equalsIgnoreCase("OUTPUT") && !((StepMeta)jec[j]).getName().equalsIgnoreCase("SUCCESS")){
                //System.out.println("Node(k): " + k);
                
                //adDS.add((String)((StepMeta)jec[j]).getName());
                String xml = ((StepMeta)jec[j]).getXML();
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
                   /*
                   recordsetName
                   recordName
                   datasetName
                   */
                   String recordsetName = XMLHandler.getNodeValue(
                           XMLHandler.getSubNode(nNode, "recordsetName")
                           );
                   String recordName = XMLHandler.getNodeValue(
                           XMLHandler.getSubNode(nNode, "recordName")
                           );
                   String datasetName = XMLHandler.getNodeValue(
                           XMLHandler.getSubNode(nNode, "datasetName")
                           );
                   
                   //outDS
                   String outDS = XMLHandler.getNodeValue(
                           XMLHandler.getSubNode(nNode, "outDS")
                           );
                   System.out.println("XMLParse Type: " + type);
                   if(!type.equals("ECLMergePaths") && !type.equals("SPECIAL") && !type.equals("SUCCESS")){
                       // System.out.println("XML Parse Value: " + name);
                       // System.out.println("XML Parse Value: " + dataset);
                       // System.out.println("XML Parse Value: " + record);
                       // System.out.println("XML Parse Value: " + type);
                       // System.out.println("--");
                        //if(dataset != null)
                            //adDS.add((String)name);
                        if(dataset != null){
                            adDS.add((String)dataset);
                            k++;
                        }
                        if(record != null){
                            adDS.add((String)record);
                            k++;
                        }
                        
                        /*
                        recordsetName
                        recordName
                        datasetName
                        */
                        if(recordsetName != null){
                            adDS.add((String)recordsetName);
                            k++;
                        }
                        if(recordName != null){
                            adDS.add((String)recordName);
                            k++;
                        }
                        if(datasetName != null){
                            adDS.add((String)datasetName);
                            k++;
                        }
                        
                      //outDS
                        if(outDS != null){
                            adDS.add((String)outDS);
                            k++;
                        }
                        
                   }
                   //dataset_name
                   //name
                   //type
                   //record_name
               }
               //XMLHandler.getNodeValue(

                //       XMLHandler.getSubNode(xml, "attribute_name")
               //        );
                
                
                
            }

            //System.out.println(((StepMeta)jec[j]).getXML());

        }
        //saving the loop code using arraylists
        datasets = adDS.toArray(new String[k]);


        
        return datasets;
    }
    
    public String[] parseDatasets(List<StepMeta> jobs) throws Exception{
        //System.out.println(" ------------ parseDataSet ------------- ");
        String datasets[] = null;
        ArrayList<String> adDS = new ArrayList<String>();
      
        
        Object[] jec = jobs.toArray();

        int k = 0;

        for(int j = 0; j<jec.length; j++){
            //System.out.println("Node(i): " + j + " | " +((StepMeta)jec[j]).getName());

            if(!((StepMeta)jec[j]).getName().equalsIgnoreCase("START") && !((StepMeta)jec[j]).getName().equalsIgnoreCase("OUTPUT") && !((StepMeta)jec[j]).getName().equalsIgnoreCase("SUCCESS")){
                //System.out.println("Node(k): " + k);
                
                //adDS.add((String)((StepMeta)jec[j]).getName());
                String xml = ((StepMeta)jec[j]).getXML();
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
                   /*
                   recordsetName
                   recordName
                   datasetName
                   */
                   String recordsetName = XMLHandler.getNodeValue(
                           XMLHandler.getSubNode(nNode, "recordsetName")
                           );
                   String recordName = XMLHandler.getNodeValue(
                           XMLHandler.getSubNode(nNode, "recordName")
                           );
                   String datasetName = XMLHandler.getNodeValue(
                           XMLHandler.getSubNode(nNode, "datasetName")
                           );
                   
                 //outDS
                   String outDS = XMLHandler.getNodeValue(
                           XMLHandler.getSubNode(nNode, "outDS")
                           );
                   
                   System.out.println("XMLParse Type: " + type);
                   if(!type.equals("ECLMergePaths") && !type.equals("SPECIAL") && !type.equals("SUCCESS")){
    
                        if(dataset != null && !dataset.equals("")){
                            adDS.add((String)dataset);
                            k++;
                        }
                        if(record != null && !record.equals("")){
                            adDS.add((String)record);
                            k++;
                        }
                        if(recordset != null && !recordset.equals("")){
                            adDS.add((String)recordset);
                            k++;
                        }
                        
                        /*
                        recordsetName
                        recordName
                        datasetName
                        */
                        if(datasetName != null && !datasetName.equals("")){
                            adDS.add((String)datasetName);
                            k++;
                        }
                        if(recordName != null && !recordName.equals("")){
                            adDS.add((String)recordName);
                            k++;
                        }
                        if(recordsetName != null && !recordsetName.equals("")){
                            adDS.add((String)recordsetName);
                            k++;
                        }
                        
                      //outDS
                        if(outDS != null && !outDS.equals("")){
                            adDS.add((String)outDS);
                            k++;
                        }
                        
                   }

               }

                
            }

            //System.out.println(((StepMeta)jec[j]).getXML());

        }
        //saving the loop code using arraylists
        datasets = adDS.toArray(new String[k]);


        
        return datasets;
    }
    
    public String[] parseRecordsets(List<StepMeta> jobs) throws Exception{
        System.out.println(" ------------ parseRecordSets ------------- ");
        String datasets[] = null;
        ArrayList<String> adDS = new ArrayList<String>();
      
        
        Object[] jec = jobs.toArray();

        int k = 0;
        
        if(jec != null){
            

            for(int j = 0; j<jec.length; j++){
                //System.out.println("Node(i): " + j + " | " +((StepMeta)jec[j]).getName());

                if(!((StepMeta)jec[j]).getName().equalsIgnoreCase("START") && !((StepMeta)jec[j]).getName().equalsIgnoreCase("OUTPUT") && !((StepMeta)jec[j]).getName().equalsIgnoreCase("SUCCESS")){
                    //System.out.println("Node(k): " + k);

                    //adDS.add((String)((StepMeta)jec[j]).getName());
                    String xml = ((StepMeta)jec[j]).getXML();
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
                       
                       /*
                       recordsetName
                       recordName
                       datasetName
                       */
                       String recordsetName = XMLHandler.getNodeValue(
                               XMLHandler.getSubNode(nNode, "recordsetName")
                               );
                       String recordName = XMLHandler.getNodeValue(
                               XMLHandler.getSubNode(nNode, "recordName")
                               );
                       String datasetName = XMLHandler.getNodeValue(
                               XMLHandler.getSubNode(nNode, "datasetName")
                               );
                       
                       //outDS
                       String outDS = XMLHandler.getNodeValue(
                               XMLHandler.getSubNode(nNode, "outDS")
                               );
                       
                       System.out.println("XMLParse Type: " + type);
                       if(!type.equals("ECLMergePaths") && !type.equals("SPECIAL") && !type.equals("SUCCESS")){

                            //if(dataset != null){
                            //    adDS.add((String)dataset);
                            //    k++;
                            //}
                            if(record != null && !record.equals("")){
                                adDS.add((String)record);
                                k++;
                            }


                       }

                   }

                }


            }
            //saving the loop code using arraylists
            datasets = adDS.toArray(new String[k]);

        }
        
        return datasets;
    }
    
    /*
     * Gets the Dataset fields from all existing nodes
     * this function uses fieldsByDatasetList to build a List<String>
     * then it converts that to a String[] for use in the dialog classes
     * 
     */
    public String[] fieldsByDataset(String datasetName,List<StepMeta> jobs)throws Exception{
        System.out.println("***fieldsByDataset***");
        String datasets[] = new String[1];
        ArrayList<String> adDS = new ArrayList<String>();
        this.fieldsByDatasetList(adDS, datasetName,jobs);
        datasets = adDS.toArray(new String[adDS.size()]);
        return datasets;
    }
    
     /*
     * Gets the Dataset fields from all existing nodes
     * This uses recursion to travel up from joins etc to the 
     * def of the datasets
     */
    public void fieldsByDatasetList(ArrayList<String> adDS, String datasetName,List<StepMeta> jobs)throws Exception{
        System.out.println(" ------------ fieldsByDatasetList ------------ ");
        Object[] jec = jobs.toArray();

        int k = 0;

        if(jec != null){


            for(int j = 0; j<jec.length; j++){
                //System.out.println("Node(i): " + j + " | " +((StepMeta)jec[j]).getName());

                if(!((StepMeta)jec[j]).getName().equalsIgnoreCase("START") && !((StepMeta)jec[j]).getName().equalsIgnoreCase("OUTPUT") && !((StepMeta)jec[j]).getName().equalsIgnoreCase("SUCCESS")){
                    //System.out.println("Node(k): " + k);

                    //adDS.add((String)((StepMeta)jec[j]).getName());
                    String xml = ((StepMeta)jec[j]).getXML();
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

                //System.out.println(((StepMeta)jec[j]).getXML());

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
        System.out.println(" ------------ mergeArray ------------- ");
        
        String[] out = new String[a1.length+a2.length];
      
        System.arraycopy(a1, 0, out, 0, a1.length);
        System.arraycopy(a1, 0, out, a1.length, a2.length);
        
        return out;
    }

    
    public String getGlobalVariable(List<StepMeta> steps, String ofType) throws Exception{
        System.out.println(" ------------ getGlobalVariable ------------- ");
        String datasets[] = null;
        ArrayList<String> adDS = new ArrayList<String>();
        String out = "";
        
        Object[] jec = steps.toArray();

        int k = 0;
        
        if(jec != null){
            

            for(int j = 0; j<jec.length; j++){
                //System.out.println("Node(i): " + j + " | " +((StepMeta)jec[j]).getName());

                //if( ((StepMeta)jec[j]).getName().equalsIgnoreCase("ECLGlobalVariables") ){
                    //System.out.println("Node(k): " + k);

                    //adDS.add((String)((StepMeta)jec[j]).getName());
                 String xml = ((StepMeta)jec[j]).getXML();
                //    System.out.println(xml);

                   NodeList nl = (XMLHandler.loadXMLString(xml)).getChildNodes(); 
                   for (int temp = 0; temp < nl.getLength(); temp++){
                       Node nNode = nl.item(temp);
                       
                       String type = XMLHandler.getNodeValue(
                           XMLHandler.getSubNode(nNode, "type")
                           );
                      // System.out.println("Type: " + type);
                       if(type.equalsIgnoreCase("ECLGlobalVariablesStep")){
                    	  // System.out.println("match");
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
                          
                          
                                  

                       }
                      
                       
                   }

               // }


            }
            //saving the loop code using arraylists
            datasets = adDS.toArray(new String[k]);

        }
        return out;

    }
}
