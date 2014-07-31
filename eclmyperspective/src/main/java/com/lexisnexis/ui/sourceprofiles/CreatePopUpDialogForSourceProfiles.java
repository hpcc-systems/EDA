package com.lexisnexis.ui.sourceprofiles;

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

import com.lexisnexis.ui.clustersources.ClusterSourcesRecordList;
import com.lexisnexis.ui.clustersources.ClusterSourcesRow;
import com.lexisnexis.ui.clustersources.SourcesRow;
import com.lexisnexis.ui.constants.Constants;

public class CreatePopUpDialogForSourceProfiles extends Dialog {
	
	public CreatePopUpDialogForSourceProfiles(Shell parent) {
		this(parent, SWT.RESIZE | SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	}

	public CreatePopUpDialogForSourceProfiles(Shell parent, int style) {
		super(parent, style);
	}
	
	public void open(int rowCount, String columnName, SourceProfilesRecordList datalist, String isRow) {
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
	
	private void createContents(final Shell shell, int rowCount, String columnName, SourceProfilesRecordList datalist) {
		Table tblDialog = new Table(shell, SWT.NONE);
		tblDialog.setLinesVisible(true);
		tblDialog.setHeaderVisible(true);
		
		String[] titles = null;
		if(columnName.equals(Constants.SOURCE_PROFILES_PARENT_FIELDS)) {
			titles = new String[]{	Constants.SOURCE_PROFILES_FIELD, Constants.SOURCE_PROFILES_UNIQUE_VALUE, Constants.SOURCE_PROFILES_ONLY_VALUE, 
									Constants.SOURCE_PROFILES_GLOBALLY_UNIQUE_VALUE, Constants.SOURCE_PROFILES_IDS_WITH_SRC };
		}
		
	    for (int i = 0; i < titles.length; i++) {
	    	TableColumn column = new TableColumn(tblDialog, SWT.NONE);
	    	column.setText(titles[i]);
	    }
		
	    TableItem item = null;
		
	    SourceProfilesRow objSourceProfilesRow = datalist.getSourceProfilesRecordList().get(rowCount);
	    if(objSourceProfilesRow != null && objSourceProfilesRow.getArlProfilesFieldsRow().size() > 0) {
	    	List<SrcProfilesFieldsRow> listSrcProfilesFieldsRow = objSourceProfilesRow.getArlProfilesFieldsRow();
	    	for (Iterator<SrcProfilesFieldsRow> itr = listSrcProfilesFieldsRow.iterator(); itr.hasNext();) {
	    		SrcProfilesFieldsRow objSrcProfilesFieldsRow = itr.next();
	    		item = new TableItem(tblDialog, SWT.NONE);
	    		item.setText(0, objSrcProfilesFieldsRow.getField());
	    		item.setText(1, objSrcProfilesFieldsRow.getUniqueValue());
	    		item.setText(2, objSrcProfilesFieldsRow.getOnlyValue());
	    		item.setText(3, objSrcProfilesFieldsRow.getGloballyUniqueValue());
	    		item.setText(4, objSrcProfilesFieldsRow.getIdsWithSrc());
	    	}
	    }
		
		for (int i=0; i<titles.length; i++) {
			tblDialog.getColumn (i).pack ();
		}
	}
	
}
