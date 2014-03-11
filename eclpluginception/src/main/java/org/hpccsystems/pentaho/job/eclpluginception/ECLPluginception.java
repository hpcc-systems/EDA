/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.eclpluginception;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.pentaho.di.cluster.SlaveServer;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Result;
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
public class ECLPluginception extends ECLJobEntry{//extends JobEntryBase implements Cloneable, JobEntryInterface {
    
    private String datasetName = "";
    private ArrayList<String> items = new ArrayList<String>();
    private ArrayList<String> types = new ArrayList<String>();
    
    private String Text = "";
    private String derived = "";
    private String resultDataset = "";
    private String test = "";
    private String dir = "";
    
    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getText() {
        return Text;
    }

    public void setText(String Text) {
        this.Text = Text;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    public ArrayList<String> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }

    public String getresultDatasetName() {
        return resultDataset;
    }

    public void setresultDatasetName(String resultDataset) {
        this.resultDataset = resultDataset;
    }

    public String getDerived() {
        return derived;
    }

    public void setDerived(String derived) {
        this.derived = derived;
    }
    
    @Override
    public Result execute(Result prevResult, int k) throws KettleException {    	  
          return null;
          
    }
        
    public String saveItems(){
    	String out = "";
    	
    	Iterator<String> it = items.iterator();
    	boolean isFirst = true;
    	while(it.hasNext()){
    		if(!isFirst){out+="|";}
    		String p = it.next();
    		out +=  p;
            isFirst = false;
    	}
    	return out;
    }
    
    public void openItems(String in){
    	String[] strLine = in.split("[|]");
        int len = strLine.length;
        if(len>0){
        	items = new ArrayList<String>();
        	for(int i = 0; i<len; i++){        		
        		items.add(strLine[i]);
        	}
        }
    }

    public String saveTypes(){
    	String out = "";
    	
    	Iterator<String> it = types.iterator();
    	boolean isFirst = true;
    	while(it.hasNext()){
    		if(!isFirst){out+="|";}
    		String p = it.next();
    		out +=  p;
            isFirst = false;
    	}
    	return out;
    }
    
    public void openTypes(String in){
    	String[] strLine = in.split("[|]");
        int len = strLine.length;
        if(len>0){
        	types = new ArrayList<String>();
        	for(int i = 0; i<len; i++){        		
        		types.add(strLine[i]);
        	}
        }
    }

    @Override
    public void loadXML(Node node, List<DatabaseMeta> list, List<SlaveServer> list1, Repository rpstr) throws KettleXMLException {
        try {
            super.loadXML(node, list, list1);
            
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")) != null)
                setDatasetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "resultDataset")) != null)
                setresultDatasetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "resultDataset")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "Derived")) != null)
                setDerived(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "Derived")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "text")) != null)
                setText(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "text")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "test")) != null)
                setTest(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "test")));
            if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dir")) != null)
                setDir(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dir")));
            //if(items != null){
            	if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "items")) != null)
            		this.openItems(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "items")));//
            //}
            //if(types != null){
            	if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "types")) != null)
            		openTypes(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "types")));
            //}
            
        } catch (Exception e) {
            throw new KettleXMLException("ECL Dataset Job Plugin Unable to read step info from XML node", e);
        }

    }

    public String getXML() {
        String retval = "";
        
        retval += super.getXML();
      
        retval += "		<dataset_name ><![CDATA[" + datasetName + "]]></dataset_name>" + Const.CR;
        retval += "		<resultdataset eclIsGraphable=\"true\"><![CDATA[" + resultDataset + "]]></resultdataset>" + Const.CR;
        retval += "		<Derived><![CDATA[" + derived + "]]></Derived>" + Const.CR;
        retval += "		<text><![CDATA[" + Text + "]]></text>" + Const.CR;
        retval += "		<test><![CDATA[" + test + "]]></test>" + Const.CR;
        retval += "		<dir><![CDATA[" + dir + "]]></dir>" + Const.CR;
        retval += "		<items><![CDATA[" + this.saveItems() + "]]></items>" + Const.CR;
        retval += "		<types><![CDATA[" + this.saveTypes() + "]]></types>" + Const.CR;
        return retval;

    }

    public void loadRep(Repository rep, ObjectId id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
            throws KettleException {
        try {
            if(rep.getStepAttributeString(id_jobentry, "datasetName") != null)
                datasetName = rep.getStepAttributeString(id_jobentry, "datasetName"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "resultdataset") != null)
                resultDataset = rep.getStepAttributeString(id_jobentry, "resultdataset"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "Derived") != null)
                derived = rep.getStepAttributeString(id_jobentry, "Derived"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "text") != null)
                Text = rep.getStepAttributeString(id_jobentry, "text"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "test") != null)
                test = rep.getStepAttributeString(id_jobentry, "test"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "dir") != null)
                dir = rep.getStepAttributeString(id_jobentry, "dir"); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "items") != null)
                this.openItems(rep.getStepAttributeString(id_jobentry, "items")); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_jobentry, "types") != null)
                this.openTypes(rep.getStepAttributeString(id_jobentry, "types")); //$NON-NLS-1$
            
        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }

    public void saveRep(Repository rep, ObjectId id_job) throws KettleException {
        try {
            rep.saveStepAttribute(id_job, getObjectId(), "datasetName", datasetName); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "resultdataset", resultDataset); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "Derived", derived); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "text", Text); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "test", test); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "dir", dir); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "items", this.saveItems()); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "types", this.saveTypes()); //$NON-NLS-1$
            
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
