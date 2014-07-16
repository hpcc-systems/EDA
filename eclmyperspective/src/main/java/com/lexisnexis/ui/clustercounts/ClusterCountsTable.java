package com.lexisnexis.ui.clustercounts;

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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.lexisnexis.ui.constants.Constants;
import com.lexisnexis.ui.sourceoutliers.SourceOutliersRecordList;
import com.lexisnexis.ui.sourceoutliers.SourceOutliersRow;
import com.lexisnexis.ui.sourceoutliers.SourceOutliersTable;
import com.lexisnexis.ui.sourceoutliers.SourceOutliersTable.SourceOutliersContentProvider;
import com.lexisnexis.ui.sourceoutliers.SourceOutliersTable.SourceOutliersLabelProvider;

public class ClusterCountsTable {
	
	private TableViewer tv;
	private ClusterCountsRecordList dataList;
	
	public ClusterCountsTable(String fileName) {
		dataList = new ClusterCountsRecordList(fileName);
	}
	
	public void run() {
		Display display = new Display();
	    Shell shell = new Shell(display);
	    shell.setLayout(new FillLayout());
	    configureShell(shell);
	    final CTabFolder folder = new CTabFolder(shell, SWT.BORDER);
	    createContents(folder);
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
		for(int i = 0; i < Constants.ARRAY_CLUSTER_COUNTS_COL_HEADERS.length; i++){
			TableColumn item = new TableColumn(table, SWT.LEFT);
			item.setText(Constants.ARRAY_CLUSTER_COUNTS_COL_HEADERS[i]);
			item.setWidth(150);
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
		item.setText(Constants.CLUSTER_COUNTS_TITLE);
		
		Composite comp = new Composite(folder, SWT.NONE);
		comp.setLayout(new FillLayout());
		
		tv = new TableViewer(comp);
		tv.setContentProvider(new ClusterCountsContentProvider());
		tv.setLabelProvider(new ClusterCountsLabelProvider());
		
		final Table table = tv.getTable();
		
		addColumns(table, comp.getShell());	//Add Column Headers and Link Listener to table
		
		tv.setInput(dataList);
		comp.getShell().setText(Constants.CLUSTER_COUNTS_TITLE);
		
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);
	    
	    item.setControl(comp);
		
		
	}
	
	public class ClusterCountsContentProvider implements IStructuredContentProvider{

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
			return ((ClusterCountsRecordList)arg0).getClusterCountsRecordsList().toArray();
		}
		
	}
	
	public class ClusterCountsLabelProvider implements ITableLabelProvider{

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
			ClusterCountsRow objClusterCountsRow = (ClusterCountsRow)arg0;
			String text = "";
			switch(arg1) {
				case 0:
					text = String.valueOf(objClusterCountsRow.getIncluster());
				break;
				case 1:
					text = String.valueOf(objClusterCountsRow.getNumberOfClusters());
				break;			
			}
			return text;
		}

	}
	
	public static void main(String[] args) {
		String fileName = "C:\\spoon demos\\new\\salt\\out_hygine\\ClusterCounts.csv";
		new ClusterCountsTable(fileName).run();
	}
	
}
