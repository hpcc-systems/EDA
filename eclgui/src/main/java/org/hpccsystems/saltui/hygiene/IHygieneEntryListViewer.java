package org.hpccsystems.saltui.hygiene;

public interface IHygieneEntryListViewer {
	
	//Update the view after the record has been added to record list
	public void addEntry(HygieneEntryBO entry);
	
	//Update the view after the record has been removed from record list
	public void removeEntry(int index);
	
	//Update the view after the record has been modified in the record list
	public void modifyEntry(HygieneEntryBO entry);
}
