/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.eclunivariate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
public class ECLUnivariate extends ECLJobEntry{//extends JobEntryBase implements Cloneable, JobEntryInterface {
    
    private String datasetName = "";
    private String logicalFileName = "";
    private java.util.List people = new ArrayList();
    private java.util.List group = new ArrayList();
    private String checkList = "";
    private String fieldList = "";
    private String single = "";
    private String mode = "";
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
	
	public void setGroup(java.util.List group){
		this.group = group;
	}
	
	public java.util.List getGroup(){
		return group;
	}

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getSingle() {
        return single;
    }

    public void setSingle(String single) {
        this.single = single;
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

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getCheckList() {
        return checkList;
    }

    public void setCheckList(String checkList) {
        this.checkList = checkList;
    }
    
    public String getFieldList() {
        return fieldList;
    }

    public void setFieldList(String fieldList) {
        this.fieldList= fieldList;
    }
    
    public String getlogicalFileName() {
        return logicalFileName;
    }

    public void setlogicalFileName(String logicalFileName) {
        this.logicalFileName = logicalFileName;
    }
    
    @Override
    public Result execute(Result prevResult, int k) throws KettleException {
    	Result result = prevResult;
        if(result.isStopped()){
        	return result;
        }
        else{
        	logBasic(Number);
        	String[] check = getCheckList().split(",");
        	String[] fieldNames = fieldList.split(",");
        	String normlist = "";int cnt = 0;
        	String List = "";
        	for(int i = 0; i<fieldNames.length; i++){
        		if(i!=fieldNames.length-1){
        			normlist += "LEFT."+fieldNames[i]+",";
        			List += "\'"+fieldNames[i]+"\',";
        		}
        		else{
        			normlist += "LEFT."+fieldNames[i];
        			List += "\'"+fieldNames[i]+"\'";
        		}
        	}
        	
        	String dataList = "";String col = "";String out = "";String grp = "";String forP = "";String join = "";String outfile = "";
        	for(Iterator it = group.iterator(); it.hasNext();){
        		Cols P = (Cols)it.next();        		
        		dataList += P.getType() + " " +P.getFirstName()+";\n";
        		col += "SELF."+P.getFirstName()+":=LEFT."+P.getFirstName()+",";
        		out += "OutDS."+P.getFirstName()+";\n";
        		grp += P.getFirstName()+",";
        		forP += "P."+P.getFirstName()+";\n";
        		outfile += "outfile."+P.getFirstName()+";\n";
        		join += "LEFT."+P.getFirstName()+"=RIGHT."+P.getFirstName()+" AND ";
        	}
        	
        	
			String ecl = "URec := RECORD\nUNSIGNED uid;\n"+this.datasetName+";\nEND;\n";
        	ecl += "URec Trans("+this.datasetName+" L, INTEGER C) := TRANSFORM\nSELF.uid := C;\nSELF := L;\nEND;\n"; 
        	ecl += "MyDS := PROJECT("+datasetName+",Trans(LEFT,COUNTER));\n";
        	
        	ecl += "NumField := RECORD\nUNSIGNED id;\n"+dataList+"STRING field;\nREAL8 value;\nEND;\n";
        	ecl += "OutDS := NORMALIZE(MyDS,"+fieldNames.length+", TRANSFORM(NumField,SELF.id:=LEFT.uid,"+col+"SELF.field:=CHOOSE(COUNTER,"+List+");SELF.value:=CHOOSE" +
        			"(COUNTER,"+normlist+")));\n";
        	ecl += "SingleField := RECORD\n"+out+"\nOutDS.field;\n";
        	if(check[0].equals("true"))
        		{ecl += "mean:=AVE(GROUP,OutDS.value);\n";cnt++;}
        	if(check[3].equals("true"))
        		{ecl += "Sd:=SQRT(VARIANCE(GROUP,OutDS.value));\n";cnt++;}
        	if(check[4].equals("true"))
        		{ecl += "Maxval:=MAX(GROUP,OutDS.value);\n";cnt++;}
        	if(check[5].equals("true"))
        		{ecl += "Minval:=MIN(GROUP,OutDS.value);\n";cnt++;}
        	ecl += "END;\n";
    		if(cnt>0)
        		ecl += "SingleUni := TABLE(OutDS,SingleField,"+grp+"field);\n";
        	
        	
        	if(check[1].equals("true") || check[2].equals("true")){
        	// this can be reused        		
	        	ecl += "RankableField := RECORD\nOutDS;\nUNSIGNED pos:=0;\nEND;\n";
	        	ecl += "T:=TABLE(SORT(OutDS,"+grp+"field,Value),RankableField);\n";
	        	ecl += "TYPEOF(T) add_rank(T le, UNSIGNED c):=TRANSFORM\nSELF.pos:=c;\nSELF:=le;\nEND;\n";
	        	ecl += "P:=PROJECT(T,add_rank(LEFT,COUNTER));\n";
	        	ecl += "RS:=RECORD\nSeq:=MIN(GROUP,P.pos);\n"+forP+"P.field;\nEND;\n";
	        	ecl += "Splits := TABLE(P,RS,"+grp+"field,FEW);\n";
	        	ecl += "TYPEOF(T) to(P le, Splits ri):=TRANSFORM\nSELF.pos:=1+le.pos-ri.Seq;\nSELF:=le;\nEND;\n";
	        	ecl += "outfile := JOIN(P,Splits,"+join+"LEFT.field=RIGHT.field,to(LEFT,RIGHT),LOOKUP);\n";
	        	if(check[1].equals("true")){
	        		ecl += "cntRec:=RECORD\n"+outfile+"outfile.field;\ncnt:=COUNT(GROUP);\nSET OF UNSIGNED poso := [];\nEND;\n";
	        		ecl += "cntgroups := TABLE(outfile,cntRec,"+grp+"field);\n";
	        		ecl += "cntRec Transcnt(cntgroups L, INTEGER C):=TRANSFORM\nSELF.poso := IF(L.cnt%2=0,[L.cnt/2,L.cnt/2 + 1],[(L.cnt+1)/2]);\nSELF:=L;\nEND;\n";
	        		ecl += "MyT := PROJECT(cntgroups,Transcnt(LEFT,COUNTER));\n";
	        		ecl += "MedianValues:=JOIN(outfile,MyT,"+join+"LEFT.field=RIGHT.field AND LEFT.pos IN RIGHT.poso);\n";
	        		ecl += "MedianTable := TABLE(MedianValues,{"+grp+"field;Median := AVE(GROUP, MedianValues.value);},"+grp+"field);\n";
	        		if(cnt == 0){
	        			ecl += getSingle()+" := MedianTable;\n";
	        			//ecl += "OUTPUT("+getSingle()+",NAMED('UnivariateStats'));\n";
	        			if(persist.equalsIgnoreCase("true")){
	    	        		if(outputName != null && !(outputName.trim().equals(""))){
	    	        			ecl += "OUTPUT("+getSingle()+",,'~eda::"+outputName+"::univariate_stats', __compressed__, overwrite,NAMED('UnivariateStats'))"+";\n";
	    	        		}else{
	    	        			ecl += "OUTPUT("+getSingle()+",,'~eda::"+defJobName+"::univariate_stats', __compressed__, overwrite,NAMED('UnivariateStats'))"+";\n";
	    	        		}
	    	        	}
	    	        	else{
	    	        			ecl += "OUTPUT("+getSingle()+",NAMED('UnivariateStats'));\n";
	    	        	}
	        		}
	        		else{
	        			ecl += getSingle()+" := JOIN(SingleUni,Mediantable,"+join+"LEFT.field = RIGHT.field);\n";
	        			//ecl += "OUTPUT("+getSingle()+",NAMED('UniVariate'));\n";
	        			if(persist.equalsIgnoreCase("true")){
	    	        		if(outputName != null && !(outputName.trim().equals(""))){
	    	        			ecl += "OUTPUT("+getSingle()+",,'~eda::"+outputName+"::univariate_stats', __compressed__, overwrite,NAMED('UnivariateStats'))"+";\n";
	    	        		}else{
	    	        			ecl += "OUTPUT("+getSingle()+",,'~eda::"+defJobName+"::univariate_stats', __compressed__, overwrite,NAMED('UnivariateStats'))"+";\n";
	    	        		}
	    	        	}
	    	        	else{
	    	        		ecl += "OUTPUT("+getSingle()+",NAMED('UnivariateStats'));\n";
	    	        	}
	        		}
	        	}
	        	
	        	if(check[2].equals("true")){
	        		ecl += "MTable := TABLE(outfile,{"+grp+"field;value;vals := COUNT(GROUP);},"+grp+"field,value);\n";
	        		ecl += "modT := TABLE(MTable,{"+grp+"field;cnt:=MAX(GROUP,vals)},"+grp+"field);\n";
	        		ecl += "Modes:=JOIN(MTable,ModT,"+join+"LEFT.field=RIGHT.field AND LEFT.vals=RIGHT.cnt);\n";
	        		ecl += getMode()+" := TABLE(Modes,{"+grp+"field;mode:=value;cnt});\n";
	        		//ecl += "OUTPUT("+getMode()+",NAMED('UniVariate_Mode'));\n";
	        		if(persist.equalsIgnoreCase("true")){
    	        		if(outputName != null && !(outputName.trim().equals(""))){
    	        			ecl += "OUTPUT("+getMode()+",,'~eda::"+outputName+"::univariate_mode', __compressed__, overwrite,NAMED('Univariate_Mode'))"+";\n";
    	        		}else{
    	        			ecl += "OUTPUT("+getMode()+",,'~eda::"+defJobName+"::univariate_mode', __compressed__, overwrite,NAMED('Univariate_Mode'))"+";\n";
    	        		}
    	        	}
    	        	else{
    	        		ecl += "OUTPUT("+getMode()+",NAMED('Univariate_Mode'));\n";
    	        	}
	        	}
	        	
        	}
        	if(cnt>0 && check[1].equals("false")){
        		ecl += getSingle()+" := SingleUni;\n";
        		//ecl += "OUTPUT("+getSingle()+",NAMED('Univariate'));\n";
        		if(persist.equalsIgnoreCase("true")){
	        		if(outputName != null && !(outputName.trim().equals(""))){
	        			ecl += "OUTPUT("+getSingle()+",,'~eda::"+outputName+"::univariate', __compressed__, overwrite,NAMED('Univariate'))"+";\n";
	        		}else{
	        			ecl += "OUTPUT("+getSingle()+",,'~eda::"+defJobName+"::univariate', __compressed__, overwrite,NAMED('Univariate'))"+";\n";
	        		}
	        	}
	        	else{
	        		ecl += "OUTPUT("+getSingle()+",NAMED('Univariate'));\n";
	        	}
        	}

        	
        	result.setResult(true);
	        
	        RowMetaAndData data = new RowMetaAndData();
	        data.addValue("ecl", Value.VALUE_TYPE_STRING, ecl);
	        
	        
	        List list = result.getRows();
	        list.add(data);
	        String eclCode = parseEclFromRowData(list);
	        result.setRows(list);
	        result.setLogText("ECLUnivariate executed, ECL code added");
        	return result;

        }
    }
    public String savePeople(){
    	String out = "";
    	
    	Iterator it = people.iterator();
    	boolean isFirst = true;
    	while(it.hasNext()){
    		if(!isFirst){out+="|";}
    		Cols p = (Cols) it.next();
    		out +=  p.getFirstName()+","+p.getType();
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
        		Cols P = new Cols();
        		P.setFirstName(S[0]);
        		P.setType(S[1]);
        		people.add(P);
        	}
        }
    }

    public String saveGroup(){
    	String out = "";
    	
    	Iterator it = group.iterator();
    	boolean isFirst = true;
    	while(it.hasNext()){
    		if(!isFirst){out+="|";}
    		Cols p = (Cols) it.next();
    		out +=  p.getFirstName()+","+p.getType();
            isFirst = false;
    	}
    	return out;
    }

    public void openGroup(String in){
        String[] strLine = in.split("[|]");
        int len = strLine.length;
        if(len>0){
        	group = new ArrayList();
        	for(int i = 0; i<len; i++){
        		String[] S = strLine[i].split(",");
        		Cols P = new Cols();
        		P.setFirstName(S[0]);
        		P.setType(S[1]);
        		group.add(P);
        	}
        }
    }

    
    @Override
    public void loadXML(Node node, List<DatabaseMeta> list, List<SlaveServer> list1, Repository rpstr) throws KettleXMLException {
        try {
            super.loadXML(node, list, list1);
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")) != null)
                setDatasetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "flag")) != null)
                setflag(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "flag")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "Number")) != null)
                setNumber(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "Number")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "checklist")) != null)
                setCheckList(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "checklist")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "logical_file_name")) != null)
                setlogicalFileName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "logical_file_name")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "people")) != null)
                openPeople(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "people")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "group")) != null)
                openGroup(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "group")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "fieldList")) != null)
                setFieldList(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "fieldList")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "single")) != null)
                setSingle(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "single")));
            //if(getCheckList().split(",")[2].equals("true")){
            	if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "mode")) != null)
                    setMode(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "mode")));
            //}
           if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "output_name")) != null)
               	setOutputName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "output_name")));
                    
           if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "label")) != null)
                setLabel(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "label")));
                    
           if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "persist_Output_Checked")) != null)
                setPersistOutputChecked(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "persist_Output_Checked")));
                
           if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "defJobName")) != null)
                setDefJobName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "defJobName")));	
            
        } catch (Exception e) {
            throw new KettleXMLException("ECL Dataset Job Plugin Unable to read step info from XML node", e);
        }

    }

    public String getXML() {
        String retval = "";
        
        retval += super.getXML();
        retval += "		<people><![CDATA[" + this.savePeople() + "]]></people>" + Const.CR;
        retval += "     <group eclIsGroup=\"true\"><![CDATA[" + this.saveGroup() + "]]></group>" + Const.CR;
        retval += "		<fieldList><![CDATA[" + fieldList + "]]></fieldList>" + Const.CR;
        retval += "		<flag><![CDATA[" + flag + "]]></flag>" + Const.CR;
        retval += "		<Number><![CDATA[" + Number + "]]></Number>" + Const.CR;
        retval += "		<logical_file_name><![CDATA[" + logicalFileName + "]]></logical_file_name>" + Const.CR;
        retval += "		<checklist eclIsUniv=\"true\"><![CDATA[" + checkList + "]]></checklist>" + Const.CR;
        retval += "		<dataset_name><![CDATA[" + datasetName + "]]></dataset_name>" + Const.CR;		
        retval += "		<single eclIsGraphable=\"true\" eclInception=\"true\"><![CDATA[" + single + "]]></single>" + Const.CR;
        if(getCheckList().length() > 0){
        	if(getCheckList().split(",")[2].equals("true"))
        		retval += "		<mode eclIsGraphable=\"true\" eclInception=\"true\"><![CDATA[" + mode + "]]></mode>" + Const.CR;
        }
        retval += "		<label><![CDATA[" + label + "]]></label>" + Const.CR;
        retval += "		<output_name><![CDATA[" + outputName + "]]></output_name>" + Const.CR;
        retval += "		<persist_Output_Checked><![CDATA[" + persist + "]]></persist_Output_Checked>" + Const.CR;
        retval += "		<defJobName><![CDATA[" + defJobName + "]]></defJobName>" + Const.CR;
        return retval;

    }

    public void loadRep(Repository rep, ObjectId id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
            throws KettleException {
        try {
            if(rep.getStepAttributeString(id_jobentry, "datasetName") != null)
                datasetName = rep.getStepAttributeString(id_jobentry, "datasetName"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "flag") != null)
            	flag = rep.getStepAttributeString(id_jobentry, "flag"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "Number") != null)
            	Number = rep.getStepAttributeString(id_jobentry, "Number"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "checklist") != null)
                checkList = rep.getStepAttributeString(id_jobentry, "checklist"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "logicalFileName") != null)
                logicalFileName = rep.getStepAttributeString(id_jobentry, "logicalFileName"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "people") != null)
                this.openPeople(rep.getStepAttributeString(id_jobentry, "people")); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "group") != null)
                this.openGroup(rep.getStepAttributeString(id_jobentry, "group")); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "fieldList") != null)
                fieldList = rep.getStepAttributeString(id_jobentry, "fieldList"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "single") != null)
                single = rep.getStepAttributeString(id_jobentry, "single"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "mode") != null)
            	mode = rep.getStepAttributeString(id_jobentry, "mode"); //$NON-NLS-1$
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
            rep.saveStepAttribute(id_job, getObjectId(), "datasetName", datasetName); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "checklist", checkList); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "logicalFileName", logicalFileName); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "people", this.savePeople()); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "group", this.saveGroup()); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "fieldList", fieldList); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "single", single); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "mode", mode); //$NON-NLS-1$
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
