package org.hpccsystems.sortui.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;


import org.hpccsystems.sortui.table.SortColumnRecord;
import org.hpccsystems.sortui.table.SortColumnRecordList;
import org.hpccsystems.sortui.table.SortColumnTable;
import org.pentaho.di.core.Const;

import com.hpccsystems.ui.constants.Constants;

public class ConfigureSortColumnUI {
	private SortColumnEntryBO sortColumnRule;
	private SortColumnTable objSortColumnTable = null;
	
	private Shell shell;
	private TableViewer conceptListTableViewer = null;
	
	public ConfigureSortColumnUI(SortColumnEntryBO crb, TableViewer conceptListTable){
		sortColumnRule = crb;
		this.conceptListTableViewer = conceptListTable;
	}
	public ConfigureSortColumnUI(SortColumnEntryBO crb){
		sortColumnRule = crb;
		//this.conceptListTableViewer = new TableViewer(null);
	}
	
	
	public void addChildControls(TabItem tab, Shell shell){
		final TabItem tabItem = tab;
		// shell = new Shell(tab.getParent().getShell(), SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);
		Composite compositeForConcept = new Composite(tab.getParent(), SWT.NONE);
		GridLayout layout = new GridLayout();
	    layout.numColumns = 3;
	    layout.makeColumnsEqualWidth = true;
	    compositeForConcept.setLayout(layout);
	    
	    GridData data = new GridData();
	    data.horizontalAlignment = GridData.FILL;
	    data.verticalAlignment = GridData.FILL;
	    data.horizontalSpan = 3;
	    data.grabExcessHorizontalSpace = true;
	    data.grabExcessVerticalSpace = true;
	    compositeForConcept.setLayoutData(data);
	    

	    
	    Label labelSelectConceptFields = new Label(compositeForConcept, SWT.NONE);
	    labelSelectConceptFields.setText(Constants.LABEL_FIELD_AND_CONCEPT);
	    data = new GridData();
	    data.verticalAlignment = SWT.TOP;
	    labelSelectConceptFields.setLayoutData(data);
	    //Group generalGroup = new Group(shell, SWT.SHADOW_NONE);
	    tab.setControl(compositeForConcept);
	    
	    
	    Composite comp = new Composite(compositeForConcept, SWT.NONE);
	    layout = new GridLayout();
	    layout.numColumns = 1;
	    comp.setLayout(layout);
	    
	    data = new GridData();
	    data.horizontalAlignment = GridData.FILL;
	    data.verticalAlignment = GridData.FILL;
	    data.horizontalSpan = 2;
	    data.grabExcessHorizontalSpace = true;
	    data.grabExcessVerticalSpace = true;
	    comp.setLayoutData(data);
	    
	    //The table for displaying fields
	   
	    objSortColumnTable = new SortColumnTable(comp,sortColumnRule.getRecordList());
	   
	    final Table table = objSortColumnTable.getTableViewer().getTable();
	    //objConceptsTable.getTableViewer().setInput(conceptRule.getRecordList());
	    //objConceptsTable.setConceptsList(conceptRule.getRecordList());
	    table.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
		        TableItem item =(TableItem)event.item;
		        if(!item.getChecked())
		        	objSortColumnTable.getTableViewer().getCellModifier().modify(item, Constants.TABLE_HEADER_DESCENDING, false);
		        
				int count = 0;
				for (int i = 0; i < table.getItemCount(); i++) {
					if (table.getItems()[i].getChecked()) {
						((SortColumnRecord)objSortColumnTable.getSortColumnList().getSortColumn().get(i)).setSelect(true);
						count++;
					} else {
						((SortColumnRecord)objSortColumnTable.getSortColumnList().getSortColumn().get(i)).setSelect(false);
					}
				}
		        
			}
		});
	    
	    table.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				Rectangle clientArea = table.getClientArea();
				Point pt = new Point(event.x, event.y);
				int index = table.getTopIndex();
				while (index < table.getItemCount()) {
					boolean visible = false;
					TableItem item = table.getItem(index);
					for (int i = 0; i < table.getColumnCount(); i++) {
						Rectangle rect = item.getBounds(i);
						if (rect.contains(pt)) {
							if (i == 1){
								SortColumnRecord concept = (SortColumnRecord)objSortColumnTable.getSortColumnList().getSortColumn().get(index);
								concept.setSelect(item.getChecked());
								if(concept.getDirection()=="descending")
									objSortColumnTable.getTableViewer().getCellModifier().modify(item, Constants.TABLE_HEADER_DESCENDING, false);
								else if(item.getChecked()) {
									objSortColumnTable.getTableViewer().getCellModifier().modify(item, Constants.TABLE_HEADER_DESCENDING, true);
								}
							}
						}
						if (!visible && rect.intersects(clientArea)) {
							visible = true;
						}
					}
					if (!visible)
						return;
					index++;
				}
			}
		});
		
	   // tabItem.setControl(comp);
	    
	    

		/*
		btnOk.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				
				
				
				sortColumnRule.setRecordList(objSortColumnTable.getSortColumnList());
				
				System.out.println(sortColumnRule.getRecordList().saveListAsString());
				if(conceptListTableViewer != null)
					conceptListTableViewer.refresh();
				
				shell.close();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		*/
		
		//Check if Selected Items > 3. If yes, disable ReOrderType Text Box.
	    
		if(objSortColumnTable.getSortColumnList() != null && objSortColumnTable.getSortColumnList().getSortColumn() != null && objSortColumnTable.getSortColumnList().getSortColumn().size() > 0){
	    	int count = 0;
	    	for (Iterator<SortColumnRecord> iterator = objSortColumnTable.getSortColumnList().getSortColumn().iterator(); iterator.hasNext();) {
	    		SortColumnRecord obj = (SortColumnRecord) iterator.next();
				if(obj.isSelect())
					count++;
	    	}
	    	
	    	
	    }
		loadData();
	}
	public void loadData(){
		
		
		
	}
	
	public void initFields(){
		//objConceptsTable.getConceptsList().initTestData();
	}
	public void run(Shell shell) {
		
		//this.shell = shell;
	    shell.setText(Constants.ADD_CONCEPTS_TITLE);
	    shell.setSize(800, 550);
	   GridLayout layout = new GridLayout();
	    layout.numColumns = 3;
	    layout.marginLeft = 10;
	    layout.marginRight = 10;
	    layout.makeColumnsEqualWidth = true;
	    shell.setLayout(layout);
	 /*
	    FormLayout formLayout = new FormLayout();
        formLayout.marginWidth = Const.FORM_MARGIN;
        formLayout.marginHeight = Const.FORM_MARGIN;
*/

        shell.setLayout(layout);
        shell.setText("Sort");
        
        TabFolder tabFolder = new TabFolder (shell, SWT.FILL | SWT.RESIZE | SWT.MIN | SWT.MAX);
        TabItem item1 = new TabItem(tabFolder, SWT.NULL);
        item1.setText ("General");
        
        TabItem item2 = new TabItem(tabFolder, SWT.NULL);
        item1.setText ("General");
        
        
	    addChildControls(item2,shell);
		
	    shell.open();
	   
	}
	
	
	public static void main(String[] args) {
		Display display = new Display();
	    Shell shell = new Shell(display);
	    
	    
	    SortColumnEntryBO crb = new SortColumnEntryBO();
	    
	    
	    List<String> test = new ArrayList();
	   // test.add("myfield");
	   //test.add("myfield2");
	   // test.add("field2");
	    test.add("field3");
	    SortColumnRecordList crl = new SortColumnRecordList("myfield*nonNull*selected*0|myfield2*allowNull*selected*1",test);
	    crl.initTestData();
	    
	    crb.setRecordList(crl);
	    
	   
	    ConfigureSortColumnUI ui = new ConfigureSortColumnUI(crb);
		ui.run(shell);
		
		while (!shell.isDisposed()) {
		      if (!display.readAndDispatch())
		        display.sleep();
		    }
		    display.dispose();
	}
	
}
