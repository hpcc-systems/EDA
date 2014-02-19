package org.hpccsystems.saltui.concept.table;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.hpccsystems.ui.constants.Constants;

public class ConceptsTable {
	
	private Table table;
	private TableViewer tableViewer;
	
	// Set column names
	private String[] columnNames = new String[] { Constants.TABLE_HEADER_CHILDREN, Constants.TABLE_HEADER_NON_NULL};
	
	private ConceptsRecordList conceptsList = null;
	
	public void setConceptsList(ConceptsRecordList conceptsList) {
		this.conceptsList = conceptsList;
		initConceptsList();
	}

	public ConceptsTable(Composite parent,ConceptsRecordList conceptsList) {
		 
		this.conceptsList = conceptsList;
		this.addChildControls(parent);
		
		initConceptsList();

	}
	public void initConceptsList(){
		if(conceptsList != null && conceptsList.listConcepts != null && conceptsList.listConcepts.size() > 0) {
			int count = 0;
			for (Iterator<ConceptsRecord> iterator = conceptsList.listConcepts.iterator(); iterator.hasNext();) {
				
				ConceptsRecord obj = iterator.next();
				TableItem item = table.getItem(count);
				item.setChecked(obj.isSelect());
				count++;
			}
		}
	}
	
	/**
	 * Add the child Components to the main Composite
	 * @param composite
	 */
	public void addChildControls(Composite composite) {
		
		// Create the table 
		createTable(composite);
		// Create and setup the TableViewer
		createTableViewer();
		tableViewer.setContentProvider(new ConceptsContentProvider());
		tableViewer.setLabelProvider(new ConceptsLabelProvider());
		
		// The input for the table viewer is the instance of ExampleTaskList
		//setConceptsList(new ConceptsRecordList());

		tableViewer.setInput(conceptsList);
	}
	
	private void createTable(Composite composite) {
		
		//int style = SWT.CHECK | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION;
		int style = SWT.CHECK | SWT.SINGLE | SWT.BORDER;
		
		table = new Table(composite, style);
		GridLayout layout = new GridLayout();
	    layout.numColumns = 1;
	    table.setLayout(layout);
	    
	    GridData data = new GridData();
	    data.horizontalAlignment = GridData.FILL;
	    data.verticalAlignment = GridData.FILL;
	    data.grabExcessHorizontalSpace = true;
	    data.grabExcessVerticalSpace = true;
	    int tableHeight = table.getItemHeight() * 5;
	    Rectangle trim = table.computeTrim(0, 0, 0, tableHeight);
	    data.heightHint = trim.height;
	    
	    table.setLayoutData(data);
	    
	    table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		TableColumn item = new TableColumn(table, SWT.LEFT, 0);
		item.setText(Constants.TABLE_HEADER_CHILDREN);
		item.setWidth(200);
		
		item = new TableColumn(table, SWT.LEFT, 1);
		item.setText(Constants.TABLE_HEADER_NON_NULL);
		item.setWidth(100);
		
	}
	
	private void createTableViewer() {
		
		tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);
		
		tableViewer.setColumnProperties(columnNames);
		// Create the cell editors
		CellEditor[] editors = new CellEditor[columnNames.length];
		
		// Column 1 : Completed (Checkbox)
		editors[1] = new CheckboxCellEditor(table);
		
		// Assign the cell editors to the viewer 
		tableViewer.setCellEditors(editors);
		
		// Set the cell modifier for the viewer
		tableViewer.setCellModifier(new ConceptsCellModifier(this));
	}
	
	class ConceptsContentProvider implements IStructuredContentProvider, IConceptsViewer{
		
		@Override
		public void inputChanged(Viewer arg0, Object oldInput, Object newInput) {
			if (newInput != null)
				((ConceptsRecordList) newInput).addChangeListener(this);
			if (oldInput != null)
				((ConceptsRecordList) oldInput).removeChangeListener(this);
		}
		
		@Override
		public void dispose() {
			if(conceptsList != null)
			conceptsList.removeChangeListener(this);
		}

		@Override
		public void conceptChanged(ConceptsRecord record) {
			tableViewer.update(record, null);
		}

		@Override
		public Object[] getElements(Object arg0) {
			return conceptsList.getConcepts().toArray();
		}

	}
	
	public Control getControl() {
		return table.getParent();
	}
	
	public void dispose() {
		// Tell the label provider to release its resources
		tableViewer.getLabelProvider().dispose();
	}
	
	public List<String> getColumnNames() {
		return Arrays.asList(columnNames);
	}
	
	/**
	 * Return the ExampleTaskList
	 */
	public ConceptsRecordList getConceptsList() {
		return conceptsList;	
	}
	
	public TableViewer getTableViewer() {
		return tableViewer;
	}
}
