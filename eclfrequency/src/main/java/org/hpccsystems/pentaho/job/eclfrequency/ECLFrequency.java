/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.eclfrequency;

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
public class ECLFrequency extends ECLJobEntry{//extends JobEntryBase implements Cloneable, JobEntryInterface {
	
	private String Name = "";
	private String Sort = "";
	private String DatasetName = "";
	private String normList = "";
	private String data_type = "";
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

	public String getDataType(){
		return data_type;
	}
    
	public void setDataType(String data_type){
		this.data_type = data_type;
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
    
    @Override
    public Result execute(Result prevResult, int k) throws KettleException {
    	
    	
        Result result = prevResult;
        if(result.isStopped()){
        	return result;
        }
        else{
        	//String sort = Sort;
	        String fieldStr = ""; String frequency = "";String[] norm = this.normList.split("-");String valueStr = "";String[] dataT = data_type.split(",");
	        String fieldNum = "";String valueNum = "";int str = 0;int notstr = 0;
	        for(int i = 0; i < norm.length; i++){
	        	String[] cols = norm[i].split(",");
	        	if(dataT[i].startsWith("string")){ 
        			valueStr += "LEFT."+cols[0]+",";
        			fieldStr += "\'"+cols[0]+"\',";
        			str++;
        		}
        		else{
        			valueNum += "LEFT."+cols[0]+",";
        			fieldNum += "\'"+cols[0]+"\',";
        			notstr++;
        		}
	        }
	        valueStr = valueStr.substring(0, valueStr.length()-1);
	        valueNum = valueNum.substring(0, valueNum.length()-1);
	        fieldStr = fieldStr.substring(0, fieldStr.length()-1);
	        fieldNum = fieldNum.substring(0, fieldNum.length()-1);
	        
	        frequency += "NumField:=RECORD\nSTRING field;\nREAL value;\nEND;\n";
	        frequency += "NumFieldStr:=RECORD\nSTRING field;\nSTRING value;\nEND;\n";
	        frequency += "OutDSStr := NORMALIZE("+this.getDatasetName()+","+str+", TRANSFORM(NumFieldStr,SELF.field:=CHOOSE(COUNTER,"+fieldStr+");SELF.value:=CHOOSE" +
        			"(COUNTER,"+valueStr+")));\n";
	        frequency += "OutDSNum := NORMALIZE("+this.getDatasetName()+","+notstr+", TRANSFORM(NumField,SELF.field:=CHOOSE(COUNTER,"+fieldNum+");SELF.value:=CHOOSE" +
        			"(COUNTER,"+valueNum+")));\n";
	        frequency += "FreqRecStr:=RECORD\nOutDSStr.field;\nOutDSStr.value;\nINTEGER frequency:=COUNT(GROUP);\n" +
	        		     "REAL8 Percent:=(COUNT(GROUP)/COUNT("+this.DatasetName+"))*100;\n" +
	        		     "END;\n";
	        frequency += "FreqRecNum:=RECORD\nOutDSNum.field;\nOutDSNum.value;\nINTEGER frequency:=COUNT(GROUP);\n" +
       		     "REAL8 Percent:=(COUNT(GROUP)/COUNT("+this.DatasetName+"))*100;\n" +
       		     "END;\n";
	        
	        
	        
	        frequency += "Frequency1 := TABLE(OutDSStr,FreqRecStr,field,value,MERGE);\n";
	        frequency += "Frequency2 := TABLE(OutDSNum,FreqRecNum,field,value,MERGE);\n";
	        for(int j = 0; j<norm.length; j++){
	        	String[] cols = norm[j].split(",");
	        	if(getSort().equals("NO") || getSort().equals("")){
	        		if(dataT[j].startsWith("string")) 
	        			frequency += "OUTPUT(TABLE(Frequency1(field = \'"+cols[0]+"\'),{"+dataT[j]+" "+cols[0]+":=value;frequency;Percent}),NAMED(\'"+cols[0]+"\'));\n";
	        		else
	        			frequency += "OUTPUT(TABLE(Frequency2(field = \'"+cols[0]+"\'),{"+dataT[j]+" "+cols[0]+":=value;frequency;Percent}),NAMED(\'"+cols[0]+"\'));\n";
	        	}
	        	else{
	        		if(cols[1].equals("ASC")){
	        			if(cols[2].equals("NAME")){
	        				if(cols[3].equals("YES")){
	        					if(dataT[j].startsWith("string"))
	        						frequency += cols[0]+"sor:=SORT(TABLE(Frequency1(field = \'"+cols[0]+"\'),{"+dataT[j]+" "+cols[0]+":=value;frequency;Percent}),(REAL)"+cols[0]+");\n";
	        					else	        						
	        						frequency += cols[0]+"sor:=SORT(TABLE(Frequency2(field = \'"+cols[0]+"\'),{"+dataT[j]+" "+cols[0]+":=value;frequency;Percent}),(REAL)"+cols[0]+");\n";
	        				}
	        				else{
	        					if(dataT[j].startsWith("string"))
	        						frequency += cols[0]+"sor:=SORT(TABLE(Frequency1(field = \'"+cols[0]+"\'),{"+dataT[j]+" "+cols[0]+":=value;frequency;Percent}),"+cols[0]+");\n";
	        					else
	        						frequency += cols[0]+"sor:=SORT(TABLE(Frequency2(field = \'"+cols[0]+"\'),{"+dataT[j]+" "+cols[0]+":=value;frequency;Percent}),"+cols[0]+");\n";
	        				}
	        					
	        				frequency += "OUTPUT("+cols[0]+"sor,NAMED(\'"+cols[0]+"\'));\n";
	        			}
	        			else{
	        				if(dataT[j].startsWith("string"))
	        					frequency += cols[0]+"sor:=SORT(TABLE(Frequency1(field = \'"+cols[0]+"\'),{"+dataT[j]+" "+cols[0]+":=value;frequency;Percent}),frequency);\n";
	        				else
	        					frequency += cols[0]+"sor:=SORT(TABLE(Frequency2(field = \'"+cols[0]+"\'),{"+dataT[j]+" "+cols[0]+":=value;frequency;Percent}),frequency);\n";
	        				frequency += "OUTPUT("+cols[0]+"sor,NAMED(\'"+cols[0]+"\'));\n";
	        			}
	        		}
	        		else if(cols[1].equals("DESC")){
	        			if(cols[2].equals("NAME")){
	        				if(cols[3].equals("YES")){
	        					if(dataT[j].startsWith("string"))
	        						frequency += cols[0]+"sor:=SORT(TABLE(Frequency1(field = \'"+cols[0]+"\'),{"+dataT[j]+" "+cols[0]+":=value;frequency;Percent}),-(REAL)"+cols[0]+");\n";
	        					else
	        						frequency += cols[0]+"sor:=SORT(TABLE(Frequency2(field = \'"+cols[0]+"\'),{"+dataT[j]+" "+cols[0]+":=value;frequency;Percent}),-(REAL)"+cols[0]+");\n";
	        				}
	        				else{
	        					if(dataT[j].startsWith("string"))
	        						frequency += cols[0]+"sor:=SORT(TABLE(Frequency1(field = \'"+cols[0]+"\'),{"+dataT[j]+" "+cols[0]+":=value;frequency;Percent}),-"+cols[0]+");\n";
	        					else
	        						frequency += cols[0]+"sor:=SORT(TABLE(Frequency2(field = \'"+cols[0]+"\'),{"+dataT[j]+" "+cols[0]+":=value;frequency;Percent}),-"+cols[0]+");\n";
	        				}
	        				frequency += "OUTPUT("+cols[0]+"sor,NAMED(\'"+cols[0]+"\'));\n";
	        			}
	        			else{
	        				if(dataT[j].startsWith("string"))
	        					frequency += cols[0]+"sor:=SORT(TABLE(Frequency1(field = \'"+cols[0]+"\'),{"+dataT[j]+" "+cols[0]+":=value;frequency;Percent}),-frequency);\n";
	        				else
	        					frequency += cols[0]+"sor:=SORT(TABLE(Frequency2(field = \'"+cols[0]+"\'),{"+dataT[j]+" "+cols[0]+":=value;frequency;Percent}),-frequency);\n";
	        				frequency += "OUTPUT("+cols[0]+"sor,NAMED(\'"+cols[0]+"\'));\n";
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
    		out +=  p.getFirstName()+","+p.getSort().toString()+","+p.getColumns().toString()+","+p.getType()+","+p.getSortNumeric().toString();
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
        		P.setColumns(Integer.parseInt(S[2]));
        		P.setType(S[3]);
        		P.setSortNumeric(Integer.parseInt(S[4]));
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

            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "Sort")) != null)
                setSort(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "Sort")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "normList")) != null)
                setnormList(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "normList")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "people")) != null)
                openPeople(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "people")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "data_type")) != null)
            	data_type = (XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "data_type")));
            
        } catch (Exception e) {
            throw new KettleXMLException("ECL Dataset Job Plugin Unable to read step info from XML node", e);
        }

    }

    public String getXML() {
        String retval = "";
        
        retval += super.getXML();
        retval += "		<name><![CDATA[" + Name + "]]></name>" + Const.CR;
        retval += "		<people><![CDATA[" + this.savePeople() + "]]></people>" + Const.CR;
        retval += "		<data_type><![CDATA[" + data_type + "]]></data_type>" + Const.CR;
        retval += "		<Sort ><![CDATA[" + Sort + "]]></Sort>" + Const.CR;
        retval += "		<normList><![CDATA[" + this.getnormList() + "]]></normList>" + Const.CR;
        retval += "		<dataset_name><![CDATA[" + DatasetName + "]]></dataset_name>" + Const.CR;
        
        return retval;

    }

    public void loadRep(Repository rep, ObjectId id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
            throws KettleException {
        try {
        	if(rep.getStepAttributeString(id_jobentry, "Name") != null)
                Name = rep.getStepAttributeString(id_jobentry, "Name"); //$NON-NLS-1$
        	
        	if(rep.getStepAttributeString(id_jobentry, "datasetName") != null)
                DatasetName = rep.getStepAttributeString(id_jobentry, "datasetName"); //$NON-NLS-1$

            if(rep.getStepAttributeString(id_jobentry, "Sort") != null)
            	Sort = rep.getStepAttributeString(id_jobentry, "Sort"); //$NON-NLS-1$
            
            if(rep.getStepAttributeString(id_jobentry, "normList") != null)
                this.setnormList(rep.getStepAttributeString(id_jobentry, "normList")); //$NON-NLS-1$
            
            if(rep.getStepAttributeString(id_jobentry, "people") != null)
                this.openPeople(rep.getStepAttributeString(id_jobentry, "people")); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "data_type") != null)
            	data_type = (rep.getStepAttributeString(id_jobentry, "data_type")); //$NON-NLS-1$

            
        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }

    public void saveRep(Repository rep, ObjectId id_job) throws KettleException {
        try {
        	rep.saveStepAttribute(id_job, getObjectId(), "datasetName", DatasetName); //$NON-NLS-1$
        	
        	rep.saveStepAttribute(id_job, getObjectId(), "Sort", Sort); //$NON-NLS-1$
        	
        	rep.saveStepAttribute(id_job, getObjectId(), "Name", Name); //$NON-NLS-1$
        	
        	rep.saveStepAttribute(id_job, getObjectId(), "normList", this.getnormList()); //$NON-NLS-1$
        	
            rep.saveStepAttribute(id_job, getObjectId(), "people", this.savePeople()); //$NON-NLS-1$
            
            rep.saveStepAttribute(id_job, getObjectId(), "data_type", data_type); //$NON-NLS-1$
            
            
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
