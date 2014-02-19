package org.hpccsystems.saltui.concept;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ConceptEntryList {
	private ArrayList<ConceptEntryBO> entries = new ArrayList<ConceptEntryBO>();
	private Set<IConceptEntryListViewer> changeListeners = new HashSet<IConceptEntryListViewer>();

	public ArrayList<ConceptEntryBO> getEntries() {
		return entries;
	}

	public void setEntries(ArrayList<ConceptEntryBO> entries) {
		this.entries = entries;
	}
	
	public void add(ConceptEntryBO eb){
		entries.add(eb);
	}
	
	public ConceptEntryBO getEntry(int index){
		return entries.get(index);
	}
	
	public void updateEntry(int index, ConceptEntryBO r){
		
		entries.set(index, r);
	}
	
	//this just updates the name stored to the entry for quick reference
	//this doesn't update indexes except if newName is "" then it assumes it was
	//deleted
	public void updateAll(String newName, String oldName){
		for(int i = 0; i<entries.size();i++){
			if(entries.get(i).getConceptName().equalsIgnoreCase(oldName)){
				entries.get(i).setConceptName(newName);
				if(newName.equals("")){
					entries.get(i).setEntryIndex(-1);
				}
				System.out.println("Updating NewName: " + newName + " OldName: " + oldName);
			}//else{
			//	System.out.println("No Update: " + oldName);
			//}
			
		}
	}
	
	//Add a new Record to the existing list
	public void addEntry(int index) {
		ConceptEntryBO entry = new ConceptEntryBO();
		
		if(index >= 0){
			entries.add(index+1, entry);
		} else {
			
			entries.add(entries.size(), entry);
		}
		Iterator<IConceptEntryListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			iterator.next().addEntry(entry);
		}
	}
	
	public int containsEntry(String name){
		int index = -1;
		//System.out.println("Looking for: " + name);
		for(int i = 0; i<entries.size();i++){
			//System.out.println(entries.get(i).getRuleName() + " vs " + name);
			if(entries.get(i).getConceptName().equalsIgnoreCase(name)){
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
			Iterator<IConceptEntryListViewer> iterator = changeListeners.iterator();
			while (iterator.hasNext())
				iterator.next().removeEntry(index);
		}
	}

	public void modifyEntry(ConceptEntryBO record) {
		Iterator<IConceptEntryListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			iterator.next().modifyEntry(record);
	}
	
	public void removeChangeListener(IConceptEntryListViewer viewer) {
		changeListeners.remove(viewer);
	}

	public void addChangeListener(IConceptEntryListViewer viewer) {
		changeListeners.add(viewer);
	}
        
     public void addEntryBO(ConceptEntryBO r){
    	entries.add(entries.size(), r);
        Iterator<IConceptEntryListViewer> iterator = changeListeners.iterator();
        while (iterator.hasNext()){
                iterator.next().addEntry(r);
        }
    }
}
