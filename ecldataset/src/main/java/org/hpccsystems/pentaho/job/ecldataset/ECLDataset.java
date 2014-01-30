/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.ecldataset;

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
public class ECLDataset extends ECLJobEntry{//extends JobEntryBase implements Cloneable, JobEntryInterface {
  
    private String logicalFileName = "";
    private String datasetName = "";
    private String recordName = "";
    private String recordDef = "";
    private String recordSet = "";
    private String fileType = "";
    private String xml = "";
    private RecordList recordList = new RecordList();
    
    private String hasHeaderRow = "No";
    private String csvSeparator = "";
    private String csvTerminator = "";
    private String csvQuote = "";
    

    public RecordList getRecordList() {
        return recordList;
    }

    public void setRecordList(RecordList recordList) {
        this.recordList = recordList;
    }

    public String getRecordSet() {
        return recordSet;
    }

    public void setRecordSet(String recordSet) {
        this.recordSet = recordSet;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getLogicalFileName() {
        return logicalFileName;
    }

    public void setLogicalFileName(String fileName) {
        this.logicalFileName = fileName;
    }

    public String getRecordDef() {
        return recordDef;
    }

    public void setRecordDef(String recordDef) {
        this.recordDef = recordDef;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    
    
    
    //public String resultListToString(){
    //	return resultListToString(this.recordList);
    //}
    
    public String getHasHeaderRow() {
		return hasHeaderRow;
	}

	public void setHasHeaderRow(String hasHeaderRow) {
		this.hasHeaderRow = hasHeaderRow;
	}

	public String getCsvSeparator() {
		return csvSeparator;
	}

	public void setCsvSeparator(String csvSeparator) {
		this.csvSeparator = csvSeparator;
	}

	public String getCsvTerminator() {
		return csvTerminator;
	}

	public void setCsvTerminator(String csvTerminator) {
		this.csvTerminator = csvTerminator;
	}

	public String getCsvQuote() {
		return csvQuote;
	}

	public void setCsvQuote(String csvQuote) {
		this.csvQuote = csvQuote;
	}

	public String fieldsValid(RecordList recordList){
        String errors = "";
        
        if(recordList != null){
            if(recordList.getRecords() != null && recordList.getRecords().size() > 0) {
            	int i = 1;
                    for (Iterator<RecordBO> iterator = recordList.getRecords().iterator(); iterator.hasNext();) {
                            RecordBO record = (RecordBO) iterator.next();
                            
                            //name type required
                            if(!(record.getColumnName() != null && !record.getColumnName().equals(""))){
                            	errors += "On the Fields Tab Row " + i + " is missing \"Column Name \"!\r\n";
                            }
                            if(!(record.getColumnType() != null && !record.getColumnType().equals("")&& !record.getColumnType().equals("Select"))){
                            	errors += "On the Fields Tab Row " + i + " is missing \"Column Type\"!\r\n";
                            } 
                            i++;
                    }
            }
        }
        
        return errors;
    }
	/*
        public String prepCSVFormat(String in){
        	String out = "";
        	String[] vals = in.split(",");
        	return out;
        }
      */                      
    @Override
    public Result execute(Result prevResult, int k) throws KettleException {
    	Result result = modifyResults(prevResult);
        if(result.isStopped()){
        	return result;
 		}
         System.out.println("Execute K value: " + k);
    	System.out.println("Dataset Execute Start");
    
    	JobMeta jobMeta = super.parentJob.getJobMeta();
        List<JobEntryCopy> jobs = jobMeta.getJobCopies();
        AutoPopulate ap = new AutoPopulate();
       
        Dataset dataset = new Dataset();
        dataset.setLogicalFileName(getLogicalFileName());
        dataset.setName(getDatasetName());
       // dataset.setRecordFormatString(getRecordDef());
        //use(hasNodeofType(type); from global variables here
        boolean isSaltHygiene = false;
        boolean isSaltSpecificity = false;
        try{
        	isSaltHygiene = ap.hasNodeofType(jobs, "SALTHygiene");
        	isSaltSpecificity = ap.hasNodeofType(jobs, "SALTSpecificity");
        }catch(Exception e){
        	
        }
       
        dataset.setRecordFormatString(resultListToString(this.recordList));
        dataset.setRecordName(getRecordName());
        dataset.setFileType(getFileType());
        dataset.setRecordSet(getRecordSet());
        
        dataset.setCsvQuote(csvQuote);
        dataset.setCsvSeparator(csvSeparator);
        dataset.setCsvTerminator(csvTerminator);
        System.out.println("hasHeaderRow: " + hasHeaderRow );
        if(hasHeaderRow.equalsIgnoreCase("Yes")){
        	dataset.setHasHeaderRow(true);
        }else{
        	dataset.setHasHeaderRow(false);
        }
        
        
        
        
        
        logBasic("{Dataset Job} Previous =" + result.getLogText());
        
        result.setResult(true);
        System.out.println("dataset adding ecl to row");
        RowMetaAndData data = new RowMetaAndData();
        data.addValue("ecl", Value.VALUE_TYPE_STRING, dataset.ecl());
        
        
        List list = result.getRows();
        list.add(data);
        result.setRows(list);
        System.out.println("dataset end adding ecl to row");
        //String eclCode = parseEclFromRowData(list);
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
            logBasic("{Dataset Job} ECL Code =" + eclCode);
        }
        */
        
        result.setLogText("ECLDataset executed, ECL code added");
        
        
        System.setProperty("Dataset-" + getDatasetName()+"-rsDef",  dataset.getRecordDef());
        System.setProperty("Dataset-" + getDatasetName()+"-dsDef",  dataset.getDatasetDef());
        System.setProperty("Dataset-" + getDatasetName()+"-rs", dataset.getRecordName());
        System.setProperty("Dataset-" + getDatasetName()+"-ds", dataset.getName());
        System.setProperty("Dataset-" + getDatasetName()+"-logical",this.getLogicalFileName());
        System.setProperty("Dataset-" + getDatasetName()+"-type",this.getFileType());
        
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
    	 					column[0] = "";//label
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
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "logical_file_name")) != null)
                setLogicalFileName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "logical_file_name")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "record_name")) != null)
                setRecordName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "record_name")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")) != null)
                setDatasetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "record_def")) != null)
                setRecordDef(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "record_def")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "recordSet")) != null)
                setRecordSet(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "recordSet")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "recordList")) != null)
                openRecordList(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "recordList")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "fileType")) != null)
                setFileType(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "fileType")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "xml")) != null)
                setXml(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "xml")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "csvSeparator")) != null)
                setCsvSeparator(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "csvSeparator")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "csvTerminator")) != null)
                setCsvTerminator(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "csvTerminator")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "csvQuote")) != null)
                setCsvQuote(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "csvQuote")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "hasHeaderRow")) != null)
                setHasHeaderRow(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "hasHeaderRow")));

        } catch (Exception e) {
            throw new KettleXMLException("ECL Dataset Job Plugin Unable to read step info from XML node", e);
        }

    }

    public String getXML() {
        String retval = "";
        
        retval += super.getXML();
      
        retval += "		<logical_file_name><![CDATA[" + logicalFileName + "]]></logical_file_name>" + Const.CR;
        retval += "		<record_name eclIsDef=\"true\" eclType=\"record\"><![CDATA[" + recordName + "]]></record_name>" + Const.CR;
        retval += "		<dataset_name eclIsDef=\"true\" eclType=\"dataset\"><![CDATA[" + datasetName + "]]></dataset_name>" + Const.CR;
        retval += "		<record_def><![CDATA[" + recordDef + "]]></record_def>" + Const.CR;
        retval += "		<recordSet><![CDATA[" + recordSet + "]]></recordSet>" + Const.CR;
        retval += "		<recordList><![CDATA[" + this.saveRecordList() + "]]></recordList>" + Const.CR;
        retval += "		<fileType><![CDATA[" + fileType + "]]></fileType>" + Const.CR;
        retval += "		<xml><![CDATA[" + xml + "]]></xml>" + Const.CR;
        retval += "		<csvSeparator><![CDATA[" + csvSeparator + "]]></csvSeparator>" + Const.CR;
        retval += "		<csvTerminator><![CDATA[" + csvTerminator + "]]></csvTerminator>" + Const.CR;
        retval += "		<csvQuote><![CDATA[" + csvQuote + "]]></csvQuote>" + Const.CR;
        retval += "		<hasHeaderRow><![CDATA[" + hasHeaderRow + "]]></hasHeaderRow>" + Const.CR;
        
        return retval;

    }

    public void loadRep(Repository rep, ObjectId id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
            throws KettleException {
        try {
            if(rep.getStepAttributeString(id_jobentry, "logicalFileName") != null)
                logicalFileName = rep.getStepAttributeString(id_jobentry, "logicalFileName"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "datasetName") != null)
                datasetName = rep.getStepAttributeString(id_jobentry, "datasetName"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "recordName") != null)
                recordName = rep.getStepAttributeString(id_jobentry, "recordName"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "recordDef") != null)
                recordDef = rep.getStepAttributeString(id_jobentry, "recordDef"); //$NON-NLS-1$
            
             if(rep.getStepAttributeString(id_jobentry, "recordSet") != null)
                recordSet = rep.getStepAttributeString(id_jobentry, "recordSet"); //$NON-NLS-1$
             
             if(rep.getStepAttributeString(id_jobentry, "recordList") != null)
                this.openRecordList(rep.getStepAttributeString(id_jobentry, "recordList")); //$NON-NLS-1$
             
             if(rep.getStepAttributeString(id_jobentry, "fileType") != null)
                fileType = rep.getStepAttributeString(id_jobentry, "fileType"); //$NON-NLS-1$
             if(rep.getStepAttributeString(id_jobentry, "xml") != null)
                 xml = rep.getStepAttributeString(id_jobentry, "xml"); //$NON-NLS-1$
             if(rep.getStepAttributeString(id_jobentry, "csvSeparator") != null)
            	 csvSeparator = rep.getStepAttributeString(id_jobentry, "csvSeparator"); //$NON-NLS-1$
             if(rep.getStepAttributeString(id_jobentry, "csvTerminator") != null)
            	 csvTerminator = rep.getStepAttributeString(id_jobentry, "csvTerminator"); //$NON-NLS-1$
             if(rep.getStepAttributeString(id_jobentry, "csvQuote") != null)
            	 csvQuote = rep.getStepAttributeString(id_jobentry, "csvQuote"); //$NON-NLS-1$
            
             if(rep.getStepAttributeString(id_jobentry, "hasHeaderRow") != null)
            	 hasHeaderRow = rep.getStepAttributeString(id_jobentry, "hasHeaderRow"); //$NON-NLS-1$
        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }

    public void saveRep(Repository rep, ObjectId id_job) throws KettleException {
        try {
            rep.saveStepAttribute(id_job, getObjectId(), "logicalFileName", logicalFileName); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "datasetName", datasetName); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "recordName", recordName); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "recordDef", recordDef); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "recordSet", recordSet); //$NON-NLS-1$
            
            rep.saveStepAttribute(id_job, getObjectId(), "recordList", this.saveRecordList()); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "fileType", fileType); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "xml", xml); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "csvSeparator", csvSeparator); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "csvTerminator", csvTerminator); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "csvQuote", csvQuote); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "hasHeaderRow", hasHeaderRow); //$NON-NLS-1$
           
        } catch (Exception e) {
            throw new KettleException("Unable to save info into repository" + id_job, e);
        }
    }

  
    
    public ArrayList<String[]> parseFileHeader(String serverHost, int serverPort, String user,String pass, String fileName){
    	
    	GetHeader header = new GetHeader(serverHost,serverPort,user,pass);
    	
    	List<Header> headers = new ArrayList<Header>();
			
		headers = header.retrieveHeaderInformation(fileName);
		ArrayList<String[]> details = new ArrayList<String[]>();
		for (Iterator<Header> iter = headers.iterator(); iter.hasNext();) 
		{
			Header entry = (Header) iter.next();
			System.out.println("Column Name: " + entry.getColumnName());		
			System.out.println("Data Type: " + entry.getDataType());
			String[] column = new String[5];
			column[0] = entry.getColumnName();
			column[1] = entry.getDataType();
			column[2] = "";
			column[3] = "";
			column[4] = "";
			details.add(column);
		}
		/*
		 * column[0] = attributes.getNamedItem("label").getTextContent();
		   column[1] = attributes.getNamedItem("ecltype").getTextContent();
		   column[2] = "";
		   column[3] = attributes.getNamedItem("size").getTextContent();
		   column[4] = attributes.getNamedItem("size").getTextContent();
		 */
    	return details;
    }
}
