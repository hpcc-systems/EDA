package com.lexisnexis.ui.sourceoutliers;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Image;
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
import org.eclipse.swt.widgets.Text;

import com.lexisnexis.ui.constants.Constants;
import com.lexisnexis.ui.dataprofiling.DataProfileTable;

public class SourceOutliersTable {
	
	private TableViewer tv;
	private SourceOutliersRecordList dataList;
	
	public SourceOutliersTable(String fileName) {
		dataList = new SourceOutliersRecordList(fileName);
	}
	
	public void run() {
		Display display = new Display();
	    Shell shell = new Shell(display);
	    shell.setLayout(new FillLayout());
	    
	    configureShell(shell);
	    //createContents(shell);
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
	    
	    this.createContents(folder);
	    
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
		for(int i = 0; i < Constants.ARRAY_SOURCE_OUTLIERS_COL_HEADERS.length; i++){
			TableColumn item = new TableColumn(table, SWT.LEFT);
			item.setText(Constants.ARRAY_SOURCE_OUTLIERS_COL_HEADERS[i]);
			if(Constants.ARRAY_SOURCE_OUTLIERS_COL_HEADERS[i].equals(Constants.KEY_SOURCE_OUTLIERS_FIELD_VALUE)) {
				item.setWidth(400);
			} else {
				item.setWidth(150);
			}
		}
	}
	
	public void createContents(CTabFolder folder) {
		
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL_HORIZONTAL;
		data.verticalAlignment = GridData.FILL_VERTICAL;
		folder.setSimple(false);
		
		CTabItem item = new CTabItem(folder, SWT.CLOSE);
		item.setText(Constants.SOURCE_OUTLIERS_TITLE);
		
		Composite comp = new Composite(folder, SWT.NONE);
		comp.setLayout(new FillLayout());
		
		tv = new TableViewer(comp);
		tv.setContentProvider(new SourceOutliersContentProvider());
		tv.setLabelProvider(new SourceOutliersLabelProvider());
		
		final Table table = tv.getTable();
		
		addColumns(table, comp.getShell());	//Add Column Headers and Link Listener to table
		
		tv.setInput(dataList);
		comp.getShell().setText(Constants.SOURCE_OUTLIERS_TITLE);
		
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);
	    
	    item.setControl(comp);
	    

	}
	
	public class SourceOutliersContentProvider implements IStructuredContentProvider{

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Object[] getElements(Object arg0) {
			return ((SourceOutliersRecordList)arg0).getSourceOutliersRecordsList().toArray();
		}
		
	}
	
	public class SourceOutliersLabelProvider implements ITableLabelProvider{

		@Override
		public void addListener(ILabelProviderListener arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void dispose() {
			//Nothing to dispose
		}

		@Override
		public boolean isLabelProperty(Object arg0, String arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Image getColumnImage(Object arg0, int arg1) {
			return null;
		}

		@Override
		public String getColumnText(Object arg0, int arg1) {
			SourceOutliersRow objSourceOutliersRow = (SourceOutliersRow)arg0;
			String text = "";
			switch(arg1) {
				case 0:
					text = String.valueOf(objSourceOutliersRow.getFieldName());
				break;
				case 1:
					text = String.valueOf(objSourceOutliersRow.getFieldValue());
				break;
				case 2:
					text = String.valueOf(objSourceOutliersRow.getSource());
				break;
				case 3:
					text = String.valueOf(objSourceOutliersRow.getCount());
				break;
				
			}
			return text;
		}

	}
	
	public static void main(String[] args) {
		String fileName = "C:\\spoon demos\\new\\salt\\out_hygine\\SrcOutliers.csv";
		new SourceOutliersTable(fileName).run();
	}
}
