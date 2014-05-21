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
	private String outTables[] = null;
	private String flag = "";
    private String Number = "";
    private String label ="";
	private String outputName ="";
	private String persist = "";
	private String defJobName = "";
	
	public String getDefJobName() {
		return defJobName;
	}

	public void setDefJobName(String defJobName) {
		this.defJobName = defJobName;
	}
	
	public String getPersistOutputChecked() {
		return persist;
	}

	public void setPersistOutputChecked(String persist) {
		this.persist = persist;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getOutputName() {
		return outputName;
	}

	public void setOutputName(String outputName) {
		this.outputName = outputName;
	}
    
	public void setPeople(java.util.List people){
		this.people = people;
	}
	
	public java.util.List getPeople(){
		return people;
	}

	public void setoutTables(String[] outTables){
		this.outTables = outTables;
	}
	
	public String[] getoutTables(){
		return outTables;
	}
	
	public String getName(){
		return Name;
	}
    
	public String getNumber() {
        return Number;
    }

    public void setNumber(String Number) {
        this.Number = Number;
    }

    public String getflag() {
        return flag;
    }

    public void setflag(String flag) {
        this.flag = flag;
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
	        String fieldStr = ""; String frequency = "";String[] norm = this.normList.split("-");String valueStr = "";String[] dataT = data_type.toLowerCase().split(",");
	        String fieldNum = "";String valueNum = "";int str = 0;int notstr = 0;outTables = new String[norm.length];
	        for(int i = 0; i < norm.length; i++){
	        	String[] cols = norm[i].split(",");
	        	if(dataT[i].startsWith("integer") || dataT[i].startsWith("decimal") || dataT[i].startsWith("real") || dataT[i].startsWith("unicode")){ 
	        		valueNum += "LEFT."+cols[0]+",";
        			fieldNum += "\'"+cols[0]+"\',";
        			notstr++;
        		}
        		else{
        			valueStr += "LEFT."+cols[0]+",";
        			fieldStr += "\'"+cols[0]+"\',";
        			str++;
        		}
	        }
	        if(valueStr.length()>0){
	        	valueStr = valueStr.substring(0, valueStr.length()-1);
	        	fieldStr = fieldStr.substring(0, fieldStr.length()-1);
	        	frequency += "NumFieldStr:=RECORD\nSTRING field;\nSTRING value;\nEND;\n";
	        	frequency += "OutDSStr := NORMALIZE("+this.getDatasetName()+","+str+", TRANSFORM(NumFieldStr,SELF.field:=CHOOSE(COUNTER,"+fieldStr+");SELF.value:=CHOOSE" +
	        			"(COUNTER,"+valueStr+")));\n";
	        	 frequency += "FreqRecStr:=RECORD\nOutDSStr.field;\nOutDSStr.value;\nINTEGER frequency:=COUNT(GROUP);\n" +
	        		     "REAL8 Percent:=(COUNT(GROUP)/COUNT("+this.DatasetName+"))*100;\n" +
	        		     "END;\n";
	        	 
	        	 frequency += "Frequency1 := TABLE(OutDSStr,FreqRecStr,field,value,MERGE);\n";
	        }
	        if(valueNum.length()>0){
	        	valueNum = valueNum.substring(0, valueNum.length()-1);		        
		        fieldNum = fieldNum.substring(0, fieldNum.length()-1);
		        frequency += "NumField:=RECORD\nSTRING field;\nSTRING value;\nEND;\n";
		        frequency += "OutDSNum := NORMALIZE("+this.getDatasetName()+","+notstr+", TRANSFORM(NumField,SELF.field:=CHOOSE(COUNTER,"+fieldNum+");SELF.value:=CHOOSE" +
	        			"(COUNTER,"+valueNum+")));\n";
		        frequency += "FreqRecNum:=RECORD\nOutDSNum.field;\nOutDSNum.value;\nINTEGER frequency:=COUNT(GROUP);\n" +
	       		     "REAL8 Percent:=(COUNT(GROUP)/COUNT("+this.DatasetName+"))*100;\n" +
	       		     "END;\n";
		        frequency += "Frequency2 := TABLE(OutDSNum,FreqRecNum,field,value,MERGE);\n";

	        }
	        
	        
	        for(int j = 0; j<norm.length; j++){
	        	String[] cols = norm[j].split(",");
	        	if(getSort().equals("NO") || getSort().equals("")){
	        		if(dataT[j].startsWith("string")) {
	        			frequency += cols[0]+"_Frequency"+getNumber()+":= TABLE(Frequency1(field = \'"+cols[0]+"\'),{"+dataT[j]+" "+cols[0]+":=value;frequency;Percent});\n";
	        			//frequency += "OUTPUT("+cols[0]+"_Frequency"+getNumber()+",THOR);\n";
<<<<<<< HEAD
=======
	        			if(persist.equalsIgnoreCase("true")){
	        	    		if(outputName != null && !(outputName.trim().equals(""))){
	        	    			frequency += "OUTPUT("+cols[0]+"_Frequency"+getNumber()+",,'~eda::"+outputName+cols[0]+"::frequency', __compressed__, overwrite,NAMED('Frequency_"+cols[0]+"'))"+";\n";
	        	    		}else{
	        	    			frequency += "OUTPUT("+cols[0]+"_Frequency"+getNumber()+",,'~eda::"+defJobName+cols[0]+"::frequency', __compressed__, overwrite,NAMED('Frequency_"+cols[0]+"'))"+";\n";
	        	    		}
	        	    	}
	        	    	else{
	        	    		frequency += "OUTPUT("+cols[0]+"_Frequency"+getNumber()+",NAMED('Frequency_"+cols[0]+"'));\n";
	        	    	}
>>>>>>> 45320053d50cd1f69e42a261625e082f6a2ed5f0
	        			
	        		}
	        		else{
	        			frequency += cols[0]+"_Frequency"+getNumber()+":=TABLE(Frequency2(field = \'"+cols[0]+"\'),{"+dataT[j]+" "+cols[0]+":=value;frequency;Percent});\n";
	        			//frequency += "OUTPUT("+cols[0]+"_Frequency"+getNumber()+",THOR);\n";
<<<<<<< HEAD
=======
	        			if(persist.equalsIgnoreCase("true")){
	        	    		if(outputName != null && !(outputName.trim().equals(""))){
	        	    			frequency += "OUTPUT("+cols[0]+"_Frequency"+getNumber()+",,'~eda::"+outputName+cols[0]+"::frequency', __compressed__, overwrite,NAMED('Frequency_"+cols[0]+"'))"+";\n";
	        	    		}else{
	        	    			frequency += "OUTPUT("+cols[0]+"_Frequency"+getNumber()+",,'~eda::"+defJobName+cols[0]+"::frequency', __compressed__, overwrite,NAMED('Frequency_"+cols[0]+"'))"+";\n";
	        	    		}
	        	    	}
	        	    	else{
	        	    		frequency += "OUTPUT("+cols[0]+"_Frequency"+getNumber()+",NAMED('Frequency_"+cols[0]+"'));\n";
	        	    	}
>>>>>>> 45320053d50cd1f69e42a261625e082f6a2ed5f0
	        			
	        		}
	        	}
	        	else{
	        		if(cols[1].equals("ASC")){
	        			if(cols[2].equals("NAME")){
	        				if(cols[3].equals("YES")){
	        					if(dataT[j].startsWith("string"))
	        						frequency += cols[0]+"_Frequency"+getNumber()+":=SORT(TABLE(Frequency1(field = \'"+cols[0]+"\'),{"+dataT[j]+" "+cols[0]+":=value;frequency;Percent}),(REAL)"+cols[0]+");\n";
	        					else	        						
	        						frequency += cols[0]+"_Frequency"+getNumber()+":=SORT(TABLE(Frequency2(field = \'"+cols[0]+"\'),{"+dataT[j]+" "+cols[0]+":=value;frequency;Percent}),(REAL)"+cols[0]+");\n";
	        				}
	        				else{
	        					if(dataT[j].startsWith("string"))
	        						frequency += cols[0]+"_Frequency"+getNumber()+":=SORT(TABLE(Frequency1(field = \'"+cols[0]+"\'),{"+dataT[j]+" "+cols[0]+":=value;frequency;Percent}),"+cols[0]+");\n";
	        					else
	        						frequency += cols[0]+"_Frequency"+getNumber()+":=SORT(TABLE(Frequency2(field = \'"+cols[0]+"\'),{"+dataT[j]+" "+cols[0]+":=value;frequency;Percent}),"+cols[0]+");\n";
	        				}
<<<<<<< HEAD
	        				//frequency += "output("+cols[0]+"_Frequency"+getNumber()+",,'~eda::frequency"+cols[0]+"', __compressed__, overwrite, named('Frequency of "+cols[0]+"'));\n";	
	        				//frequency += "OUTPUT("+cols[0]+"_Frequency"+getNumber()+",THOR);\n";
=======
	        					
	        				//frequency += "OUTPUT("+cols[0]+"_Frequency"+getNumber()+",THOR);\n";
	        				if(persist.equalsIgnoreCase("true")){
		        	    		if(outputName != null && !(outputName.trim().equals(""))){
		        	    			frequency += "OUTPUT("+cols[0]+"_Frequency"+getNumber()+",,'~eda::"+outputName+cols[0]+"::frequency', __compressed__, overwrite,NAMED('Frequency_"+cols[0]+"'))"+";\n";
		        	    		}else{
		        	    			frequency += "OUTPUT("+cols[0]+"_Frequency"+getNumber()+",,'~eda::"+defJobName+cols[0]+"::frequency', __compressed__, overwrite,NAMED('Frequency_"+cols[0]+"'))"+";\n";
		        	    		}
		        	    	}
		        	    	else{
		        	    		frequency += "OUTPUT("+cols[0]+"_Frequency"+getNumber()+",NAMED('Frequency_"+cols[0]+"'));\n";
		        	    	}
>>>>>>> 45320053d50cd1f69e42a261625e082f6a2ed5f0
	        				
	        			}
	        			else{
	        				if(dataT[j].startsWith("string"))
	        					frequency += cols[0]+"_Frequency"+getNumber()+":=SORT(TABLE(Frequency1(field = \'"+cols[0]+"\'),{"+dataT[j]+" "+cols[0]+":=value;frequency;Percent}),frequency);\n";
	        				else
	        					frequency += cols[0]+"_Frequency"+getNumber()+":=SORT(TABLE(Frequency2(field = \'"+cols[0]+"\'),{"+dataT[j]+" "+cols[0]+":=value;frequency;Percent}),frequency);\n";
<<<<<<< HEAD
	        			//	frequency += "OUTPUT("+cols[0]+"_Frequency"+getNumber()+",THOR);\n";
	        				//frequency += "output("+cols[0]+"_Frequency"+getNumber()+",,'~eda::frequency"+cols[0]+"', __compressed__, overwrite, named('Frequency of "+cols[0]+"'));\n";
=======
	        				//frequency += "OUTPUT("+cols[0]+"_Frequency"+getNumber()+",THOR);\n";
	        				if(persist.equalsIgnoreCase("true")){
		        	    		if(outputName != null && !(outputName.trim().equals(""))){
		        	    			frequency += "OUTPUT("+cols[0]+"_Frequency"+getNumber()+",,'~eda::"+outputName+cols[0]+"::frequency', __compressed__, overwrite,NAMED('Frequency_"+cols[0]+"'))"+";\n";
		        	    		}else{
		        	    			frequency += "OUTPUT("+cols[0]+"_Frequency"+getNumber()+",,'~eda::"+defJobName+cols[0]+"::frequency', __compressed__, overwrite,NAMED('Frequency_"+cols[0]+"'))"+";\n";
		        	    		}
		        	    	}
		        	    	else{
		        	    		frequency += "OUTPUT("+cols[0]+"_Frequency"+getNumber()+",NAMED('Frequency_"+cols[0]+"'));\n";
		        	    	}
>>>>>>> 45320053d50cd1f69e42a261625e082f6a2ed5f0


	        			}
	        		}
	        		else if(cols[1].equals("DESC")){
	        			if(cols[2].equals("NAME")){
	        				if(cols[3].equals("YES")){
	        					if(dataT[j].startsWith("string"))
	        						frequency += cols[0]+"_Frequency"+getNumber()+":=SORT(TABLE(Frequency1(field = \'"+cols[0]+"\'),{"+dataT[j]+" "+cols[0]+":=value;frequency;Percent}),-(REAL)"+cols[0]+");\n";
	        					else
	        						frequency += cols[0]+"_Frequency"+getNumber()+":=SORT(TABLE(Frequency2(field = \'"+cols[0]+"\'),{"+dataT[j]+" "+cols[0]+":=value;frequency;Percent}),-(REAL)"+cols[0]+");\n";
	        				}
	        				else{
	        					if(dataT[j].startsWith("string"))
	        						frequency += cols[0]+"_Frequency"+getNumber()+":=SORT(TABLE(Frequency1(field = \'"+cols[0]+"\'),{"+dataT[j]+" "+cols[0]+":=value;frequency;Percent}),-"+cols[0]+");\n";
	        					else
	        						frequency += cols[0]+"_Frequency"+getNumber()+":=SORT(TABLE(Frequency2(field = \'"+cols[0]+"\'),{"+dataT[j]+" "+cols[0]+":=value;frequency;Percent}),-"+cols[0]+");\n";
	        				}
	        				//frequency += "OUTPUT("+cols[0]+"_Frequency"+getNumber()+",THOR);\n";
<<<<<<< HEAD
	        				//frequency += "output("+cols[0]+"_Frequency"+getNumber()+",,'~eda::frequency"+cols[0]+"', __compressed__, overwrite, named('Frequency of "+cols[0]+"'));\n";
=======
	        				if(persist.equalsIgnoreCase("true")){
		        	    		if(outputName != null && !(outputName.trim().equals(""))){
		        	    			frequency += "OUTPUT("+cols[0]+"_Frequency"+getNumber()+",,'~eda::"+outputName+cols[0]+"::frequency', __compressed__, overwrite,NAMED('Frequency_"+cols[0]+"'))"+";\n";
		        	    		}else{
		        	    			frequency += "OUTPUT("+cols[0]+"_Frequency"+getNumber()+",,'~eda::"+defJobName+cols[0]+"::frequency', __compressed__, overwrite,NAMED('Frequency_"+cols[0]+"'))"+";\n";
		        	    		}
		        	    	}
		        	    	else{
		        	    		frequency += "OUTPUT("+cols[0]+"_Frequency"+getNumber()+",NAMED('Frequency_"+cols[0]+"'));\n";
		        	    	}
>>>>>>> 45320053d50cd1f69e42a261625e082f6a2ed5f0
	        				
	        			}
	        			else{
	        				if(dataT[j].startsWith("string"))
	        					frequency += cols[0]+"_Frequency"+getNumber()+":=SORT(TABLE(Frequency1(field = \'"+cols[0]+"\'),{"+dataT[j]+" "+cols[0]+":=value;frequency;Percent}),-frequency);\n";
	        				else
	        					frequency += cols[0]+"_Frequency"+getNumber()+":=SORT(TABLE(Frequency2(field = \'"+cols[0]+"\'),{"+dataT[j]+" "+cols[0]+":=value;frequency;Percent}),-frequency);\n";
	        				//frequency += "OUTPUT("+cols[0]+"_Frequency"+getNumber()+",THOR);\n";
<<<<<<< HEAD
	        				//frequency += "output("+cols[0]+"_Frequency"+getNumber()+",,'~eda::frequency"+cols[0]+"', __compressed__, overwrite, named('Frequency of "+cols[0]+"'));\n";
=======
	        				if(persist.equalsIgnoreCase("true")){
		        	    		if(outputName != null && !(outputName.trim().equals(""))){
		        	    			frequency += "OUTPUT("+cols[0]+"_Frequency"+getNumber()+",,'~eda::"+outputName+cols[0]+"::frequency', __compressed__, overwrite,NAMED('Frequency_"+cols[0]+"'))"+";\n";
		        	    		}else{
		        	    			frequency += "OUTPUT("+cols[0]+"_Frequency"+getNumber()+",,'~eda::"+defJobName+cols[0]+"::frequency', __compressed__, overwrite,NAMED('Frequency_"+cols[0]+"'))"+";\n";
		        	    		}
		        	    	}
		        	    	else{
		        	    		frequency += "OUTPUT("+cols[0]+"_Frequency"+getNumber()+",NAMED('Frequency_"+cols[0]+"'));\n";
		        	    	}
>>>>>>> 45320053d50cd1f69e42a261625e082f6a2ed5f0
	        				
	        			}
	        		}
	        	}
	        	frequency += "output("+cols[0]+"_Frequency"+getNumber()+",,'~eda::frequency"+cols[0]+"', __compressed__, overwrite, named('Frequency_of_"+cols[0]+"'));\n";
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

    public String saveOutTables(){
    	String out = "";int i = 0;    	
    	boolean isFirst = true;
    	if(outTables!=null){
	    	while(i < outTables.length){
	    		if(!isFirst){out+="|";}
	    		
	    		out +=  outTables[i];
	    		i++;
	            isFirst = false;
	    	}
    	}
    	return out;
    }

    public void openOutTables(String in){
    	String[] strLine = in.split("[|]");
    	int len = strLine.length;
    	if(len>0){
    		outTables = new String[len];
    		for(int i = 0; i<len; i++){
    			outTables[i] = strLine[i];
    		}
    	}
    }
    
    @Override
    public void loadXML(Node node, List<DatabaseMeta> list, List<SlaveServer> list1, Repository rpstr) throws KettleXMLException {
        try {
            super.loadXML(node, list, list1);
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "name")) != null)
                setDatasetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "name")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "flag")) != null)
                setflag(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "flag")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "Number")) != null)
                setNumber(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "Number")));
            
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
           
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "output_name")) != null)
                setOutputName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "output_name")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "label")) != null)
                setLabel(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "label")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "persist_Output_Checked")) != null)
                setPersistOutputChecked(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "persist_Output_Checked")));
            //if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outTables")) != null)
              //  openOutTables(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outTables")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "defJobName")) != null)
                setDefJobName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "defJobName")));
            String[] S = normList.split("-");
        	this.outTables = new String[S.length];        	
        	for(int i = 0; i<S.length; i++){
        		String[] s = S[i].split(",");
        		this.outTables[i] = s[0]+"_Frequency"+getNumber();
        	}

            
        } catch (Exception e) {
            throw new KettleXMLException("ECL Dataset Job Plugin Unable to read step info from XML node", e);
        }

    }

    public String getXML() {
        String retval = "";
        
        retval += super.getXML();
        retval += "		<name><![CDATA[" + Name + "]]></name>" + Const.CR;
        retval += "		<people><![CDATA[" + this.savePeople() + "]]></people>" + Const.CR;
        for(int i = 0; i<saveOutTables().split("[|]").length; i++){
        	retval += "		<outTables eclIsGraphable=\"true\"><![CDATA[" + saveOutTables().split("[|]")[i] + "]]></outTables>" + Const.CR;
        }
        retval += "		<data_type><![CDATA[" + data_type + "]]></data_type>" + Const.CR;
        retval += "		<Sort ><![CDATA[" + Sort + "]]></Sort>" + Const.CR;
        retval += "		<normList><![CDATA[" + this.getnormList() + "]]></normList>" + Const.CR;
        retval += "		<dataset_name><![CDATA[" + DatasetName + "]]></dataset_name>" + Const.CR;
        retval += "		<flag><![CDATA[" + flag + "]]></flag>" + Const.CR;
        retval += "		<Number><![CDATA[" + Number + "]]></Number>" + Const.CR;
        retval += "		<label><![CDATA[" + label + "]]></label>" + Const.CR;
        retval += "		<output_name><![CDATA[" + outputName + "]]></output_name>" + Const.CR;
        retval += "		<persist_Output_Checked><![CDATA[" + persist + "]]></persist_Output_Checked>" + Const.CR;
        retval += "		<defJobName><![CDATA[" + defJobName + "]]></defJobName>" + Const.CR;
        return retval;

    }

    public void loadRep(Repository rep, ObjectId id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
            throws KettleException {
        try {
        	if(rep.getStepAttributeString(id_jobentry, "Name") != null)
                Name = rep.getStepAttributeString(id_jobentry, "Name"); //$NON-NLS-1$
        	
        	if(rep.getStepAttributeString(id_jobentry, "datasetName") != null)
                DatasetName = rep.getStepAttributeString(id_jobentry, "datasetName"); //$NON-NLS-1$

        	if(rep.getStepAttributeString(id_jobentry, "flag") != null)
            	flag = rep.getStepAttributeString(id_jobentry, "flag"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "Number") != null)
            	Number = rep.getStepAttributeString(id_jobentry, "Number"); //$NON-NLS-1$
            
            if(rep.getStepAttributeString(id_jobentry, "Sort") != null)
            	Sort = rep.getStepAttributeString(id_jobentry, "Sort"); //$NON-NLS-1$
            
            if(rep.getStepAttributeString(id_jobentry, "normList") != null)
                this.setnormList(rep.getStepAttributeString(id_jobentry, "normList")); //$NON-NLS-1$
            
            if(rep.getStepAttributeString(id_jobentry, "people") != null)
                this.openPeople(rep.getStepAttributeString(id_jobentry, "people")); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "data_type") != null)
            	data_type = (rep.getStepAttributeString(id_jobentry, "data_type")); //$NON-NLS-1$
            //if(rep.getStepAttributeString(id_jobentry, "outTables") != null)
                //this.openOutTables(rep.getStepAttributeString(id_jobentry, "outTables")); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "outputName") != null)
            	outputName = rep.getStepAttributeString(id_jobentry, "outputName"); //$NON-NLS-1$
            
            if(rep.getStepAttributeString(id_jobentry, "label") != null)
            	label = rep.getStepAttributeString(id_jobentry, "label"); //$NON-NLS-1$
            
            if(rep.getStepAttributeString(id_jobentry, "persist_Output_Checked") != null)
            	persist = rep.getStepAttributeString(id_jobentry, "persist_Output_Checked"); //$NON-NLS-1$
            
            if(rep.getStepAttributeString(id_jobentry, "defJobName") != null)
            	defJobName = rep.getStepAttributeString(id_jobentry, "defJobName"); //$NON-NLS-1$
            
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
            rep.saveStepAttribute(id_job, getObjectId(), "outTables", this.saveOutTables()); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "flag", flag); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "Number", Number); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "outputName", outputName);
        	rep.saveStepAttribute(id_job, getObjectId(), "label", label);
        	rep.saveStepAttribute(id_job, getObjectId(), "persist_Output_Checked", persist);
        	rep.saveStepAttribute(id_job, getObjectId(), "defJobName", defJobName);
            
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
