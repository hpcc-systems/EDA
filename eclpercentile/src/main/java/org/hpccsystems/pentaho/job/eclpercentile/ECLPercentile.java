/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.eclpercentile;

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
import org.hpccsystems.eclguifeatures.AutoPopulate;
import org.hpccsystems.ecljobentrybase.*;


/**
 *
 * @author KeshavS
 */
public class ECLPercentile extends ECLJobEntry{//extends JobEntryBase implements Cloneable, JobEntryInterface {
	
	private String Name = "";
	private String DatasetName = "";
	private String normList = "";
	private String outTables[] = null;
	//private ArrayList<String[]> percentileSettings = new ArrayList<String[]>();
	private java.util.List fields = new ArrayList();
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

	public void setFields(java.util.List fields){
		this.fields = fields;
	}
	
	public java.util.List getFields(){
		return fields;
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
    
	public void setName(String Name){
		this.Name = Name;
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
	
/*	public ArrayList<String[]> getpercentileSettings(){
		return percentileSettings;
	}
	
	public void setpercentileSettings(ArrayList<String[]> percentileSettings){
		this.percentileSettings = percentileSettings;
	}
*/  final AutoPopulate ap = new AutoPopulate();
	
	
    @Override
    public Result execute(Result prevResult, int k) throws KettleException {

    	
    	
        Result result = prevResult;
        if(result.isStopped()){
        	return result;
        }
        else{
	        String percentile = "";String[] norm = this.normList.split("#");String value = "";String field = "";String normString = "SET OF INTEGER buckets:=[";
	        String percents = "";String normalize = "";int high = 0;
	        for(int i = 0; i<norm.length; i++){
	        	String[] S = norm[i].split("-");
	        	if(high < S[1].split(",").length)
	        		high = S[1].split(",").length;
	        	if(i != norm.length-1){ 
        			value += "LEFT."+S[0]+",";
        			field += "\'"+S[0]+"\',";
        			normalize += "IF(SELF.field='"+S[0]+"',"+S[0]+"P[COUNTER],";
        		}
        		else{
        			value += "LEFT."+S[0];
        			field += "\'"+S[0]+"\'";        			
        			normalize += S[0]+"P[COUNTER]";
        		}
	        	percents += "SET OF INTEGER "+S[0]+"P := [0,1,5,10,25,50,75,90,95,99,100";
    			if(!S[1].equals(" "))
    				percents += ","+S[1]+"];\n";
    			else
    				percents += "];\n";
	        }
	        for(int i = 0; i<norm.length-1; i++){
	        	normalize += ")";
	        }
	        percentile += "NormRec:=RECORD\nINTEGER4 number;\nSTRING field;\nREAL value;\nEND;\n";
	        percentile += "OutDS:=NORMALIZE("+this.getDatasetName()+","+norm.length+",TRANSFORM(NormRec,SELF.number:=COUNTER,SELF.field:=CHOOSE(COUNTER,"+field+")" +
	        		",SELF.value:=CHOOSE(COUNTER,"+value+")));\n";
	        percentile += "RankableField := RECORD\nOutDS;\nUNSIGNED pos:=0;\nEND;\n";
	        percentile += "T:=TABLE(SORT(OutDS,field,Value),RankableField);\n";
	        percentile += "TYPEOF(T) add_rank(T le, UNSIGNED c):=TRANSFORM\nSELF.pos:=c;\nSELF:=le;\nEND;\n";
	        percentile += "P:=PROJECT(T,add_rank(LEFT,COUNTER));\n";
	        percentile += "RS:=RECORD\nSeq:=MIN(GROUP,P.pos);\nP.field;\nEND;\n";
	        percentile += "Splits := TABLE(P,RS,field,FEW);\n";
	        percentile += "TYPEOF(T) to(P le, Splits ri):=TRANSFORM\nSELF.pos:=1+le.pos-ri.Seq;\nSELF:=le;\nEND;\n";
	        percentile += "outfile := JOIN(P,Splits,LEFT.field=RIGHT.field, to(LEFT,RIGHT),LOOKUP);\n";
	        percentile += "N:=COUNT("+this.getDatasetName()+");\n";
	        	       
/*	        for(Iterator<String[]> it = percentileSettings.iterator(); it.hasNext();)
	        {
	        	String[] S = it.next();
	        	normString += S[1].split("#").length+",";	        	
	        }
	        normString =  normString.substring(0, normString.length()-1);

	        percentile += normString+"];\nmaximum:=MAX(buckets);\n";
	        percentile += "T roll(T L, T R):=TRANSFORM\nSELF:=L;\nEND;\n";
	        percentile += "Split:=ROLLUP(T, LEFT.field = RIGHT.field, roll(LEFT,RIGHT));\n";*/
	        
	        percentile += percents;
	        percentile += "Rec1:=RECORD\nSTRING field;\nINTEGER4 percentiles;\nEND;\n";
	        
	        //percentile += "MyTab:=NORMALIZE(Split,maximum+1,TRANSFORM(Rec1,SELF.field:=LEFT.field,SELF.Percentiles:=IF(buckets[LEFT.number]>=COUNTER-1," +
	        	//	"IF(ROUNDUP(100/buckets[LEFT.number])=100/buckets[LEFT.number],(COUNTER-1)*(100/buckets[LEFT.number]),(COUNTER-1)*(ROUNDUP(100/buckets[LEFT.number])-1)),-1)));\n";
	        
	        percentile += "MyTab:=NORMALIZE(Splits,"+(11+high)+",TRANSFORM(Rec1,SELF.field:=LEFT.field,SELF.percentiles:="+normalize+"));\n";
	        
	        percentile += "PerRec:=RECORD\nMyTab;\nREAL rank:=IF(mytab.percentiles = -1,0," +
																"IF(mytab.percentiles = 0,1,"+
																   "IF(ROUND(mytab.percentiles*(N+1)/100)>=N,N,"+
																	  "mytab.percentiles*(N+1)/100)));\nEND;\n";
	        percentile += "valuestab := TABLE(mytab,perRec);\n";
	        percentile += "rankRec := RECORD\nSTRING field := valuestab.field;\nREAL rank := valuestab.rank;\nINTEGER4 intranks;\nREAL decranks;\nINTEGER4 plusOneranks;\n"+
								"valuestab.percentiles;\nEND;\n";
	        percentile += "rankRec tr(valuestab L, INTEGER C) := TRANSFORM\n"+
					      "SELF.decranks := IF(L.rank - (ROUNDUP(L.rank) - 1) = 1,0,L.rank - (ROUNDUP(L.rank) - 1));\n"+						  
						  "SELF.intranks := IF(ROUNDUP(L.rank) = L.rank,L.rank,(ROUNDUP(L.rank) - 1));\n"+
						  "SELF.plusOneranks := SELF.intranks + 1;\n"+
						  "SELF := L;\n"+
						  "END;\n";
	        percentile += "ranksTab := PROJECT(valuestab,tr(LEFT,COUNTER));\n";
	        percentile += "ranksRec := RECORD\nSTRING field;\nranksTab.decranks;\nranksTab.percentiles;\nINTEGER4 ranks;\nEND;\n";
	        percentile += "rankTab := NORMALIZE(ranksTab,2,TRANSFORM(ranksRec,SELF.field := LEFT.field; SELF.ranks := CHOOSE(COUNTER,LEFT.intranks,LEFT.plusOneranks),SELF := LEFT));\n";
	        percentile += "MTable:=SORT(JOIN(rankTab, outfile, LEFT.field = RIGHT.field AND LEFT.ranks = RIGHT.pos),field,percentiles,ranks);\n";
	        percentile += "MyTable := DEDUP(MTable, LEFT.percentiles = RIGHT.percentiles AND LEFT.ranks = RIGHT.ranks AND LEFT.field = RIGHT.field);\n";
	        percentile += "MyTable RollThem(MyTable L, MyTable R) := TRANSFORM\n"+
   						  "SELF.value := L.value + L.decranks*(R.value - L.value);\n"+
						  "SELF := L;\n"+
						  "END;\n";
	        percentile += "percentileTab := ROLLUP(MyTable, LEFT.percentiles = RIGHT.percentiles AND LEFT.field=RIGHT.field, RollThem(LEFT,RIGHT));\n";
	        for(int i = 0, j=norm.length; i<norm.length; i++,j++){
	        	String[] S = norm[i].split("-");
	        	percentile += S[0]+"_"+getName()+":=TABLE(percentileTab(field='"+S[0]+"'),{field,percentiles,value});\n";
	        	if(persist.equalsIgnoreCase("true")){
	        		if(outputName != null && !(outputName.trim().equals(""))){
	        			percentile += "OUTPUT("+S[0]+"_"+getName()+",,'~eda::percentile::"+outputName+S[0]+"', __compressed__, overwrite,NAMED('Percentile_"+S[0]+"_"+i+"'))"+";\n";
	        		}else{
	        			percentile += "OUTPUT("+S[0]+"_"+getName()+",,'~eda::percentile::"+defJobName+S[0]+"', __compressed__, overwrite,NAMED('Percentile_"+S[0]+"_"+i+"'))"+";\n";
	        		}
	        	}
	        	else{
	        		percentile += "OUTPUT("+S[0]+"_"+getName()+",NAMED('Percentile_"+S[0]+"_"+i+"'));\n";
	        	}
	        	//percentile += "OUTPUT("+S[0]+"_"+getName()+",THOR);\n";
	        }
        	
	        RowMetaAndData data = new RowMetaAndData();
	        data.addValue("ecl", Value.VALUE_TYPE_STRING, percentile);
	        
	        
	        List list = result.getRows();
	        list.add(data);
	        String eclCode = parseEclFromRowData(list);
	        result.setRows(list);
	        result.setLogText("ECLPercentile executed, ECL code added");
        }
        return result;
  }
    
    public String saveFields(){
    	String out = "";
    	
    	Iterator it = fields.iterator();
    	boolean isFirst = true;
    	while(it.hasNext()){
    		if(!isFirst){out+="|";}
    		Cols p = (Cols) it.next();
    		out +=  p.getFirstName()+"-"+p.getNumber();
            isFirst = false;
    	}
    	return out;
    }

    public void openFields(String in){
        String[] strLine = in.split("[|]");
        int len = strLine.length;
        if(len>0){
        	fields = new ArrayList();
        	for(int i = 0; i<len; i++){
        		String[] S = strLine[i].split("-");
        		Cols P = new Cols();
        		if(S.length == 1){
        			P.setFirstName(S[0]);
        		}
        		else if(S.length ==2){
        			P.setFirstName(S[0]);
        			P.setNumber(S[1]);
        		}
        		fields.add(P);
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

/*    public void openOutTables(String in){
    	String[] strLine = in.split("[|]");
    	int len = strLine.length;
    	if(len>0){
    		outTables = new String[len];
    		for(int i = 0; i<len; i++){
    			outTables[i] = strLine[i];
    		}
    	}
    }
 */
    
    
    @Override
    public void loadXML(Node node, List<DatabaseMeta> list, List<SlaveServer> list1, Repository rpstr) throws KettleXMLException {
        try {
            super.loadXML(node, list, list1);
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "name")) != null)
                setName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "name")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")) != null)
                setDatasetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")));
                        
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "normList")) != null)
                setnormList(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "normList")));
            
            //if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "percentileSettings")) != null)
              //  openPercentile(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "percentileSettings")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "fields")) != null)
                openFields(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "fields")));
            
            //if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outTables")) != null)
            	//openOutTables(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outTables")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "output_name")) != null)
                setOutputName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "output_name")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "label")) != null)
                setLabel(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "label")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "persist_Output_Checked")) != null)
                setPersistOutputChecked(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "persist_Output_Checked")));
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "defJobName")) != null)
                setDefJobName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "defJobName")));
            
            String[] S = normList.split("#");
        	this.outTables = new String[S.length];        	
        	for(int i = 0; i<S.length; i++){
        		String[] s = S[i].split("-");
        		this.outTables[i] = s[0]+"_"+getName();
        	}
            
        } catch (Exception e) {
            throw new KettleXMLException("ECL Dataset Job Plugin Unable to read step info from XML node", e);
        }

    }

    public String getXML() {
        String retval = "";
        
        retval += super.getXML();
        retval += "		<name><![CDATA[" + Name + "]]></name>" + Const.CR;
        retval += "		<fields><![CDATA[" + this.saveFields() + "]]></fields>" + Const.CR;
        retval += "		<normList><![CDATA[" + this.getnormList() + "]]></normList>" + Const.CR;
        retval += "		<dataset_name><![CDATA[" + DatasetName + "]]></dataset_name>" + Const.CR;
        //retval += "		<percentileSettings><![CDATA[" + this.savePercentile() + "]]></percentileSettings>" + Const.CR;
        for(int i = 0; i<saveOutTables().split("[|]").length; i++){
        	retval += "		<outTables eclIsGraphable=\"true\"><![CDATA[" + saveOutTables().split("[|]")[i] + "]]></outTables>" + Const.CR;
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
        	if(rep.getStepAttributeString(id_jobentry, "Name") != null)
                Name = rep.getStepAttributeString(id_jobentry, "Name"); //$NON-NLS-1$
        	
        	if(rep.getStepAttributeString(id_jobentry, "datasetName") != null)
                DatasetName = rep.getStepAttributeString(id_jobentry, "datasetName"); //$NON-NLS-1$
            
            if(rep.getStepAttributeString(id_jobentry, "normList") != null)
                this.setnormList(rep.getStepAttributeString(id_jobentry, "normList")); //$NON-NLS-1$
            
          //  if(rep.getStepAttributeString(id_jobentry, "percentileSettings") != null)
            //    this.openPercentile(rep.getStepAttributeString(id_jobentry, "percentileSettings")); //$NON-NLS-1$
            
            if(rep.getStepAttributeString(id_jobentry, "fields") != null)
                this.openFields(rep.getStepAttributeString(id_jobentry, "fields")); //$NON-NLS-1$
            
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
        	
        	rep.saveStepAttribute(id_job, getObjectId(), "Name", Name); //$NON-NLS-1$
        	
        	rep.saveStepAttribute(id_job, getObjectId(), "normList", this.getnormList()); //$NON-NLS-1$
        	
        	//rep.saveStepAttribute(id_job, getObjectId(), "percentileSettings", this.savePercentile()); //$NON-NLS-1$
        	
        	rep.saveStepAttribute(id_job, getObjectId(), "fields", this.saveFields()); //$NON-NLS-1$
        	for(int i = 0; i<saveOutTables().split("[|]").length; i++)
        		rep.saveStepAttribute(id_job, getObjectId(), "outTables", this.saveOutTables().split("[|]")[i]); //$NON-NLS-1$
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
