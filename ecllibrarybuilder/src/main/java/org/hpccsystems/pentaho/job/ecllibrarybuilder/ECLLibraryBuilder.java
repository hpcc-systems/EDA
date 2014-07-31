/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.ecllibrarybuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hpccsystems.ecljobentrybase.ECLJobEntry;
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

/**
 *
 * @author KeshavS
 */
public class ECLLibraryBuilder extends ECLJobEntry{//extends JobEntryBase implements Cloneable, JobEntryInterface {
    
    private String libraryName = "";
    private String[] libValues;
    private String[] libCombos; 
    private ArrayList<String> entries = new ArrayList<String>();
    private String code = "";
    private String txtVals = "";
    private String[] outVals;        
    
    
    public String[] getOutVals() {
		return outVals;
	}

	public void setOutVals(String[] outVals) {
		this.outVals = outVals;
	}

	public String getTxtVals() {
		return txtVals;
	}

	public void setTxtVals(String txtVals) {
		this.txtVals = txtVals;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ArrayList<String> getEntries() {
		return entries;
	}

	public void setEntries(ArrayList<String> entries) {
		this.entries = entries;
	}

	public String[] getLibValues() {
		return libValues;
	}

	public void setLibValues(String[] libValues) {
		this.libValues = libValues;
	}

	public String[] getLibCombos() {
		return libCombos;
	}

	public void setLibCombos(String[] libCombos) {
		this.libCombos = libCombos;
	}

	public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

        @Override
    public Result execute(Result prevResult, int k) throws KettleException {
    	Result result = prevResult;
        if(result.isStopped()){
        	return result;
        }
        else{
        	
        	String Library = "";
        	Library += "IMPORT addCounter;\n";
        	/*for(int i = Integer.parseInt(getTxtVals()); i<libValues.length; i++){
        		outVals[i] = libValues[i];
        	}*/
        	String[] S = code.split(";");
        	for(int i = 0; i<S.length; i++){
        		//logBasic(S[i]);
        		Library += Replace(S[i],libValues,libCombos,entries) + ";\n";
        	}
        	
            logBasic("Library Builder Job =" + Library); 
	        
	        result.setResult(true);
	        
	        RowMetaAndData data = new RowMetaAndData();
	        data.addValue("ecl", Value.VALUE_TYPE_STRING, Library);
	        
	        
	        List list = result.getRows();
	        list.add(data);
	        String eclCode = parseEclFromRowData(list);
	        result.setRows(list);
	        result.setLogText("ECLRandom executed, ECL code added");
	        return result;
        }
        
        
    }
        
        public String saveLibValues(){
        	String out = "";int i = 0;    	
        	boolean isFirst = true;
        	if(libValues!=null){
    	    	while(i < libValues.length){
    	    		if(!isFirst){out+="|";}
    	    		
    	    		out +=  libValues[i];
    	    		i++;
    	            isFirst = false;
    	    	}
        	}
        	return out;
        }

        public void openLibValues(String in){
        	String[] strLine = in.split("[|]");
        	int len = strLine.length;
        	if(len>0){
        		libValues = new String[len];
        		for(int i = 0; i<len; i++){
        			libValues[i] = strLine[i];
        		}
        	}
        }
        
        public String saveOutVals(){
        	String out = "";int i = 0;    	
        	boolean isFirst = true;
        	if(outVals!=null){
    	    	while(i < outVals.length){
    	    		if(!isFirst){out+="|";}
    	    		
    	    		out +=  outVals[i];
    	    		i++;
    	            isFirst = false;
    	    	}
        	}
        	return out;
        }

        public void openOutVals(String in){
        	String[] strLine = in.split("[|]");
        	int len = strLine.length;
        	if(len>0){
        		outVals = new String[len];
        		for(int i = 0; i<len; i++){
        			outVals[i] = strLine[i];
        		}
        	}
        }

        public String saveLibCombos(){
        	String out = "";int i = 0;    	
        	boolean isFirst = true;
        	if(libCombos!=null){
    	    	while(i < libCombos.length){
    	    		if(!isFirst){out+="|";}
    	    		
    	    		out +=  libCombos[i];
    	    		i++;
    	            isFirst = false;
    	    	}
        	}
        	return out;
        }

        public void openLibCombos(String in){
        	String[] strLine = in.split("[|]");
        	int len = strLine.length;
        	if(len>0){
        		libCombos = new String[len];
        		for(int i = 0; i<len; i++){
        			libCombos[i] = strLine[i];
        		}
        	}
        }

        public String saveEntries(){
        	String out = ""; 	
        	Iterator<String> it = entries.iterator();
        	boolean isFirst = true;
        	while(it.hasNext()){
        		if(!isFirst){out+="|";}
        		String p = (String) it.next();
        		out +=  p;
                isFirst = false;
        	}
        	return out;
        }
        
        public void openEntries(String in){
            String[] strLine = in.split("[|]");
            int len = strLine.length;
            if(len>0){
            	entries = new ArrayList<String>();
            	for(int i = 0; i<len; i++){
            		String S = strLine[i]; 
            		entries.add(S);
            	}
            }
        }
        
        
    @Override
    public void loadXML(Node node, List<DatabaseMeta> list, List<SlaveServer> list1, Repository rpstr) throws KettleXMLException {
        try {
            super.loadXML(node, list, list1);
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "library_name")) != null)
                setLibraryName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "library_name")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "txtVals")) != null)
                setTxtVals(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "txtVals")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "Code")) != null)
                setCode(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "Code")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "libValues")) != null)
                openLibValues(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "libValues")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "libCombos")) != null)
                openLibCombos(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "libCombos")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outVals")) != null)
                openOutVals(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outVals")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "Entries")) != null)
                openEntries(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "Entries")));
            
            
        } catch (Exception e) {
            throw new KettleXMLException("ECL Dataset Job Plugin Unable to read step info from XML node", e);
        }

    }

    public String getXML() {
        String retval = "";
        
        retval += super.getXML();
      
        retval += "		<library_name ><![CDATA[" + libraryName + "]]></library_name>" + Const.CR;
        retval += "		<Code ><![CDATA[" + code + "]]></Code>" + Const.CR;
        retval += "		<txtVals><![CDATA[" + txtVals + "]]></txtVals>" + Const.CR;
        
        retval += "		<outVals eclIsDef=\"true\" eclType=\"dataset\"><![CDATA[" + saveOutVals() + "]]></outVals>" + Const.CR;
        
        retval += "     <libValues><![CDATA[" + this.saveLibValues() + "]]></libValues>" + Const.CR;
        retval += "     <libCombos><![CDATA[" + this.saveLibCombos() + "]]></libCombos>" + Const.CR;
        retval += "     <Entries><![CDATA[" + this.saveEntries() + "]]></Entries>" + Const.CR;
        return retval;

    }

    public void loadRep(Repository rep, ObjectId id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
            throws KettleException {
        try {
            if(rep.getStepAttributeString(id_jobentry, "libraryName") != null)
            	libraryName = rep.getStepAttributeString(id_jobentry, "libraryName"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "Code") != null)
            	code = rep.getStepAttributeString(id_jobentry, "Code"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "txtVals") != null)
            	txtVals = rep.getStepAttributeString(id_jobentry, "txtVals"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "libValues") != null)
            	this.openLibValues(rep.getStepAttributeString(id_jobentry, "libValues")); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "libCombos") != null)
            	this.openLibCombos(rep.getStepAttributeString(id_jobentry, "libCombos")); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "outVals") != null)
            	this.openOutVals(rep.getStepAttributeString(id_jobentry, "outVals")); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "Entries") != null)
            	this.openEntries(rep.getStepAttributeString(id_jobentry, "Entries")); //$NON-NLS-1$
            
        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }

    public void saveRep(Repository rep, ObjectId id_job) throws KettleException {
        try {
            rep.saveStepAttribute(id_job, getObjectId(), "libraryName", libraryName); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "Code", code); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "txtVals", txtVals); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "libValues", this.saveLibValues()); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "libCombos", this.saveLibCombos()); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "outVals", this.saveOutVals()); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "Entries", this.saveEntries()); //$NON-NLS-1$
            
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
    
    public String Replace(String line,String[] libValues, String[] libCombos, ArrayList<String> Entries){
    	int cnt = 0;ArrayList<String> replace = new ArrayList<String>();
    	for(int i = 0; i<line.length(); i++){
    		char ch = line.charAt(i);
    		if(ch == '<')
    			cnt++;    		
    		if(cnt == 3){
    			i = i + 1;
    			String S = "";;
    			while(line.charAt(i)!='>'){
    				S += ""+line.charAt(i);
    				i++;
    			}
    			replace.add(S);
    			i = i + 2;
    			cnt = 0;
    		}
    	}
    	for(Iterator<String> it = replace.iterator(); it.hasNext();){
    		String S = (String) it.next();
    		int idx = Entries.indexOf(S);
    		if(idx<libValues.length){
    			line = line.replace(S, libValues[idx]);
    		}
    		else if(idx >= libValues.length && idx < libValues.length+libCombos.length){
    			line = line.replace(S, libCombos[idx-libValues.length]);
    		}
    		else{
    			line = line.replace(S,libraryName);
    		}
        	logBasic(line);

    	}
    	line = line.replace("<<<","");
    	line = line.replace(">>>","");
    	
    	return line;
    }
    
}
