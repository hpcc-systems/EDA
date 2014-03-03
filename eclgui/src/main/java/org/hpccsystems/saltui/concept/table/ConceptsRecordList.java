package org.hpccsystems.saltui.concept.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConceptsRecordList {
	
	List<ConceptsRecord> listConcepts = null;
	private Set<IConceptsViewer> changeListeners = new HashSet<IConceptsViewer>();
	private String delim = "<>";
	
	public ConceptsRecordList() {
		listConcepts = new ArrayList<ConceptsRecord>();
		//initData();
	}
	public ConceptsRecordList(String inData, List<String> listDatasetVals){
		listConcepts = new ArrayList<ConceptsRecord>();
		openFromString(inData);
		//initTestData();
		modifiedConceptsList(listDatasetVals);
	}
	//adds any new dataset fields
	public void modifiedConceptsList (List<String> listDatasetVals){
		
		Map<String, ConceptsRecord> mapFinalConcepts = new HashMap<String, ConceptsRecord>();
		Map<String, ConceptsRecord> mapConcepts = new HashMap<String, ConceptsRecord>();
		for (Iterator<ConceptsRecord> iterator = listConcepts.iterator(); iterator.hasNext();) {
			ConceptsRecord rec = iterator.next();
			mapConcepts.put(rec.getChildren(), rec);
		}
		
		for (Iterator<String> iterator = listDatasetVals.iterator(); iterator.hasNext();) {
			String val = iterator.next();
			if(mapConcepts.containsKey(val)) {
				mapFinalConcepts.put(val, mapConcepts.get(val));
			} else {
				ConceptsRecord obj = new ConceptsRecord();
				obj.setChildren(val);
				obj.setNonNull(false);
				mapFinalConcepts.put(val, obj);
			}
		}
		
		Set<String> mapKeys = mapFinalConcepts.keySet();
		int count = 0;
		listConcepts = new ArrayList<ConceptsRecord>();
		for (Iterator<String> iterator = mapKeys.iterator(); iterator.hasNext();) {
			String key = iterator.next();
			ConceptsRecord objRec = mapFinalConcepts.get(key);
			objRec.setCounter(count);
			
			//System.out.println(objRec.getChildren());
			//System.out.println(objRec.isSelect());
			
			listConcepts.add(objRec);
			count++;
		}
	}

	public String saveListAsString(){
		String out = "";
		
		for(int i =0; i<listConcepts.size(); i++){
			
			ConceptsRecord obj = listConcepts.get(i);
			if(!out.equals("")){
				out += delim;
			}
			out += obj.saveAsString();
		}
		
		return out;
	}
	
	public void openFromString(String in){
		if(!in.equals("")){
			
			String[] tokens = in.split("[" + delim + "]");
			for(int i = 0; i<tokens.length; i++){
				ConceptsRecord obj = new ConceptsRecord();
				
				obj.loadFromString(tokens[i]);
				listConcepts.add(obj);
			}
		}
	}
	
	public String fromStringToXML(String in){
		String xml = "";
		if(!in.equals("")){
			
			String[] tokens = in.split("[" + delim + "]");
			for(int i = 0; i<tokens.length; i++){
				ConceptsRecord obj = new ConceptsRecord();
				xml += obj.fromStringToXML(tokens[i]);
				
			}
		}
		return xml;
	}
	
	/**
	 * Set the Table Data Here
	 */
	public void initTestData() {
		ConceptsRecord obj = new ConceptsRecord();
		obj.setChildren("ProductName");
		obj.setNonNull(true);
		obj.setCounter(1);
		obj.setSelect(true);
		listConcepts.add(obj);
		
		obj = new ConceptsRecord();
		obj.setChildren("ProductCode");
		obj.setNonNull(false);
		obj.setCounter(2);
		obj.setSelect(false);
		listConcepts.add(obj);
		
		obj = new ConceptsRecord();
		obj.setChildren("ProductType");
		obj.setNonNull(true);
		obj.setCounter(3);
		obj.setSelect(true);
		listConcepts.add(obj);
		
		obj = new ConceptsRecord();
		obj.setChildren("Test1");
		obj.setNonNull(true);
		obj.setCounter(4);
		obj.setSelect(true);
		listConcepts.add(obj);
		
		obj = new ConceptsRecord();
		obj.setChildren("Test2");
		obj.setNonNull(false);
		obj.setCounter(5);
		obj.setSelect(true);
		listConcepts.add(obj);
	}
	
	public List<ConceptsRecord> getConcepts() {
		return listConcepts;
	}
	
	public void conceptChanged(ConceptsRecord record) {
		Iterator<IConceptsViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			IConceptsViewer conceptViewer = (iterator.next());
			conceptViewer.conceptChanged(record);
		}
	}
	
	/**
	 * @param viewer
	 */
	public void removeChangeListener(IConceptsViewer viewer) {
		changeListeners.remove(viewer);
	}

	/**
	 * @param viewer
	 */
	public void addChangeListener(IConceptsViewer viewer) {
		changeListeners.add(viewer);
	}
	
}
