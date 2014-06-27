package org.hpccsystems.recordlayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class RecordList {

	private ArrayList<RecordBO> arlRecords = new ArrayList<RecordBO>();
	private Set<IRecordListViewer> changeListeners = new HashSet<IRecordListViewer>();
	
	// Combo box choices
	static final String[] COLUMN_TYPE_ARRAY = {"Select", 
		"BOOLEAN",
		"DATA",
		"DATASET",
		"DECIMAL",
		"INTEGER", 
		"QSTRING",
		"REAL", 
		"STRING",
		"SET OF BOOLEAN",
		"SET OF DATA",
		"SET OF INTEGER",
		"SET OF REAL",
		"SET OF STRING",
		"SET OF UNICODE",
		"UNICODE", 
		"UNSIGNED INTEGER",
		"VARSTRING", 
		"VARUNICODE"
		};
	static final String[] COLUMN_SORT_ORDER_ARRAY = {"ASCENDING", 
		"DESENDING"
		};
	//following will need more hlep
	/*
	 TYPEOF
	 RECORDOF
	 ENUM
	 */
		
	
	public RecordList(){
		super();
	}
	
	//Returns the values used in Column Type Combo Box
		public String[] getColSortOrder() {
			return COLUMN_SORT_ORDER_ARRAY;
		}
		
	//Returns the values used in Column Type Combo Box
	public String[] getColTypes() {
		return COLUMN_TYPE_ARRAY;
	}
	
	//Returns the Record List
	public ArrayList<RecordBO> getRecords() {
		return arlRecords;
	}
	
	//Add a new Record to the existing list
	public void addRecord(int index) {
		RecordBO record = new RecordBO();
		if(index >= 0){
			arlRecords.add(index+1, record);
		} else {
			arlRecords.add(arlRecords.size(), record);
		}
		Iterator<IRecordListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			iterator.next().addRecord(record);
		}
	}

	public void removeRecord(int index) {
		arlRecords.remove(index);
		Iterator<IRecordListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			iterator.next().removeRecord(index);
	}

	public void modifyRecord(RecordBO record) {
		Iterator<IRecordListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			iterator.next().modifyRecord(record);
	}
	
	public void removeChangeListener(IRecordListViewer viewer) {
		changeListeners.remove(viewer);
	}

	public void addChangeListener(IRecordListViewer viewer) {
		changeListeners.add(viewer);
	}
        
     public void addRecordBO(RecordBO r){
        arlRecords.add(arlRecords.size(), r);
        Iterator<IRecordListViewer> iterator = changeListeners.iterator();
        while (iterator.hasNext()){
                iterator.next().addRecord(r);
        }
    }
}
