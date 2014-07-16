package com.lexisnexis.ui.dataprofiling;

import java.util.List;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.pentaho.di.core.gui.SpoonFactory;
import org.pentaho.di.ui.spoon.Spoon;

import com.lexisnexis.ui.constants.Constants;

public class DataProfileTable {
	
	private TableViewer viewer;
	private DataProfileRecordsList datalist;
	private String fileName;
	
	public DataProfileTable(String fileName){
		this.fileName = fileName;
		datalist = new DataProfileRecordsList(fileName);
	}
 
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void run() {
		Display display = new Display();
	    Shell shell = new Shell(display);
	    shell.setLayout(new FillLayout());
	    configureShell(shell);
	    
	    Composite comp = new Composite(shell, SWT.BORDER);
	    comp.setLayout(new GridLayout());
	    comp.setLayoutData(new GridData(GridData.FILL_BOTH));

	    Label lbl = new Label(comp, SWT.CENTER | SWT.TOP);
	    GridData ldata = new GridData(SWT.CENTER, SWT.TOP, true, false);
	    lbl.setLayoutData(ldata);
	    lbl.setText("Preview data returned from SALt");
	    
	    CTabFolder folder = new CTabFolder(comp, SWT.CLOSE);
	    folder.setSimple(false);
	    folder.setBorderVisible(true);
	    folder.setLayoutData(new GridData(GridData.FILL_BOTH));
	    CTabItem tabDP = new CTabItem(folder, SWT.NONE);
	    tabDP.setText("Data Profile");
	    DataProfileTable dpt = new DataProfileTable(this.fileName);
	    Composite prof = dpt.createContents(folder);
	    tabDP.setControl(prof);
	    
	    
	    //createContents(shell);
	    shell.open();
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch()) {
	        display.sleep();
	      }
	    }
	    display.dispose();
	}
	
	protected void configureShell(Shell shell) {
		shell.setText(Constants.DATA_PROFILING_REPORT_TITLE);
		shell.setSize(950, 400);
	}
	
	/**
	 * Add the columns to the table and add the listener
	 * @param table
	 * @param parent
	 */
	public void addColumns(Table table, Shell parent){
		for(int i = 0; i < Constants.ARRAY_COL_HEADERS.length; i++){
			TableColumn item = new TableColumn(table, SWT.LEFT);
			item.setText(Constants.ARRAY_COL_HEADERS[i]);
			
			if(Constants.ARRAY_COL_HEADERS[i].equals(Constants.KEY_FIELDNAME)) {
				item.setWidth(150);
				new LinkListener(viewer, item).displayLink(parent.getShell(), table, Constants.KEY_FIELDNAME, Constants.ISROW);
			} else if(Constants.ARRAY_COL_HEADERS[i].equals(Constants.KEY_FIELDNUMBER) || Constants.ARRAY_COL_HEADERS[i].equals(Constants.KEY_CARDINALITY) ) {
				item.setWidth(100);
			} else {
				item.setWidth(100);
				new LinkListener(viewer, item).displayLink(parent.getShell(), table, Constants.ARRAY_COL_HEADERS[i], Constants.ISCOL);
			}
		}
	}
	
	/**
	 * Create a Table and add Label and Content providers
	 * @param parent
	 * @return Control
	 */
	public Composite createContents(final CTabFolder parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());
		
		viewer = new TableViewer(composite);
		viewer.setContentProvider(new DataProfileContentProvider());
		viewer.setLabelProvider(new DataProfileLabelProvider());
		
		final Table table = viewer.getTable();
		
		addColumns(table, parent.getShell());	//Add Column Headers and Link Listener to table
	    viewer.setInput(datalist);
	    
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);
	    
		return composite;
	}
	
	/**
	 * This class will be used as a Listener class that will open a dialog box and display the results.
	 */
	class LinkListener {
		
		private TableViewer viewer;
		private TableColumn item;
		
		public LinkListener(TableViewer viewer, TableColumn item) {
			this.viewer = viewer;
			this.item = item;
		}
		
		/**
		 * Creates a link for each column and handles the link click event to which opens a dialog box 
		 * that displays the output result in tabular format
		 * @param parent - Shell
		 * @param table - table to attach the dialog
		 * @param colName - column header of the column the user clicked
		 * @param isRow - To display the entire row data or just the subset of data
		 */
		public void displayLink(final Shell parent, final Table table, final String colName, final String isRow){
			TableViewerColumn actionsNameCol = new TableViewerColumn(viewer, item);
			actionsNameCol.setLabelProvider(new ColumnLabelProvider(){
				@Override
				public void update(final ViewerCell cell) {

					final TableItem item = (TableItem) cell.getItem();
					Link link = new Link((Composite) cell.getViewerRow().getControl(), SWT.NONE);
					if(Constants.ISROW.equals(isRow)) {
						link.setText(Constants.LINK_ANCHOR_START +datalist.getDataProfileRecordsList().get( table.getItemCount() - 1 ).getFieldName()+ Constants.LINK_ANCHOR_END);
					} else if(Constants.ISCOL.equals(isRow)){
						link.setText(Constants.LINK_TEXT);
					}
					
					link.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
					link.setSize(100, 40);

					TableEditor editor = new TableEditor(item.getParent());
					editor.grabHorizontal = true;
					editor.grabVertical = true;
					editor.horizontalAlignment = SWT.CENTER;
					editor.setEditor(link, item, cell.getColumnIndex());
					editor.layout();
					
					//Add the listener thats displays the dialog 
					link.addListener(SWT.Selection, new Listener() {
						public void handleEvent(Event event) {
							CreatePopUpDialog dlg = new CreatePopUpDialog(parent.getShell());
					        dlg.open(table.indexOf(item), colName, datalist, isRow);
						}
					});
				}
			});
		}//end of displayLink()
		
	}
	
	public static void main(String[] args) {
		String fn = "C:\\Documents and Settings\\ChambeJX.RISK\\My Documents\\spoon-plugins\\spoon-plugins\\perspectives\\saltresults\\src\\main\\java\\com\\lexisnexis\\ui\\dataprofiling\\Dataprofiling_AllProfiles.csv";
		
		new DataProfileTable(fn).run();
	}
	
}
