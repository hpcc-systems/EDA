package org.hpccsystems.sortui.table;

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

public class SortColumnTable {
	
	private Table table;
	private TableViewer tableViewer;
	
	// Set column names
	private String[] columnNames = new String[] { Constants.TABLE_HEADER_SORT_COLUMN, Constants.TABLE_HEADER_DESCENDING};
	
	private SortColumnRecordList sortColumnList = null;
	
	public void setSortColumnList(SortColumnRecordList sortColumnList) {
		this.sortColumnList = sortColumnList;
		initSortColumnList();
	}

	public SortColumnTable(Composite parent,SortColumnRecordList sortColumnList) {
		 
		this.sortColumnList = sortColumnList;
		this.addChildControls(parent);
		
		initSortColumnList();

	}
	public void initSortColumnList(){
		if(sortColumnList != null && sortColumnList.listSortColumn != null && sortColumnList.listSortColumn.size() > 0) {
			int count = 0;
			for (Iterator<SortColumnRecord> iterator = sortColumnList.listSortColumn.iterator(); iterator.hasNext();) {
				
				SortColumnRecord obj = iterator.next();
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
		tableViewer.setContentProvider(new SortColumnContentProvider());
		tableViewer.setLabelProvider(new SortColumnLabelProvider());
		
		// The input for the table viewer is the instance of ExampleTaskList
		//setSortColumnList(new SortColumnRecordList());

		tableViewer.setInput(sortColumnList);
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
		item.setText(Constants.TABLE_HEADER_SORT_COLUMN);
		item.setWidth(200);
		
		item = new TableColumn(table, SWT.LEFT, 1);
		item.setText(Constants.TABLE_HEADER_DESCENDING);
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
		tableViewer.setCellModifier(new SortColumnCellModifier(this));
	}
	
	class SortColumnContentProvider implements IStructuredContentProvider, ISortColumnViewer{
		
		@Override
		public void inputChanged(Viewer arg0, Object oldInput, Object newInput) {
			if (newInput != null)
				((SortColumnRecordList) newInput).addChangeListener(this);
			if (oldInput != null)
				((SortColumnRecordList) oldInput).removeChangeListener(this);
		}
		
		@Override
		public void dispose() {
			if(sortColumnList != null)
			sortColumnList.removeChangeListener(this);
		}

	

		@Override
		public Object[] getElements(Object arg0) {
			return sortColumnList.getSortColumn().toArray();
		}

		@Override
		public void sortColumnChanged(SortColumnRecord record) {
			// TODO Auto-generated method stub
			tableViewer.update(record, null);
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
	public SortColumnRecordList getSortColumnList() {
		return sortColumnList;	
	}
	
	public TableViewer getTableViewer() {
		return tableViewer;
	}
}
