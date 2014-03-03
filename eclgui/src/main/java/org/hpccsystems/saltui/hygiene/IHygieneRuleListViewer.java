package org.hpccsystems.saltui.hygiene;

public interface IHygieneRuleListViewer {
	
	//Update the view after the record has been added to record list
	public void addFieldType(HygieneRuleBO fieldType);
	
	//Update the view after the record has been removed from record list
	public void removeFieldType(int index);
	
	//Update the view after the record has been modified in the record list
	public void modifyFieldType(HygieneRuleBO fieldType);
}
