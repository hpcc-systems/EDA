/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.eclfilter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hpccsystems.javaecl.Filter;
import org.hpccsystems.javaecl.Project;
import org.pentaho.di.cluster.SlaveServer;
import org.pentaho.di.compatibility.Value;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Result;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.job.entry.JobEntryBase;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.w3c.dom.Node;
import org.hpccsystems.javaecl.Dataset;
import org.hpccsystems.recordlayout.RecordBO;
import org.hpccsystems.recordlayout.RecordList;
import org.hpccsystems.mapper.*;
import org.hpccsystems.ecljobentrybase.*;


/**
 *
 * @author ShetyeD
 */
public class ECLFilter extends ECLJobEntry{//extends JobEntryBase implements Cloneable, JobEntryInterface {
    
    private String recordsetName = "";
    private String inRecordName = "";
    private String outRecordName = "";
    private String filterFormat = "";
    private String filterStatement = "";
    private RecordList recordList = new RecordList();
    
    private MapperRecordList mapperRecList = new MapperRecordList();

    public String getRecordsetName() {
        return recordsetName;
    }
    public void setRecordsetName(String recordsetName) {
        this.recordsetName = recordsetName;
    }
    public String getInRecordName() {
        return inRecordName;
    }
    public void setInRecordName(String inRecordName) {
        this.inRecordName = inRecordName;
    }
    public String getOutRecordName(){
        return outRecordName;
    }
    public void setOutRecordName(String outRecordName){
        this.outRecordName = outRecordName;
    }
    public String getFilterFormat(){
        return this.filterFormat;
    }
    public void setFilterFormat(String filterFormat){
        this.filterFormat = filterFormat;
    }

    public RecordList getRecordList() {
        return recordList;
    }

    public void setRecordList(RecordList recordList) {
        this.recordList = recordList;
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
	public String saveRecordList() {
		String out = "";
		ArrayList list = recordList.getRecords();
		Iterator<RecordBO> itr = list.iterator();
		boolean isFirst = true;
		while (itr.hasNext()) {
			if (!isFirst) {
				out += "|";
			}

			out += itr.next().toCSV();
			isFirst = false;
		}
		
		//System.out.println("RESULT of saveRecordList() ........... "+out);
		
		return out;
	}

	public void openRecordList(String in) {
		//System.out.println("Inside Method openRecordList .........."+in);
		String[] strLine = in.split("[|]");

		int len = strLine.length;
		if (len > 0) {
			recordList = new RecordList();
			//System.out.println("Open Record List");
			for (int i = 0; i < len; i++) {
				//System.out.println("++++++++++++" + strLine[i]);
				// this.recordDef.addRecord(new RecordBO(strLine[i]));
				RecordBO rb = new RecordBO(strLine[i]);
				//System.out.println(rb.getColumnName());
				recordList.addRecordBO(rb);
			}
		}
	}
	
	/**
	 * This method is used to generate ECl for the Mapper Grid values
	 * @param arlRecords
	 * @return String containing ECL Code 
	 */
	/*public String generateEclForMapperGrid() {
		String out = "";
		if (mapperRecList != null) {
			if (mapperRecList.getRecords() != null && mapperRecList.getRecords().size() > 0) {
				//System.out.println("Size: " + mapperRecList.getRecords().size());
				for (Iterator<MapperBO> iterator = mapperRecList.getRecords().iterator(); iterator.hasNext();) {
					MapperBO record = (MapperBO) iterator.next();
					out += record.getExpression();
					out += " ";
				}
			}
		}
		//System.out.println("RESULT of generateEclForMapperGrid() ........... "+out);
		
		return out;
	}
	*/
    
	public String saveRecordListForMapper() {
		String out = "";
		ArrayList list = mapperRecList.getRecords();
		Iterator<MapperBO> itr = list.iterator();
		boolean isFirst = true;
		while (itr.hasNext()) {
			//System.out.println("loop");
			if (!isFirst) {
				out += "|";
			}

			out += itr.next().toCSV();
			isFirst = false;
		}
		
		//System.out.println("RESULT of saveRecordListForMapper() ........... "+out);
		
		return out;
	}

	public void openRecordListForMapper(String in) {
		//System.out.println("Inside Method openRecordListForMapper .........."+in);
		String[] strLine = in.split("[|]");

		int len = strLine.length;
		if (len > 0) {
			mapperRecList = new MapperRecordList();
			//System.out.println("Open Record List");
			for (int i = 0; i < len; i++) {
				//System.out.println("++++++++++++" + strLine[i]);
				// this.recordDef.addRecord(new RecordBO(strLine[i]));
				MapperBO rb = new MapperBO(strLine[i]);
				mapperRecList.addRecord(rb);
			}
		}
	}
	

    @Override
    public Result execute(Result prevResult, int k) throws KettleException {
        
        Result result = modifyResults(prevResult);
        if(result.isStopped()){
        	return result;
 		}
        Filter filter = new Filter();
        filter.setName(this.getRecordsetName());

        filter.setInRecordName(this.getInRecordName());
       
        filter.setFilterStatement(this.getFilterStatement());
        
        String ecl = filter.ecl();

        logBasic("{Project Job} Execute = " + ecl);
        //System.out.println("PROJECT JOB ---->>>> : "+project.ecl());
        
        logBasic("{Project Job} Previous =" + result.getLogText());
        
        //ecl += "OUTPUT(Filter);";
        
        result.setResult(true);
        
        RowMetaAndData data = new RowMetaAndData();
        data.addValue("ecl", Value.VALUE_TYPE_STRING, ecl);
        
        
        List list = result.getRows();
        list.add(data);
        String eclCode = parseEclFromRowData(list);
        
        result.setRows(list);
        
        
        return result;
    }

    @Override
    public void loadXML(Node node, List<DatabaseMeta> list, List<SlaveServer> list1, Repository rpstr) throws KettleXMLException {
        try {
            super.loadXML(node, list, list1);

            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "recordset_name")) != null)
                setRecordsetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "recordset_name")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "inRecordName")) != null)
                setInRecordName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "inRecordName")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outRecordName")) != null)
                setOutRecordName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outRecordName")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "filterFormat")) != null)
                setFilterFormat(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "filterFormat")));
            //if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"parameterName")) != null)
             //   setParameterName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"parameterName")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "recordList")) != null)
                openRecordList(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "recordList")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "mapperRecList")) != null)
            	openRecordListForMapper(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "mapperRecList")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "filterStatement")) != null)
            	setFilterStatement(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "filterStatement")));
            
            
        } catch (Exception e) {
            throw new KettleXMLException("ECL Project Job Plugin Unable to read step info from XML node", e);
        }

    }

    public String getXML() {
        String retval = "";

        retval += super.getXML();

        retval += "		<recordset_name eclIsDef=\"true\" eclType=\"dataset\"><![CDATA[" + recordsetName + "]]></recordset_name>" + Const.CR;
        retval += "		<inRecordName><![CDATA[" + inRecordName + "]]></inRecordName>" + Const.CR;
        retval += "		<outRecordName clIsDef=\"true\" eclType=\"record\"><![CDATA[" + outRecordName + "]]></outRecordName>" + Const.CR;
        retval += "		<filterFormat><![CDATA[" + filterFormat + "]]></filterFormat>" + Const.CR;
        retval += "		<recordList><![CDATA[" + this.saveRecordList() + "]]></recordList>" + Const.CR;
        retval += "		<mapperRecList><![CDATA[" + this.saveRecordListForMapper() + "]]></mapperRecList>" + Const.CR;
        retval += "		<filterStatement><![CDATA[" + this.filterStatement + "]]></filterStatement>" + Const.CR;
        
        return retval;

    }

    public void loadRep(Repository rep, ObjectId id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
            throws KettleException {
        try {
            //jobName = rep.getStepAttributeString(id_jobentry, "jobName"); //$NON-NLS-1$

            if(rep.getStepAttributeString(id_jobentry, "recordsetName") != null)
                recordsetName = rep.getStepAttributeString(id_jobentry, "recordsetName"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "inRecordName") != null)
                inRecordName = rep.getStepAttributeString(id_jobentry, "inRecordName"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "outRecordName") != null)
                outRecordName = rep.getStepAttributeString(id_jobentry, "outRecordName"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "filterFormat") != null)
                filterFormat = rep.getStepAttributeString(id_jobentry, "filterFormat"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "recordList") != null)
                this.openRecordList(rep.getStepAttributeString(id_jobentry, "recordList")); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "mapperRecList") != null)
                this.openRecordListForMapper(rep.getStepAttributeString(id_jobentry, "mapperRecList")); //$NON-NLS-1$
            
            if(rep.getStepAttributeString(id_jobentry, "filterStatement") != null)
            	filterStatement = rep.getStepAttributeString(id_jobentry, "filterStatement"); //$NON-NLS-1$
        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }

    public void saveRep(Repository rep, ObjectId id_job) throws KettleException {
        try {
            rep.saveStepAttribute(id_job, getObjectId(), "recordsetName", recordsetName); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "inRecordName", inRecordName); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "outRecordName", outRecordName); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "filterFormat", filterFormat); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "recordList", this.saveRecordList()); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "mapperRecList", this.saveRecordListForMapper()); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "filterStatement", this.filterStatement); //$NON-NLS-1$
            
        } catch (Exception e) {
            throw new KettleException("Unable to save info into repository" + id_job, e);
        }
    }

    
}
