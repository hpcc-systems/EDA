package org.hpccsystems.saltui.hygiene;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class HygieneEntryList {
	private ArrayList<HygieneEntryBO> entries = new ArrayList<HygieneEntryBO>();
	private Set<IHygieneEntryListViewer> changeListeners = new HashSet<IHygieneEntryListViewer>();

	public ArrayList<HygieneEntryBO> getEntries() {
		return entries;
	}

	public void setEntries(ArrayList<HygieneEntryBO> entries) {
		this.entries = entries;
	}
	
	public void add(HygieneEntryBO eb){
		entries.add(eb);
	}
	
	public HygieneEntryBO getEntry(int index){
		return entries.get(index);
	}
	
	public void updateEntry(int index, HygieneEntryBO r){
		System.out.println("Update Entry: " + index + " - " + r.getField());
		entries.set(index, r);
	}
	
	//this just updates the name stored to the entry for quick reference
	//this doesn't update indexes except if newName is "" then it assumes it was
	//deleted
	public void updateAll(String newName, String oldName){
		for(int i = 0; i<entries.size();i++){
			if(entries.get(i).getRuleName().equalsIgnoreCase(oldName)){
				entries.get(i).setRuleName(newName);
				if(newName.equals("")){
					entries.get(i).setHygieneRuleListIndex(-1);
				}
				System.out.println("Updating NewName: " + newName + " OldName: " + oldName);
			}//else{
			//	System.out.println("No Update: " + oldName);
			//}
			
		}
	}
	
	//Add a new Record to the existing list
	public void addEntry(int index) {
		HygieneEntryBO entry = new HygieneEntryBO();
		if(index >= 0){
			entries.add(index+1, entry);
		} else {
			entries.add(entries.size(), entry);
		}
		Iterator<IHygieneEntryListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			iterator.next().addEntry(entry);
		}
	}
	
	public int containsEntry(String name){
		int index = -1;
		//System.out.println("Looking for: " + name);
		for(int i = 0; i<entries.size();i++){
			//System.out.println(entries.get(i).getRuleName() + " vs " + name);
			if(entries.get(i).getRuleName().equalsIgnoreCase(name)){
				index = i;
			}//else{
			//	System.out.println("No Update: " + oldName);
			//}
			
		}
		
		return index;
	}

	public void removeEntry(int index) {
		if(entries.size()>index){
			entries.remove(index);
			Iterator<IHygieneEntryListViewer> iterator = changeListeners.iterator();
			while (iterator.hasNext())
				iterator.next().removeEntry(index);
		}
	}

	public void modifyEntry(HygieneEntryBO record) {
		Iterator<IHygieneEntryListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			iterator.next().modifyEntry(record);
	}
	
	public void removeChangeListener(IHygieneEntryListViewer viewer) {
		changeListeners.remove(viewer);
	}

	public void addChangeListener(IHygieneEntryListViewer viewer) {
		changeListeners.add(viewer);
	}
        
     public void addEntryBO(HygieneEntryBO r){
    	entries.add(entries.size(), r);
        Iterator<IHygieneEntryListViewer> iterator = changeListeners.iterator();
        while (iterator.hasNext()){
                iterator.next().addEntry(r);
        }
    }
}
