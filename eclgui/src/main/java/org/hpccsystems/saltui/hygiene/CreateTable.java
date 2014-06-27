package org.hpccsystems.saltui.hygiene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.hpccsystems.eclguifeatures.ErrorNotices;
import org.hpccsystems.ecljobentrybase.ECLJobEntryDialog;
import org.pentaho.di.core.Const;



public class CreateTable {
	private Shell shell;
	private Table table;
	private TableViewer tableViewer;
	private HygieneEntryList entryList;
	private HygieneRuleBO activeRule = new HygieneRuleBO();
	private Composite compForGrp2;
	private ArrayList<TableEditor> tableEditEditor = new ArrayList<TableEditor>();
	private ArrayList<TableEditor> tableDelEditor = new ArrayList<TableEditor>();
	private int tmpBtnIndex = 0;
	
	
	private Shell editRuleDialog;
	private HygieneRuleList ruleList;
	private String rules[];
	private Combo ruleName;
	//private Text fieldName;
	private Combo fieldName;
	private Text displayName;
	private Text machineRuleName;
	private Text allowChars;
	private Text spaceChars;
	private Text ignoreChars;
	private Button leftTrim;
	private Button caps;
	private Text lengths;
	private Text noQuotes;
	//private Combo like;
	private Text words;
	private Combo onfail;
	
	private String currentRuleName;
	/**
	 * Return the RecordList
	 */

	private String fields[];
	
	public void setEntryList(HygieneEntryList entryList) {
		this.entryList = entryList;
	}
	public HygieneRuleList getRuleList() {
		return ruleList;
	}
	public void setRuleList(HygieneRuleList ruleList) {
		this.ruleList = ruleList;
	}
	public HygieneEntryList getEntryList() {
		return entryList;	
	}
	
	
	
	public void loadFields(String fields[]){
		this.fields = fields;
	}
	public TabItem buildDetailsTab(String tabName, TabFolder tabFolder){
    	
    	TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
		tabItem.setText(tabName);
		
		/*Composite composite = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite);*/

		ScrolledComposite sc2 = new ScrolledComposite(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);
		compForGrp2 = new Composite(sc2, SWT.NONE);
		sc2.setContent(compForGrp2);
		
		// Set the minimum size
		sc2.setMinSize(650, 450);

		// Expand both horizontally and vertically
		sc2.setExpandHorizontal(true);
		sc2.setExpandVertical(true);
        

        FormLayout groupLayout = new FormLayout();
        groupLayout.marginWidth = 10;
        groupLayout.marginHeight = 10;

        GridData gridData = new GridData (GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_BOTH);
        compForGrp2.setLayoutData (gridData);

		// Set numColumns to 6 for the buttons 
		GridLayout layout = new GridLayout(4, false);
		compForGrp2.setLayout (layout);
		
		
		int style = SWT.CHECK | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION;

		table = new Table(compForGrp2, style);
		Listener paintListener = new Listener() {
			   public void handleEvent(Event event) {
			      // height cannot be per row so simply set
				   try{
					   switch(event.type){
					   case SWT.MeasureItem:
						   event.height = 20;
						   break;
						   
					   case SWT.PaintItem:
						   event.height = 20;
						   break;
						   
					   }
					   
				   }catch(Exception e){
					   System.out.println("Listener error");
				   }
			   }
			   
			};
			table.addListener(SWT.MeasureItem,paintListener);
			table.addListener(SWT.PaintItem,paintListener);
			table.setRedraw(true);
		tableViewer = new TableViewer(table);     
                table.addListener (SWT.Selection, new Listener () {
                    public void handleEvent (Event event) {
                            tableViewer.refresh();
                            table.redraw();
                            
                    }
                });      
		GridData tableGridData = new GridData(GridData.FILL_BOTH);
		tableGridData.grabExcessVerticalSpace = true;
		tableGridData.horizontalSpan = 4;
		tableGridData.grabExcessHorizontalSpace = true;
		tableGridData.widthHint = 625;
		table.setLayoutData(tableGridData);		
					
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		addColumns();
		loadData();
		addRowButtons();
		refreshTable();
        //tabItem.setControl(compForGrp2);
        tabItem.setControl(sc2);
        createButtons(compForGrp2);
        sc2.redraw();
		return tabItem;
    }




	public void refreshTable(){
		
		tableViewer.refresh();
	    table.redraw();
	}

	public void removeRowButtons(){
		//System.out.println("Remove Row Buttons--");
		TableItem[] items = table.getItems();
		for(int i=0; i<items.length;i++){
			//System.out.println("Index: " + i + " size Edit: " + tableEditEditor.size());
			if (i >= 0 && i < tableEditEditor.size()) {
				//System.out.println("Dispose tableEditEditor --");
				tableEditEditor.get(i).getEditor().dispose();
				tableEditEditor.get(i).dispose();
			}
			//System.out.println("Index: " + i + " size Del: " + tableDelEditor.size());
			if (i >= 0 && i < tableDelEditor.size()) {
				//System.out.println("Dispose tableDelEditor --");
				tableDelEditor.get(i).getEditor().dispose();
				tableDelEditor.get(i).dispose();
			}
		}
		tmpBtnIndex++;
		tableEditEditor.clear();
		tableDelEditor.clear();
		tableEditEditor = new ArrayList<TableEditor>();
		tableDelEditor = new ArrayList<TableEditor>();
	}
	public void addRowButtons(){
		TableItem[] items = table.getItems();
		for(int i=0; i<items.length;i++){
			//System.out.println("adding button: " + i);
			buildRowButtons(i);
		}
	}
	public void buildRowButtons(int i){
		TableEditor tableEditEditor = new TableEditor(table);
		TableEditor tableDelEditor = new TableEditor(table);
		//get the row
		TableItem item = table.getItem(i);//items[i];
		Button editButton = new Button(table,SWT.PUSH);
		editButton.setText("Edit");
		//editButton.setText("EDIT"+tmpBtnIndex);
		
		tableEditEditor.minimumWidth = 45;
		tableEditEditor.minimumHeight = 18;
		tableEditEditor.horizontalAlignment = SWT.CENTER;
		tableEditEditor.setEditor(editButton,item,2);
		GridData editBtnGrid = new GridData();
		editBtnGrid.widthHint = 45;
		editBtnGrid.heightHint = 18;
		editButton.setLayoutData(editBtnGrid);
		editButton.pack();
		//add listner
		final int index = i;
		 Listener editListener = new Listener() {

	            public void handleEvent(Event e) {
	                //System.out.println("EDIT--" + index);
	            	createEditDialog(index, entryList.getEntry(index));
	            }
	        };

	        editButton.addListener(SWT.Selection, editListener);
		
		Button deleteButton = new Button(table,SWT.PUSH);
		//deleteButton.setText("Delete"+tmpBtnIndex);
		deleteButton.setText("Delete");
		
		tableDelEditor.minimumWidth = 45;
		tableDelEditor.minimumHeight = 18;
		tableDelEditor.horizontalAlignment = SWT.CENTER;
		tableDelEditor.setEditor(deleteButton,item,3);
		GridData delBtnGrid = new GridData();
		delBtnGrid.widthHint = 45;
		delBtnGrid.heightHint = 18;
		deleteButton.setLayoutData(delBtnGrid);
		deleteButton.pack();
		//add listner
		Listener delListener = new Listener() {

            public void handleEvent(Event e) {
                deleteEntry(index);
    			
            }
        };

        deleteButton.addListener(SWT.Selection, delListener);
		
		this.tableEditEditor.add(tableEditEditor);
		this.tableDelEditor.add(tableDelEditor);
	}
	
	private void deleteEntry(int index){
		//System.out.println("deleteEntry-- index: " + index);
		removeRowButtons();
		entryList.removeEntry(index);
		tableViewer.remove(index);
		
		//table.getItem(index).dispose();
		
		tableViewer.refresh();
		table.setRedraw(true);
        table.redraw();
        addRowButtons();            
		//refreshTable();
	}
	public CreateTable(){
		ruleList = new HygieneRuleList();
		ruleList.createDefault();
	}
	public static void main(String[] args) {
		HygieneEntryBO e = new HygieneEntryBO();
		e.setField("test");
		e.setRuleName("To Uppercase");
		CreateTable ct = new CreateTable();
		Display display = new Display ();
		final Shell dialog = new Shell (display, SWT.DIALOG_TRIM);
		ct.shell = dialog;
		ct.createEditDialog(1, e);
		
		while (!ct.shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		ct.shell.dispose ();
		
	}

	private void createEditDialog(int entryIndex, HygieneEntryBO newEntry){
			//tableViewer.getElementAt(index).
		
			final HygieneEntryBO entry = newEntry;//entryList.getEntry(index);
			final int thisEntryIndex = entryIndex;
			final String oldRuleName = newEntry.getRuleName();
			currentRuleName = newEntry.getRuleName();
			editRuleDialog = new Shell(this.shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
			editRuleDialog.setText("Edit Rule Field Relation ");
			GridLayout layout = new GridLayout();
			layout.marginWidth = 10;
			layout.marginHeight = 10;
			//formLayout.spacing = 10;
			layout.numColumns = 2;
			
			GridData dialogGrid = new GridData();
			dialogGrid.widthHint = 400;
			dialogGrid.heightHint = 425;
			dialogGrid.horizontalAlignment = SWT.CENTER;
			editRuleDialog.setLayout(layout);
			editRuleDialog.setLayoutData(dialogGrid);
			// position near parent window location
			editRuleDialog.setLocation(this.shell.getLocation().x + 10,this.shell.getLocation().y + 10);
			//dialog.setSize(400, 400);
			
			
			//final Combo combo = new Combo(dialog, SWT.NONE);
			//combo.setItems (options);
			ModifyListener lsMod = new ModifyListener() {

	            public void modifyText(ModifyEvent e) {
	                //jobEntry.setChanged();
	            }
	        };
	        int middle = 20;
	        int margin = Const.MARGIN;
	        
	        GridLayout groupLayout = new GridLayout();
	        groupLayout.marginWidth = 10;
	        groupLayout.marginHeight = 10;
	        groupLayout.numColumns = 2;
	        
	        
	    
	        // Stepname line
	        Group generalGroup = new Group(editRuleDialog, SWT.SHADOW_NONE);
	       // props.setLook(generalGroup);
	        generalGroup.setText("Field to Rule Association");
	        generalGroup.setLayout(groupLayout);
	        
	        GridData gridData = new GridData();
			gridData.widthHint = 500;
			gridData.heightHint = 100;
			gridData.grabExcessHorizontalSpace = true;
			gridData.grabExcessVerticalSpace = true;
			gridData.horizontalSpan = 2;
			generalGroup.setLayoutData(gridData);

	        
			//fieldName = buildText("Field",  generalGroup);
			fieldName = buildCombo("Field",  generalGroup, this.fields);
			if(entry.getField() != null) fieldName.setText(entry.getField());
			
			rules = ruleList.getTitles();
			ruleName = buildCombo("Rule", generalGroup, rules);
			ruleName.add("New Rule");
			
			if(entry.getHygieneRuleListIndex() != -1 && ruleList.getFields().size() > 0){
				activeRule = ruleList.get(entry.getHygieneRuleListIndex());
			}else{
				activeRule = new HygieneRuleBO();
			}
			
			Group detailsGroup = new Group(editRuleDialog, SWT.SHADOW_NONE);
		       // props.setLook(generalGroup);
			detailsGroup.setText("Rule Details");
			detailsGroup.setLayout(groupLayout);
			
			GridData detailData = new GridData();
			detailData.widthHint = 500;
			detailData.heightHint = 300;
			detailData.grabExcessHorizontalSpace = true;
			detailData.grabExcessVerticalSpace = true;
			detailData.horizontalSpan = 2;
			detailsGroup.setLayoutData(detailData);
		        
			displayName = buildText("Rule Name",  detailsGroup);
			machineRuleName = buildText("Machine Rule Name",  detailsGroup);
			machineRuleName.setEditable(false);
			Color color = new Color(shell.getDisplay(),242,242,242);
			machineRuleName.setBackground(color);
			displayName.addKeyListener(new KeyListener() {

				@Override
				public void keyPressed(KeyEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void keyReleased(KeyEvent arg0) {
					machineRuleName.setText(displayName.getText().replace(" ", "_"));
					
				}
				});
			
			
			allowChars = buildText("Allowed Characters",  detailsGroup);
			spaceChars = buildText("Characters treated as spaces",  detailsGroup);
			ignoreChars = buildText("Characters Ignored",  detailsGroup);
			leftTrim = buildCheckbox("Left Trim",  detailsGroup);
			caps = buildCheckbox("All Letters are Capital",  detailsGroup);
			lengths = buildText("Lengths",  detailsGroup);
			noQuotes = buildText("Disallow Characters at Start/End (quotes)",  detailsGroup);
			//like = buildCombo("Like",generalGroup,fieldTypes.getTitles());
			words = buildText("Words",  detailsGroup);
			onfail = buildCombo("On Fail",detailsGroup,new String[]{"IGNORE", "CLEAN", "BLANK"});
			

			Button edit = new Button(detailsGroup, SWT.PUSH);
			edit.setText("Edit Rule");
			GridData editFormat = new GridData();
			editFormat.widthHint = 100;
			//editFormat.horizontalSpan = 2;
			editFormat.horizontalIndent = 10;
			
			//cancelFormat.horizontalAlignment = SWT.CENTER;
			edit.setLayoutData(editFormat);
            
			//layout stuff...
			//add listener for when cancel is pressed:
			edit.addSelectionListener(new SelectionAdapter() {
			 public void widgetSelected(SelectionEvent e) {
					ErrorNotices en = new ErrorNotices();
					
		    		String notice = "Editing this rule will effect all fields using the rule!";
					if(en.openComfirmDialog(editRuleDialog,notice)){
						setDetailFieldsState(true);
					}
			 }
			});
			
			Button del = new Button(detailsGroup, SWT.PUSH);
			del.setText("Delete Rule");
			GridData delFormat = new GridData();
			delFormat.widthHint = 100;
			//delFormat.horizontalSpan
			
			delFormat.horizontalIndent = 10;
			//cancelFormat.horizontalAlignment = SWT.CENTER;
			del.setLayoutData(delFormat);
            
			//layout stuff...
			//add listener for when cancel is pressed:
			del.addSelectionListener(new SelectionAdapter() {
			 public void widgetSelected(SelectionEvent e) {
				 
				 ErrorNotices en = new ErrorNotices();
					
		    	 String notice = "Deleting this rule will effect all fields using the rule!";
				 if(en.openComfirmDialog(editRuleDialog,notice)){
					 //code to delete rule.
					 //start here!!!!!!!!
					 //get the index of rule to delete.
					 String oldName = ruleName.getText();
					 int indexToDel = ruleName.getSelectionIndex();
					 
					 if(indexToDel == -1){
						 //rule hasn't changed we are still on the existing one
						 indexToDel = entry.getHygieneRuleListIndex();
					 }
					 //set select to new rule
					 ruleName.setText("New Rule");
					 //remove rule from BO
					 ruleList.remove(indexToDel);
					 //remove rule from select
					 ruleName.remove(indexToDel);
					 loadRuleEditData(-1);
					 ruleName.setText("New Rule");
					 
					 //now we need to update the list page.
					 entryList.updateAll("", oldName);
					 tableViewer.refresh();
					 table.setRedraw(true);
					 table.redraw();
				 }
			 }
			});
			
			
			setDetailFieldsState(false);
			
			
			if(entry.getRuleName() != null){ 
				ruleName.setText(entry.getRuleName());
				loadRuleEditData(entry.getHygieneRuleListIndex());
			}else{
				ruleName.setText("New Rule");
			}
			
			
			ruleName.addSelectionListener(new SelectionAdapter() {
				 public void widgetSelected(SelectionEvent e) {
					 System.out.println("Rule Changed");
					 //fieldTypes
					 
					 int selected = ruleName.getSelectionIndex();
					 
					 loadRuleEditData(selected);
					 
					 currentRuleName = ruleName.getText();
				 }
				});
			
			
			//final ArrayList<Button> checkboxes = new ArrayList<Button>();
			//for(int i = 0; i<fieldTypes.getFields().size(); i++){
			//	checkboxes.add(buildCheckbox(fieldTypes.get(i).getDisplayTitle(), generalGroup));
			//}
			 //create cancel button
			

			//creates ok button
			Button ok = new Button(editRuleDialog, SWT.PUSH);
			ok.setText("OK");
			GridData okFormat = new GridData();
			okFormat.horizontalIndent = 175;
			okFormat.widthHint = 50;
			//okFormat.horizontalAlignment = SWT.CENTER;
			ok.setLayoutData(okFormat);
			//layout stuff...
			//add listener when OK button pressed
			ok.addSelectionListener(
			// nested anonymous inner class for listener, could be separate class too
			new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				saveEditDialog(oldRuleName,entry,thisEntryIndex);

			}

			});
			
			Button cancel = new Button(editRuleDialog, SWT.PUSH);
			cancel.setText("Cancel");
			GridData cancelFormat = new GridData();
			cancelFormat.widthHint = 50;
			
			//cancelFormat.horizontalAlignment = SWT.CENTER;
			cancel.setLayoutData(cancelFormat);
            
			//layout stuff...
			//add listener for when cancel is pressed:
			cancel.addSelectionListener(new SelectionAdapter() {
			 public void widgetSelected(SelectionEvent e) {
			 System.out.println("User cancelled dialog");
			 editRuleDialog.close();
			 }
			});
			
			
			editRuleDialog.pack();
			editRuleDialog.open();
			
	};
	
	private void saveEditDialog(String oldRuleName, HygieneEntryBO entry, int thisEntryIndex){

		String errors = "";
		Boolean isValid = true;
		
		if(fieldName.getText().equals("")){
			isValid = false;
			errors = "You must include a Field for this Field Rule Relation!" + "\r\n";
		}
		if(displayName.getText().equals("")){
			//no save
			errors = "You Must include a Rule Name for your rule!" + "\r\n";
			isValid = false;
		}
		Boolean isUnique = true;
		
		if(isValid){
			//lets make sure the name hasn't been used
			
			
			int testIndex = ruleList.getIndex(displayName.getText()); //will be -1 if no match
			
			if(ruleName.getText().equalsIgnoreCase("New Rule")){
				//we are creating a new rule and its name must be unique.
				if(testIndex >= 0){
					//rule name already exists and this is a new rule so we know its a duplicate
					isUnique = false;
				}
				
			}else{
				//we may be updating a rule to a new name
				
				int currentIndex = ruleName.getSelectionIndex();
				
				if(currentIndex == -1){
					//rule hasn't been changed check to make sure ruleName and display name match
					//we need more logic here
					if(ruleName.getText().equalsIgnoreCase(displayName.getText())){
						//rule names match so we are editing the same rule
						isUnique = true;
					}else{
						if(testIndex >= 0){
							//rule name already exists now is this the same rule
							if(entry.getHygieneRuleListIndex() == testIndex){
								isUnique = true;//we are on the same rule
							}else{
								isUnique = false;
							}
						}
					}
					
				}else if (currentIndex == testIndex){
					//hey we are updating the same rule.
					isUnique = true;
				}else{
					isUnique = false;
				}
				
				
				
				
			}
			}
			
			if(!isUnique){
				errors = "You must use a unique Name!"+ "\r\n";
				isValid = false;
			}
		
		if(!isValid){
			ErrorNotices en = new ErrorNotices();
    		errors += "\r\n";
    		errors += "\r\n\r\n";
    		en.openSaveErrorDialog(editRuleDialog,errors);
		}else{
			//we need to see if the name changed 
			
			

			
			if(fieldName.getText() != null)entry.setField(fieldName.getText());
			
			
			int newRuleIndex = ruleList.getIndex(ruleName.getText());
			int ruleIndex = entry.getHygieneRuleListIndex();//fieldTypes.getIndex(entry.getRuleName());
			System.out.println("entry's current index: " + ruleIndex);
			System.out.println("selected index: " + newRuleIndex);
			//need to see if its a new record if so we will create a new record in memory
			
			if(ruleName.getText().equals("New Rule")){
				 //new rule
				System.out.println("new rule " + activeRule.getDisplayTitle());
				 activeRule = new HygieneRuleBO();
				 updateHygieneRuleBO();
				 System.out.println("new rule " + activeRule.getDisplayTitle());
				 
				 ruleList.add(activeRule);
				 int thisRuleIndex = ruleList.getIndex(displayName.getText());
				 System.out.println("New Rule Index : " + thisRuleIndex);
				 entry.setHygieneRuleListIndex(thisRuleIndex);
				 entry.setRuleName(displayName.getText());
				 ruleName.setItem(thisRuleIndex, displayName.getText());
				 
			 }else{
				 if(!currentRuleName.equals(displayName.getText())){
						//now iterate through any entries if fieldName is old update
						//newname,oldname
						if(!oldRuleName.equals("")){
							entryList.updateAll(displayName.getText(), oldRuleName);
						}
					}
				 
				 updateHygieneRuleBO();
				 System.out.println("update rule index: " + ruleIndex + " (" + entry.getRuleName() + ") Selected (newRuleIndex): " + newRuleIndex + "(" + ((HygieneRuleBO)ruleList.get(newRuleIndex)).getDisplayTitle() + ")");
				 //if(entry.getFieldTypeListIndex() != -1){
				//	 fieldTypes.update(entry.getFieldTypeListIndex(), activeFieldType);
				 //}else 
			     if (newRuleIndex != -1){
					 //updates the existing rule list entry with the new info
			    	 ruleList.update(newRuleIndex, activeRule);
					 
				 }
				 entry.setHygieneRuleListIndex(newRuleIndex);
				 //ruleName.setItem(newRuleIndex,displayName.getText());
				 entry.setRuleName(displayName.getText());
				 //updates the select with the new value
				 entryList.updateEntry(thisEntryIndex,entry);
				 //edit rule
				 //ft = fieldTypes.get(selected);
			 }
		
			
			//activeRule = new HygieneRuleBO();
		// close the message dialog
			tableViewer.refresh();
			table.setRedraw(true);
			table.redraw();
			editRuleDialog.close();
		}
	}
	private void setDetailFieldsState(Boolean enable){
		
		Color color = new Color(shell.getDisplay(),237,237,237);
		
		if(enable){
			color = new Color(shell.getDisplay(),255,255,255);
		}
		displayName.setEditable(enable);
		displayName.setBackground(color);
		
		//machineRuleName.setEditable(enable);
		//machineRuleName.setBackground(color);
		
		allowChars.setEditable(enable);
		allowChars.setBackground(color);
		
		spaceChars.setEditable(enable);
		spaceChars.setBackground(color);
		
		ignoreChars.setEditable(enable);
		ignoreChars.setBackground(color);
		
		leftTrim.setEnabled(enable);
		leftTrim.setBackground(color);
		
		caps.setEnabled(enable);
		caps.setBackground(color);
		
		lengths.setEditable(enable);
		lengths.setBackground(color);
		
		noQuotes.setEditable(enable);
		noQuotes.setBackground(color);
		
		words.setEditable(enable);
		words.setBackground(color);
		
		onfail.setEnabled(enable);
		onfail.setBackground(color);
		

		
	}
private void updateHygieneRuleBO(){
	
	activeRule.setDisplayTitle(displayName.getText());
	activeRule.setTypename(machineRuleName.getText());
	activeRule.setAllow(allowChars.getText());
	activeRule.setSpaces(spaceChars.getText());
	activeRule.setIgnore(ignoreChars.getText());
	activeRule.setLefttrim(leftTrim.getSelection());
	activeRule.setCaps(caps.getSelection());
	activeRule.setLengths(lengths.getText());
	activeRule.setDisallowedQuotes(noQuotes.getText());
	//activeFieldType.setLike(like.getText());
	activeRule.setWords(words.getText());
	activeRule.setOnfail(onfail.getText());
	
}
private void loadRuleEditData(int selected){
	
	 
	 System.out.println("loadRuleEditData - Selected Index: " + selected);
	 if(selected > rules.length-1 || selected == -1 || ruleList.getTitles().length == 0 || ruleName.getText().equalsIgnoreCase("New Rule")){
		 //new rule
		 System.out.println("new rule");
		 activeRule = new HygieneRuleBO("");
		 setDetailFieldsState(true);
		 ruleName.setText("New Rule");
		 
	 }else{
		 System.out.println("Current Active Rule : " +  activeRule.getDisplayTitle());
		 System.out.println("Loading new (existing) Active Rule: " + selected);
		 activeRule = ruleList.get(selected);
		 System.out.println("Loading : " +  activeRule.getDisplayTitle());
		 setDetailFieldsState(false);
	 }
	 System.out.println(activeRule.getDisplayTitle());
	 displayName.setText(activeRule.getDisplayTitle());
	 machineRuleName.setText(activeRule.getTypename());
	 allowChars.setText(activeRule.getAllow());
	 spaceChars.setText(activeRule.getSpaces());
	 ignoreChars.setText(activeRule.getIgnore());
	 leftTrim.setSelection(activeRule.isLefttrim());
	 caps.setSelection(activeRule.isCaps());
	 lengths.setText(activeRule.getLengths());
	 noQuotes.setText(activeRule.getDisallowedQuotes());
	// like.setText(activeFieldType.getLike());
	 words.setText(activeRule.getWords());
	 onfail.setText(activeRule.getOnfail());
	 

}
/**
 * Add the Buttons
 * @param parent
 */
private void createButtons(Composite parent) {
	
	// Create and configure the "Add" button
	Button add = new Button(parent, SWT.PUSH | SWT.CENTER);
	add.setText("Add");
	
	GridData gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
	gridData.widthHint = 80;
	add.setLayoutData(gridData);
	add.addSelectionListener(new SelectionAdapter() {
   		// Add a record and refresh the view
		public void widgetSelected(SelectionEvent e) {
			removeRowButtons();
			entryList.addEntry(table.getSelectionIndex());
			addRowButtons();
            tableViewer.refresh();
            table.redraw();
            
            createEditDialog(entryList.getEntries().size()-1, entryList.getEntry(entryList.getEntries().size()-1));
            rules = ruleList.getTitles();
            
		}
	});

	//	Create and configure the "Delete" button
	Button delete = new Button(parent, SWT.PUSH | SWT.CENTER);
	delete.setText("Delete");
	gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
	gridData.widthHint = 80; 
	delete.setLayoutData(gridData); 
	delete.addSelectionListener(new SelectionAdapter() {
		//Remove all the records that are checked and refresh the view
		public void widgetSelected(SelectionEvent e) {
			List<Integer> arlCheckedIndexes = new ArrayList<Integer>();
			for (int i = 0; i < table.getItemCount(); i++) {
				if (table.getItems()[i].getChecked()) {
					arlCheckedIndexes.add(i);
				}
			}
					
			Collections.sort(arlCheckedIndexes);
					
			Integer[] arrSortedIndexes = arlCheckedIndexes.toArray(new Integer[arlCheckedIndexes.size()]);
			for (int j = arrSortedIndexes.length - 1 ; j>=0; j--) {
				deleteEntry(arrSortedIndexes[j]);
				//entryList.removeEntry(arrSortedIndexes[j]);
				//table.remove(arrSortedIndexes[j]);
				//tableViewer.refresh();
                //                    table.redraw();
			}
                            //table.getItem(0).setImage(RecordLabels.getImage("unchecked"));
                            //tableViewer.refresh();
                            //table.redraw();
                            table.getColumns()[0].setImage(HygieneRecordLabels.getImage("unchecked"));
		}
	});
	
	/*
	
	//Create and configure the "Move Up" button
	Button btnRowUp = new Button(parent, SWT.PUSH | SWT.CENTER);
	btnRowUp.setImage(RecordLabels.getImage("upArrow"));
	btnRowUp.setText("Move Up");
	gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
	gridData.widthHint = 90; 
	btnRowUp.setLayoutData(gridData); 

	btnRowUp.addSelectionListener(new SelectionAdapter() {
		//Move the selected record one level up and refresh the view
		public void widgetSelected(SelectionEvent e) {
			if(entryList.getEntries() != null && entryList.getEntries().size() > 0) {
				int selectionIndex = 0;
				for (int i = 0; i < table.getItemCount(); i++) {
					if (table.getItems()[i].getChecked()) {
						selectionIndex = i;
					}
				}
				
				if(table.getItem(selectionIndex).getChecked() && selectionIndex > 0){
					Collections.swap(entryList.getEntries(), selectionIndex-1, selectionIndex);
				}
				
				tableViewer.refresh();
				if(selectionIndex > 0){
					for (int i = 0; i < table.getItemCount(); i++) {
						table.getItems()[i].setChecked(false);
					}
					table.getItem(selectionIndex-1).setChecked(true);
				}
			}
                            tableViewer.refresh();
                            table.redraw();
		}
	});
	
	///Create and configure the "Move Down" button
	Button btnRowDown = new Button(parent, SWT.PUSH | SWT.CENTER);
	btnRowDown.setImage(RecordLabels.getImage("downArrow"));
	btnRowDown.setText("Move Down");
	gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
	gridData.widthHint = 90;
	btnRowDown.setLayoutData(gridData); 

	btnRowDown.addSelectionListener(new SelectionAdapter() {
		//Move the selected record one level down and refresh the view
		public void widgetSelected(SelectionEvent e) {
			if(entryList.getEntries() != null && entryList.getEntries().size() > 0) {
				int selectionIndex = 0;
				for (int i = 0; i < table.getItemCount(); i++) {
					if (table.getItems()[i].getChecked()) {
						selectionIndex = i;
						break;
					}
				}
				
				if(table.getItem(selectionIndex).getChecked() && selectionIndex < table.getItems().length -1){
					Collections.swap(entryList.getEntries(), selectionIndex+1, selectionIndex);
				}
				
				tableViewer.refresh();
				if(selectionIndex < table.getItems().length - 1) {
					for (int i = 0; i < table.getItemCount(); i++) {
						table.getItems()[i].setChecked(false);
					}
					table.getItem(selectionIndex+1).setChecked(true);
				}
			}
                            tableViewer.refresh();
                            table.redraw();
		}
	});*/
	
}

	public void addColumns(){
		TableColumn column = new TableColumn(table, SWT.LEFT, 0);
        column.setText("Fields");
        column.setWidth(250);
        
        TableColumn column2 = new TableColumn(table, SWT.LEFT, 1);
        column2.setText("Rules");
        column2.setWidth(250);
        
        TableColumn column3 = new TableColumn(table, SWT.CENTER, 2);
        column3.setText("Edit");
        column3.setWidth(50);
        
        TableColumn column4 = new TableColumn(table, SWT.CENTER, 3);
        column4.setText("Delete");
        column4.setWidth(50);
	}
	
	public void loadData(){
		/*entryList = new EntryList();
		EntryBO eb = new EntryBO();
		eb.setField("test");
		eb.setRuleName("test Rule");
		entryList.add(eb);
		EntryBO eb2 = new EntryBO();
		eb2.setField("test2");
		eb2.setRuleName("test Rule");
		entryList.add(eb2);
		EntryBO eb3 = new EntryBO();
		eb3.setField("test3");
		eb3.setRuleName("test Rule");
		entryList.add(eb3);
		EntryBO eb4 = new EntryBO();
		eb4.setField("test4");
		eb4.setRuleName("test Rule");
		entryList.add(eb4);*/
		tableViewer.setContentProvider(new ExampleContentProvider());	//Set the Content Provider for the table	
		tableViewer.setLabelProvider(new HygieneEntryLabels());	//Set the Label Provider for the table
		tableViewer.setInput(entryList);
		
		table.setRedraw(true);
	}
	
	
	public CreateTable(Shell shell) {
		ruleList = new HygieneRuleList();
		ruleList.createDefault();
		this.shell = shell;
	}
	
	
	
	/**
	 * InnerClass that acts as a proxy for the RecordList providing content for the Table. It implements the IRecordListViewer 
	 * interface since it must register changeListeners with the RecordList 
	 */
	class ExampleContentProvider implements IStructuredContentProvider, IHygieneEntryListViewer {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
			if (newInput != null) {
				((HygieneEntryList) newInput).addChangeListener(this);
			}
			if (oldInput != null)
				((HygieneEntryList) oldInput).removeChangeListener(this);
		}

		public void dispose() {
			entryList.removeChangeListener(this);
		}

		// Return the tasks as an array of Objects
		public Object[] getElements(Object parent) {
			return entryList.getEntries().toArray();
		}

		public void addEntry(org.hpccsystems.saltui.hygiene.HygieneEntryBO record) {
                    //System.out.println("addRecord");
			//Insert the record at a specific position
                   // removeRowButtons();
			if(tableViewer.getTable().getSelectionIndex() >= 0){
				tableViewer.insert(record, tableViewer.getTable().getSelectionIndex()+1);
				//buildRowButtons(tableViewer.getTable().getSelectionIndex()+1);
			} else {
				tableViewer.add(record);
				//buildRowButtons(table.getItems().length-1);
			}
			          
             refreshTable();
		}

		public void removeEntry(int index) {
			//deleteEntry(index);
			
		}

		public void modifyEntry(HygieneEntryBO record) {
			tableViewer.update(record, null);	
		}
	}
	
	
	
	public Button buildCheckbox(String strLabel, Composite cBox ){
		Button chk = new Button(cBox,SWT.CHECK);
		chk.setText(strLabel);
		GridData chkData = new GridData();
		chkData.horizontalSpan = 2;
		chkData.widthHint = 200;
		chkData.horizontalAlignment = SWT.RIGHT;
		
		chk.setLayoutData(chkData);
		return chk;
	}
	
	
	
	 public Text buildText(String strLabel, Composite groupBox) {
         // label
         Label fmt = new Label(groupBox, SWT.RIGHT);
         fmt.setText(strLabel);
         Text text = new Text(groupBox, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
         GridData gridData = new GridData();
         gridData.widthHint = 200;
         text.setLayoutData(gridData);
         return text;
     }

 public Text buildMultiText(String strLabel, Composite groupBox) {
         // label
         Label fmt = new Label(groupBox, SWT.RIGHT);
         fmt.setText(strLabel);
         Text text = new Text(groupBox, SWT.MULTI | SWT.LEFT | SWT.BORDER | SWT.V_SCROLL);
         GridData gridData = new GridData();
         gridData.widthHint = 200;
         text.setLayoutData(gridData);
         return text;
     }

 public Combo buildCombo(String strLabel, Composite groupBox, String[] items) {
         // label
         Label fmt = new Label(groupBox, SWT.RIGHT);
         fmt.setText(strLabel);
         Combo combo = new Combo(groupBox, SWT.MULTI | SWT.LEFT | SWT.BORDER);
         if(items != null){
        	 combo.setItems(items);
 		 }
         GridData gridData = new GridData();
         gridData.widthHint = 183;
         combo.setLayoutData(gridData);
         return combo;
     }
 public Label buildLabel(String strLabel, Control prevControl,
         ModifyListener lsMod, int middle, int margin, Composite groupBox){
         Label fmt = new Label(groupBox, SWT.RIGHT);
         fmt.setText(strLabel);
         return fmt;
     }
 
 
	
}
