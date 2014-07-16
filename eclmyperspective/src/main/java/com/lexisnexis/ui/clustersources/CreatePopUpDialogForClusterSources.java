package com.lexisnexis.ui.clustersources;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.lexisnexis.ui.constants.Constants;

public class CreatePopUpDialogForClusterSources extends Dialog{
	
	public CreatePopUpDialogForClusterSources(Shell parent) {
		this(parent, SWT.RESIZE | SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	}

	public CreatePopUpDialogForClusterSources(Shell parent, int style) {
		super(parent, style);
	}
	
	public void open(int rowCount, String columnName, ClusterSourcesRecordList datalist, String isRow) {
		// Create the dialog window
		Shell shell = new Shell(getParent(), getStyle());
		Point pt = Display.getCurrent().getCursorLocation ();
		shell.setLocation (pt.x, pt.y);
		shell.setLayout(new FillLayout());
		
		if (Constants.ISCOL.equals(isRow)){
			shell.setSize(600,200);
			shell.setText(columnName);
			createContents(shell, rowCount, columnName, datalist);
		}
		
		shell.open();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	private void createContents(final Shell shell, int rowCount, String columnName, ClusterSourcesRecordList datalist) {
		Table tblDialog = new Table(shell, SWT.NONE);
		tblDialog.setLinesVisible(true);
		tblDialog.setHeaderVisible(true);
		
		String[] titles = null;
		if(columnName.equals(Constants.CLUSTER_SOURCES)) {
			titles = new String[]{Constants.CLUSTER_SOURCES_SRC, Constants.CLUSTER_SOURCES_COOCCUR_PCNT, Constants.CLUSTER_SOURCES_COOCCUR, Constants.CLUSTER_SOURCES_EXPECTED_COOCCUR };
		}
		
	    for (int i = 0; i < titles.length; i++) {
	    	TableColumn column = new TableColumn(tblDialog, SWT.NONE);
	    	column.setText(titles[i]);
	    }
		
	    TableItem item = null;
		
	    ClusterSourcesRow clusterSourcesRow = datalist.getClusterSourcesRecordList().get(rowCount);
	    if(clusterSourcesRow != null && clusterSourcesRow.getArlSourcesRow().size() > 0) {
	    	List<SourcesRow> listSourcesRow = clusterSourcesRow.getArlSourcesRow();
	    	for (Iterator<SourcesRow> itr = listSourcesRow.iterator(); itr.hasNext();) {
	    		SourcesRow sourcesRow = itr.next();
	    		item = new TableItem(tblDialog, SWT.NONE);
	    		item.setText(0, sourcesRow.getSource());
	    		item.setText(1, sourcesRow.getCooccurPcnt());
	    		item.setText(2, sourcesRow.getCooccur());
	    		item.setText(3, sourcesRow.getExpectedcooccur());
	    	}
	    }
		
		for (int i=0; i<titles.length; i++) {
			tblDialog.getColumn (i).pack ();
		}
	}
}
