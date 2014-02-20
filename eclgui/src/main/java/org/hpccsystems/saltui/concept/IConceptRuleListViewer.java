package org.hpccsystems.saltui.concept;

public interface IConceptRuleListViewer {
	
	//Update the view after the record has been added to record list
	public void addFieldType(ConceptEntryBO fieldType);
	
	//Update the view after the record has been removed from record list
	public void removeFieldType(int index);
	
	//Update the view after the record has been modified in the record list
	public void modifyFieldType(ConceptEntryBO fieldType);
}
