package org.hpccsystems.sortui.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SortColumnRecordList {
	
	List<SortColumnRecord> listSortColumn = null;
	private Set<ISortColumnViewer> changeListeners = new HashSet<ISortColumnViewer>();
	private String delim = "<>";
	
	public SortColumnRecordList() {
		listSortColumn = new ArrayList<SortColumnRecord>();
		//initData();
	}
	public SortColumnRecordList(String inData, List<String> listDatasetVals){
		listSortColumn = new ArrayList<SortColumnRecord>();
		openFromString(inData);
		//initTestData();
		modifiedSortColumnList(listDatasetVals);
	}
	//adds any new dataset fields
	public void modifiedSortColumnList (List<String> listDatasetVals){
		
		Map<String, SortColumnRecord> mapFinalSortColumn = new HashMap<String, SortColumnRecord>();
		Map<String, SortColumnRecord> mapSortColumn = new HashMap<String, SortColumnRecord>();
		for (Iterator<SortColumnRecord> iterator = listSortColumn.iterator(); iterator.hasNext();) {
			SortColumnRecord rec = iterator.next();
			mapSortColumn.put(rec.getChildren(), rec);
		}
		
		for (Iterator<String> iterator = listDatasetVals.iterator(); iterator.hasNext();) {
			String val = iterator.next();
			if(mapSortColumn.containsKey(val)) {
				mapFinalSortColumn.put(val, mapSortColumn.get(val));
			} else {
				SortColumnRecord obj = new SortColumnRecord();
				obj.setChildren(val);
				obj.setDirection("ascending");
				mapFinalSortColumn.put(val, obj);
			}
		}
		
		Set<String> mapKeys = mapFinalSortColumn.keySet();
		int count = 0;
		listSortColumn = new ArrayList<SortColumnRecord>();
		for (Iterator<String> iterator = mapKeys.iterator(); iterator.hasNext();) {
			String key = iterator.next();
			SortColumnRecord objRec = mapFinalSortColumn.get(key);
			objRec.setCounter(count);
			
			//System.out.println(objRec.getChildren());
			//System.out.println(objRec.isSelect());
			
			listSortColumn.add(objRec);
			count++;
		}
	}

	public String saveListAsString(){
		String out = "";
		
		for(int i =0; i<listSortColumn.size(); i++){
			
			SortColumnRecord obj = listSortColumn.get(i);
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
				SortColumnRecord obj = new SortColumnRecord();
				
				obj.loadFromString(tokens[i]);
				listSortColumn.add(obj);
			}
		}
	}
	
	public String fromStringToXML(String in){
		String xml = "";
		if(!in.equals("")){
			
			String[] tokens = in.split("[" + delim + "]");
			for(int i = 0; i<tokens.length; i++){
				SortColumnRecord obj = new SortColumnRecord();
				xml += obj.fromStringToXML(tokens[i]);
				
			}
		}
		return xml;
	}
	
	/**
	 * Set the Table Data Here
	 */
	public void initTestData() {
		SortColumnRecord obj = new SortColumnRecord();
		obj.setChildren("ProductName");
		obj.setDirection("descending");
		obj.setCounter(1);
		obj.setSelect(true);
		listSortColumn.add(obj);
		
		obj = new SortColumnRecord();
		obj.setChildren("ProductCode");
		obj.setDirection("ascending");
		obj.setCounter(2);
		obj.setSelect(false);
		listSortColumn.add(obj);
		
		obj = new SortColumnRecord();
		obj.setChildren("ProductType");
		obj.setDirection("descending");
		obj.setCounter(3);
		obj.setSelect(true);
		listSortColumn.add(obj);
		
		obj = new SortColumnRecord();
		obj.setChildren("Test1");
		obj.setDirection("descending");
		obj.setCounter(4);
		obj.setSelect(true);
		listSortColumn.add(obj);
		
		obj = new SortColumnRecord();
		obj.setChildren("Test2");
		obj.setDirection("ascending");
		obj.setCounter(5);
		obj.setSelect(true);
		listSortColumn.add(obj);
	}
	
	public List<SortColumnRecord> getSortColumn() {
		return listSortColumn;
	}
	
	public void conceptChanged(SortColumnRecord record) {
		Iterator<ISortColumnViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			ISortColumnViewer conceptViewer = (iterator.next());
			conceptViewer.sortColumnChanged(record);
		}
	}
	
	/**
	 * @param viewer
	 */
	public void removeChangeListener(ISortColumnViewer viewer) {
		changeListeners.remove(viewer);
	}

	/**
	 * @param viewer
	 */
	public void addChangeListener(ISortColumnViewer viewer) {
		changeListeners.add(viewer);
	}
	
}
