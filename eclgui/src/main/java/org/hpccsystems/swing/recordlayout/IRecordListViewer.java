package org.hpccsystems.swing.recordlayout;

public interface IRecordListViewer {
	
	//Update the view after the record has been added to record list
	public void addRecord(RecordBO record);
	
	//Update the view after the record has been removed from record list
	public void removeRecord(int index);
	
	//Update the view after the record has been modified in the record list
	public void modifyRecord(RecordBO record);
}
