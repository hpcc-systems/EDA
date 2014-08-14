/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.eclcorrelation;

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
import org.hpccsystems.javaecl.Filter;

/**
 *
 * @author KeshavS
 */
public class ECLCorrelation extends ECLJobEntry{//extends JobEntryBase implements Cloneable, JobEntryInterface {
    
    private String datasetName = "";
    private ArrayList<Player> fields = new ArrayList<Player>();
    private String method = "";
   
    private String rule = "";
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
    
    public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

	public void setFields(ArrayList<Player> fields){
		this.fields = fields;
	}
	
	public ArrayList<Player> getFields(){
		return fields;
	}

    @Override
    public Result execute(Result prevResult, int k) throws KettleException {
    	Result result = prevResult;
        if(result.isStopped()){
        	return result;
        }
        else{	
        	String ecl = "";int idx = 1;String Rec = "";String choose = "";String valChoose = "";int cnt = 0;
        	for(Iterator<Player> it = fields.iterator(); it.hasNext();){
        		Player P = (Player) it.next();
        		String S = P.getFirstName();
        		String rule = P.getRule();
        		boolean first = true;
        		for(Iterator<Player> it1 = fields.iterator(); it1.hasNext();){
        			if(idx < fields.size()){
        				Player P1 = (Player) it1.next();
	        			if(first){
		        			for(int i = 0; i<idx; i++){
		        				P1 = (Player) it1.next();        				
		        			}
		        			first = false;
	        			}
	            		String S1 = P1.getFirstName();
	            		String rule1 = P1.getRule();
	            		if(!rule.equals("") && !rule1.equals("")){
	            			Rec += S+"_"+S1+"_CORR := CORRELATION(GROUP,"+S+","+S1+","+rule+"AND "+rule1+"),\n";
	            			Rec += S+"_"+S1+"_RECS_USED := COUNT(GROUP,"+rule+"AND "+rule1+"),\n";
	            		}
	            		if(rule.equals("") && !rule1.equals("")){
	            			Rec += S+"_"+S1+"_CORR := CORRELATION(GROUP,"+S+","+S1+","+rule1+"),\n";
	            			Rec += S+"_"+S1+"_RECS_USED := COUNT(GROUP,"+rule1+"),\n";
	            		}
	            		if(!rule.equals("") && rule1.equals("")){
	            			Rec += S+"_"+S1+"_CORR := CORRELATION(GROUP,"+S+","+S1+","+rule+"),\n";
	            			Rec += S+"_"+S1+"_RECS_USED := COUNT(GROUP,"+rule+"),\n";
	            		}
	            		if(rule.equals("") && rule1.equals("")){
	            			Rec += S+"_"+S1+"_CORR := CORRELATION(GROUP,"+S+","+S1+"),\n";
	            			Rec += S+"_"+S1+"_RECS_USED := COUNT(GROUP),\n";
	            		}
	            		choose += "'"+S+"_"+S1+"_CORR','"+S+"_"+S1+"_RECS_USED',";
	            		valChoose += "LEFT."+S+"_"+S1+"_CORR,LEFT."+S+"_"+S1+"_RECS_USED,";
	            		cnt++;            		
        			}
        		}
        		idx++;
        		if(idx == fields.size())
        			break;
        	}
        	Rec = Rec.substring(0,Rec.length()-2);
        	choose = choose.substring(0,choose.length()-1);
        	valChoose = valChoose.substring(0,valChoose.length()-1);
        	
        	ecl += "Tbl := TABLE("+getDatasetName()+",{"+Rec+"});\n";
        	ecl += "MyRec := RECORD\n	STRING Fields;\n	REAL Val;\nEND;\n";
        	ecl += "MyCorr := NORMALIZE(Tbl,"+cnt*2+",TRANSFORM(MyRec,SELF.Fields:=CHOOSE(counter,"+choose+"),SELF.Val:=CHOOSE(counter,"+valChoose+")));\n";
        	ecl += "OUTPUT(MyCorr,NAMED('Correlation'));\n";
        	
        	logBasic(ecl);
        	
        	result.setResult(true);
	        
	        RowMetaAndData data = new RowMetaAndData();
	        data.addValue("ecl", Value.VALUE_TYPE_STRING, ecl);
	        
	        
	        List list = result.getRows();
	        list.add(data);
	        String eclCode = parseEclFromRowData(list);
	        result.setRows(list);
	        result.setLogText("ECLCorrelation executed, ECL code added");
        	return result;
        }
    }
    
    public String saveFields(){
    	String out = "";
    	
    	Iterator it = fields.iterator();
    	boolean isFirst = true;
    	while(it.hasNext()){
    		if(!isFirst){out+="|";}
    		Player p = (Player) it.next();
    		out +=  p.getFirstName()+","+p.getRule();
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
        		String[] S = strLine[i].split(",");
        		Player P = new Player();
        		P.setFirstName(S[0]);
        		if(S.length == 1)
        			P.setRule("");
        		else
        			P.setRule(S[1]);
        		fields.add(P);
        	}
        }
    }

    
    
    
    
    @Override
    public void loadXML(Node node, List<DatabaseMeta> list, List<SlaveServer> list1, Repository rpstr) throws KettleXMLException {
        try {
            super.loadXML(node, list, list1);
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")) != null)
                setDatasetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "method")) != null)
                setMethod(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "method")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "fields")) != null)
                openFields(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "fields")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "rule")) != null)
                setRule(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "rule")));
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
      
        retval += "		<method><![CDATA[" + method + "]]></method>" + Const.CR;
        retval += "		<fields><![CDATA[" + this.saveFields() + "]]></fields>" + Const.CR;
        retval += "		<dataset_name><![CDATA[" + datasetName + "]]></dataset_name>" + Const.CR;
        retval += "		<rule><![CDATA[" + rule + "]]></rule>" + Const.CR;
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
            if(rep.getStepAttributeString(id_jobentry, "method") != null)
                method = rep.getStepAttributeString(id_jobentry, "method"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "fields") != null)
                this.openFields(rep.getStepAttributeString(id_jobentry, "fields")); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "rule") != null)
                rule = rep.getStepAttributeString(id_jobentry, "rule"); //$NON-NLS-1$
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
            rep.saveStepAttribute(id_job, getObjectId(), "fields", this.saveFields()); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "method", method); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "rule", rule); //$NON-NLS-1$
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
