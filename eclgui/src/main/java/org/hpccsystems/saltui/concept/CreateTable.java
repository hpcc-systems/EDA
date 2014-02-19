package org.hpccsystems.saltui.concept;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.hpccsystems.recordlayout.RecordLabels;
import org.hpccsystems.saltui.concept.edit.ConfigureConceptsUI;
import org.hpccsystems.saltui.concept.table.ConceptsRecordList;

import com.hpccsystems.ui.constants.Constants;



public class CreateTable {
	//public Boolean hasChanged = false;
	private ArrayList datasetFields = null;
	private Shell shell;
	private Table table;
	private TableViewer tableViewer;
	private ConceptEntryList entryList;
	private ConceptEntryBO activeRule = new ConceptEntryBO();
	private Composite compForGrp2;
	private ArrayList<TableEditor> tableEditEditor = new ArrayList<TableEditor>();
	private ArrayList<TableEditor> tableDelEditor = new ArrayList<TableEditor>();
	private int tmpBtnIndex = 0;
	
	
	private Shell editRuleDialog;
	//private ConceptRuleList ruleList;
	private String rules[];
	private Combo ruleName;

	
	private String currentRuleName;
	/**
	 * Return the RecordList
	 */

	private String fields[];
	
	public void setEntryList(ConceptEntryList entryList) {
		this.entryList = entryList;
	}
	//public ConceptRuleList getRuleList() {
	//	return ruleList;
	//}
	//public void setRuleList(ConceptRuleList ruleList) {
	//	this.ruleList = ruleList;
	//}
	public ConceptEntryList getEntryList() {
		return entryList;	
	}
	
	
	
	public void loadFields(String fields[]){
		this.fields = fields;
		this.datasetFields = new ArrayList();
		for(int i = 0; i< fields.length; i++){
			this.datasetFields.add(fields[i]);
		}
		
	}
	public TabItem buildDetailsTab(String tabName, TabFolder tabFolder){
    	
    	TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
		tabItem.setText(tabName);
		
		/*Composite composite = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite);*/
		GridLayout sc2Layout = new GridLayout();
		GridData s2cData = new GridData();
		s2cData.grabExcessHorizontalSpace = true;
		s2cData.grabExcessVerticalSpace = true;
		ScrolledComposite sc2 = new ScrolledComposite(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);
		compForGrp2 = new Composite(sc2, SWT.NONE);
		sc2.setContent(compForGrp2);
		sc2.setLayout(sc2Layout);
		// Set the minimum size
		sc2.setMinSize(650, 450);

		// Expand both horizontally and vertically
		sc2.setExpandHorizontal(true);
		sc2.setExpandVertical(true);
        

       // FormLayout groupLayout = new FormLayout();
       // groupLayout.marginWidth = 10;
       // groupLayout.marginHeight = 10;

        GridData gridData = new GridData (GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_BOTH);
        compForGrp2.setLayoutData (gridData);

		// Set numColumns to 6 for the buttons 
		GridLayout layout = new GridLayout(2, false);
		compForGrp2.setLayout (layout);
		
		
		int style = SWT.CHECK | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION;

		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_BOTH);
		
		table = new Table(compForGrp2, style);
		table.setLayoutData(gridData);
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
			
			//table.setRedraw(true);
		
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
		//refreshTable();
        //tabItem.setControl(compForGrp2);
        tabItem.setControl(sc2);
        createButtons(compForGrp2);
        //compForGrp2.redraw();
        //sc2.redraw();
		return tabItem;
    }




	public void refreshTable(){
		
		tableViewer.refresh();
	    //table.redraw();
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
		tableEditEditor.setEditor(editButton,item,1);
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
		tableDelEditor.setEditor(deleteButton,item,2);
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
		//ruleList = new ConceptRuleList();
		//ruleList.createDefault();
	}
	public static void main(String[] args) {
		ConceptEntryBO e = new ConceptEntryBO();
		e.setConceptName("tester");
		
		//e.setField("test");
		//e.setRuleName("To Uppercase");
		CreateTable ct = new CreateTable();
		ct.datasetFields = new ArrayList();
		ct.datasetFields.add("test");
		ct.datasetFields.add("test2");
		Display display = new Display ();
		final Shell dialog = new Shell (display, SWT.DIALOG_TRIM);
		ct.shell = dialog;
		//ct.createEditDialog(1, e);
		//ct.editRuleDialog = new Shell(ct.shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		//ct.editRuleDialog.setText("Edit Concept");
		//
		ct.entryList = new ConceptEntryList();
		
		GridLayout layout = new GridLayout(1, false);
		ct.shell.setLayout (layout);
		
		
		ct.run(dialog);
		while (!ct.shell.isDisposed ()) {
			ct.refreshTable();
			if (!display.readAndDispatch ()) display.sleep ();
		}
		ct.shell.dispose ();
		
		
	}
	
	public void run(Shell shell) {
		
		this.shell = shell;
	    shell.setText(Constants.ADD_CONCEPTS_TITLE);
	    shell.setSize(800, 650);
	    
	    GridLayout layout = new GridLayout(2, false);
		
	    layout.numColumns = 1;
	    layout.marginLeft = 10;
	    layout.marginRight = 10;
	    layout.makeColumnsEqualWidth = false;
	    shell.setLayout(layout);
	    TabFolder tabFolder = new TabFolder (shell, SWT.FILL | SWT.RESIZE | SWT.MIN | SWT.MAX);
	    TabItem item2 = this.buildDetailsTab("Concepts", tabFolder);
	    
	    Button tmpSave = new Button(shell, SWT.NONE);
	    tmpSave.setText("Test Save");
		Listener saveListener = new Listener() {

            public void handleEvent(Event e) {
                
            }
        };

        tmpSave.addListener(SWT.Selection, saveListener);
	    //addChildControls();
		
	    shell.open();
	   
	}

	private void createEditDialog(int entryIndex, ConceptEntryBO newEntry){
			//tableViewer.getElementAt(index).
		editRuleDialog = new Shell(this.shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		editRuleDialog.setText("Edit Concept");
		String inputFields = "";
		if(newEntry != null && newEntry.getRecordList() != null){
			inputFields = newEntry.getRecordList().saveListAsString();
		}
		ConceptsRecordList recordList = new ConceptsRecordList(inputFields,datasetFields);
	    newEntry.setRecordList(recordList);
		ConfigureConceptsUI concept = new ConfigureConceptsUI(newEntry,tableViewer);
		
		concept.run(editRuleDialog);
				
	}
	
	private void saveEditDialog(String oldRuleName, ConceptEntryBO entry, int thisEntryIndex){

		String errors = "";
		Boolean isValid = true;
		
		
			tableViewer.refresh();
			table.setRedraw(true);
			table.redraw();
			editRuleDialog.close();
		
	}
	private void setDetailFieldsState(Boolean enable){
		
		Color color = new Color(shell.getDisplay(),237,237,237);
		
		if(enable){
			color = new Color(shell.getDisplay(),255,255,255);
		}
		//displayName.setEditable(enable);
		
		

		
	}
private void updateHygieneRuleBO(){
	
	//activeRule.setDisplayTitle(displayName.getText());
	
	
}
private void loadRuleEditData(int selected){
	
	 /*
	 System.out.println("loadRuleEditData - Selected Index: " + selected);
	 if(selected > rules.length-1 || selected == -1 || ruleList.getTitles().length == 0 || ruleName.getText().equalsIgnoreCase("New Rule")){
		 //new rule
		 System.out.println("new rule");
		 activeRule = new ConceptRuleBO("");
		 setDetailFieldsState(true);
		 ruleName.setText("New Rule");
		 
	 }else{
		 System.out.println("Current Active Rule : " +  activeRule.getConceptName());
		 System.out.println("Loading new (existing) Active Rule: " + selected);
		 activeRule = ruleList.get(selected);
		 System.out.println("Loading : " +  activeRule.getConceptName());
		 setDetailFieldsState(false);
	 }
	 */
	 System.out.println(activeRule.getConceptName());
	// displayName.setText(activeRule.getDisplayTitle());
	 
	 

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
			//System.out.println("Index: " + table.getSelectionIndex());
			entryList.addEntry(table.getSelectionIndex());
			addRowButtons();
            tableViewer.refresh();
            table.redraw();
            
            createEditDialog(entryList.getEntries().size()-1, entryList.getEntry(entryList.getEntries().size()-1));
           // rules = ruleList.getTitles();
            
		}
	});

	//	Create and configure the "Delete" button
	Button delete = new Button(parent, SWT.PUSH | SWT.CENTER);
	delete.setText("Delete Selected");
	gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
	gridData.widthHint = 90; 
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

			}

            table.getColumns()[0].setImage(RecordLabels.getImage("unchecked"));
		}
	});
	
	
	
}

	public void addColumns(){
		TableColumn column = new TableColumn(table, SWT.LEFT, 0);
        column.setText("Concepts");
        column.setWidth(500);
        
        
        TableColumn column2 = new TableColumn(table, SWT.CENTER, 1);
        column2.setText("Edit");
        column2.setWidth(50);
        
        TableColumn column3 = new TableColumn(table, SWT.CENTER, 2);
        column3.setText("Delete");
        column3.setWidth(50);
	}
	
	public void loadData(){
		
		tableViewer.setContentProvider(new ExampleContentProvider());	//Set the Content Provider for the table	
		tableViewer.setLabelProvider(new ConceptEntryLabels());	//Set the Label Provider for the table
		tableViewer.setInput(entryList);
		
		table.setRedraw(true);
	}
	
	
	public CreateTable(Shell shell) {
	//	ruleList = new ConceptRuleList();
		//ruleList.createDefault();
		this.shell = shell;
	}
	
	public CreateTable(Shell shell, String[] datasets) {
		//	ruleList = new ConceptRuleList();
			//ruleList.createDefault();
			this.shell = shell;
			this.datasetFields = new ArrayList();
			for(int i = 0; i< datasets.length; i++){
				this.datasetFields.add(datasets[i]);
			}
		}
	
	
	
	/**
	 * InnerClass that acts as a proxy for the RecordList providing content for the Table. It implements the IRecordListViewer 
	 * interface since it must register changeListeners with the RecordList 
	 */
	class ExampleContentProvider implements IStructuredContentProvider, IConceptEntryListViewer {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
			if (newInput != null) {
				((ConceptEntryList) newInput).addChangeListener(this);
			}
			if (oldInput != null)
				((ConceptEntryList) oldInput).removeChangeListener(this);
		}

		public void dispose() {
			entryList.removeChangeListener(this);
		}

		// Return the tasks as an array of Objects
		public Object[] getElements(Object parent) {
			return entryList.getEntries().toArray();
		}

		public void addEntry(ConceptEntryBO record) {
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

		public void modifyEntry(ConceptEntryBO record) {
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
         combo.setItems(items);
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
