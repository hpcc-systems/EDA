package com.lexisnexis.ui.datasummary;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.lexisnexis.ui.constants.Constants;
import com.lexisnexis.ui.dataprofiling.DataProfileTable;

public class DataSummaryTable{
	
	private TableViewer tv;
	private DataSummaryRecordList dataList;
	private String fileName;
	
	public DataSummaryTable(String fileName) {
		dataList = new DataSummaryRecordList(fileName);
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
	    tabDP.setText("Data Summary");
	    DataSummaryTable dst = new DataSummaryTable(this.fileName);
	    Composite prof = dst.createContents(folder);
	    tabDP.setControl(prof);
	    
	   // createContents(shell);
	    shell.open();
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch()) {
	        display.sleep();
	      }
	    }
	    display.dispose();
	}
	
	protected void configureShell(Shell shell) {
		shell.setSize(950, 400);
	}
	
	/**
	 * Add the columns to the table
	 * @param table
	 * @param parent
	 */
	public void addColumns(Table table, Shell parent){
		for(int i = 0; i < Constants.ARRAY_DS_COL_HEADERS.length; i++){
			TableColumn item = new TableColumn(table, SWT.LEFT);
			item.setText(Constants.ARRAY_DS_COL_HEADERS[i]);
			
			if(Constants.ARRAY_DS_COL_HEADERS[i].equals(Constants.KEY_DS_FIELD_NAME)) {
				item.setWidth(150);
			} else if(Constants.ARRAY_DS_COL_HEADERS[i].equals(Constants.KEY_DS_AVGLENGTH)) {
				item.setWidth(200);
			} else {
				item.setWidth(100);
			}
		}
	}
	
	public Composite createContents(CTabFolder parent) {
		
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new FillLayout());
		
		tv = new TableViewer(comp);
		tv.setContentProvider(new DataSummaryContentProvider());
		tv.setLabelProvider(new DataSummaryLabelProvider());
		
		final Table table = tv.getTable();
		
		addColumns(table, parent.getShell());	//Add Column Headers and Link Listener to table
		
		tv.setInput(dataList);
		comp.getShell().setText("Summary Report for "+((DataSummaryRow)dataList.getDataSummaryRecordsList().get(0)).getRecordLength()+" records");
		
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);
		
		return comp;
	}
	
	public static void main(String[] args) {
		String fileName = "C:\\Spoon Demos\\new\\salt\\out_dataprofiling2\\Dataprofiling_SummaryReport.csv";
		new DataSummaryTable(fileName).run();
	}
}
