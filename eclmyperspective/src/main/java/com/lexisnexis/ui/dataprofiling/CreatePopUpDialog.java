package com.lexisnexis.ui.dataprofiling;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

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

public class CreatePopUpDialog 	extends Dialog{

	public CreatePopUpDialog(Shell parent) {
		this(parent, SWT.RESIZE | SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	}

	public CreatePopUpDialog(Shell parent, int style) {
		super(parent, style);
	}
	
	public void open(int rowCount, String columnName, DataProfileRecordsList datalist, String isRow) {
		// Create the dialog window
		Shell shell = new Shell(getParent(), getStyle());
		Point pt = Display.getCurrent().getCursorLocation ();
		shell.setLocation (pt.x, pt.y);
		shell.setLayout(new FillLayout());
		
		if(Constants.ISROW.equals(isRow)) {
			shell.setSize(600,300);
			shell.setText("Row Details");
			createContentsForRow(shell, rowCount, columnName, datalist);
		}
		else if (Constants.ISCOL.equals(isRow)){
			shell.setSize(200,200);
			shell.setText(columnName + " Count");
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
	
	private void createContents(final Shell shell, int rowCount, String columnName, DataProfileRecordsList datalist) {
		Table tblDialog = new Table(shell, SWT.NONE);
		tblDialog.setLinesVisible(true);
		tblDialog.setHeaderVisible(true);
		
		String[] titles = null;
		if(columnName.equals(Constants.KEY_LENGTH)){
			titles = new String[]{Constants.TABLE_HEADER_LENGTH, Constants.TABLE_HEADER_COUNT};
		} else if(columnName.equals(Constants.KEY_WORDS)) {
			titles = new String[]{Constants.TABLE_HEADER_WORDS, Constants.TABLE_HEADER_COUNT};
		} else if(columnName.equals(Constants.KEY_CHARACTERS)) {
			titles = new String[]{Constants.TABLE_HEADER_CHAR, Constants.TABLE_HEADER_COUNT};
		} else if(columnName.equals(Constants.KEY_PATTERNS)) {
			titles = new String[]{Constants.TABLE_HEADER_DATA_PATTERN, Constants.TABLE_HEADER_COUNT};
		} else if(columnName.equals(Constants.KEY_FREQUENT_TERMS)) {
			titles = new String[]{Constants.TABLE_HEADER_VALUE, Constants.TABLE_HEADER_COUNT};
		} else if(columnName.equals(Constants.KEY_FIELDNAME)) {
			titles = new String[]{Constants.KEY_FIELDNUMBER, Constants.KEY_FIELDNAME, Constants.KEY_CARDINALITY, 
					Constants.KEY_LENGTH, Constants.TABLE_HEADER_COUNT, Constants.KEY_WORDS, Constants.TABLE_HEADER_COUNT, 
					Constants.KEY_CHARACTERS, Constants.TABLE_HEADER_COUNT, Constants.TABLE_HEADER_DATA_PATTERN, Constants.TABLE_HEADER_COUNT, 
					Constants.KEY_FREQUENT_TERMS, Constants.TABLE_HEADER_COUNT};
		}
		
	    for (int i = 0; i < titles.length; i++) {
	      TableColumn column = new TableColumn(tblDialog, SWT.NONE);
	      column.setText(titles[i]);
	    }
		
	    TableItem item = null;
		
	    List<DataProlifeRow> arlDataProfileRecords = datalist.getDataProfileRecordsList();
	    if(arlDataProfileRecords!= null && arlDataProfileRecords.size() > 0) {
	    	DataProlifeRow dataProlifeRow = arlDataProfileRecords.get(rowCount);
	    	Map<String, String> mapRecordRow = (Map<String, String>)dataProlifeRow.getMapRowDetails().get(columnName);
			Iterator<String> iter = mapRecordRow.keySet().iterator();
			while(iter.hasNext()) {
				String key = iter.next();
				String val = mapRecordRow.get(key);
				item = new TableItem(tblDialog, SWT.NONE);
				item.setText(0, key);
				item.setText(1, val);
			}
	    }
		
		for (int i=0; i<titles.length; i++) {
			tblDialog.getColumn (i).pack ();
		}
	}
	
	private void createContentsForRow(final Shell shell, int rowCount, String columnName, DataProfileRecordsList datalist) {
		
		Table tblDialog = new Table(shell, SWT.NONE);
		String[] titles = {Constants.KEY_FIELDNUMBER, Constants.KEY_FIELDNAME, Constants.KEY_CARDINALITY, 
					Constants.KEY_LENGTH, Constants.TABLE_HEADER_COUNT, Constants.KEY_WORDS, Constants.TABLE_HEADER_COUNT, 
					Constants.KEY_CHARACTERS, Constants.TABLE_HEADER_COUNT, Constants.TABLE_HEADER_DATA_PATTERN, Constants.TABLE_HEADER_COUNT, 
					Constants.KEY_FREQUENT_TERMS, Constants.TABLE_HEADER_COUNT};
		
	    for (int i = 0; i < titles.length; i++) {
	      TableColumn column = new TableColumn (tblDialog, SWT.NONE);
	      column.setText(titles[i]);
	      column.setWidth(100);
	    }
	    
	    TableItem item = null;
		
	    List<DataProlifeRow> arlDataProfileRecords = datalist.getDataProfileRecordsList();
	    if(arlDataProfileRecords!= null && arlDataProfileRecords.size() > 0) {
	    	DataProlifeRow dataProlifeRow = arlDataProfileRecords.get(rowCount);
	    	String[] arrLen = null;
	    	String[] arrLenCount = null;
	    	String[] arrWords = null;
	    	String[] arrWordsCount = null;
	    	String[] arrChars = null;
	    	String[] arrCharsCount = null;
	    	String[] arrPatterns = null;
	    	String[] arrPatternsCount = null;
	    	String[] arrFreqTerms = null;
	    	String[] arrFreqTermsCount = null;
	    	
	    	Map<String, Map<String, String>> map = dataProlifeRow.getMapRowDetails();
			Set<Entry<String, Map<String, String>>> setEntry = map.entrySet();
			Iterator<Entry<String, Map<String, String>>> itr = setEntry.iterator();
			while(itr.hasNext()){
				Entry<String, Map<String, String>> entry = itr.next();
				if((entry.getKey().equals(Constants.KEY_LENGTH))){ 
					Map<String, String> mapInner = (Map<String, String>)entry.getValue();
					arrLen = new String[mapInner.size()];
					arrLenCount = new String[mapInner.size()];
					int count = 0;
					Set<Entry<String, String>> entrySetInnerMap = (Set<Entry<String, String>>)mapInner.entrySet();
					Iterator<Entry<String, String>> itrInner = entrySetInnerMap.iterator();
					while(itrInner.hasNext()) {
						Entry<String, String> entryInner = itrInner.next();
				    	arrLen[count] = entryInner.getKey();
				    	arrLenCount[count] = entryInner.getValue();
				    	count++;
					}
				} else if((entry.getKey().equals(Constants.KEY_WORDS))){ 
					Map<String, String> mapInner = (Map<String, String>)entry.getValue();
					arrWords = new String[mapInner.size()];
					arrWordsCount = new String[mapInner.size()];
					int count = 0;
					Set<Entry<String, String>> entrySetInnerMap = (Set<Entry<String, String>>)mapInner.entrySet();
					Iterator<Entry<String, String>> itrInner = entrySetInnerMap.iterator();
					while(itrInner.hasNext()) {
						Entry<String, String> entryInner = itrInner.next();
						arrWords[count] = entryInner.getKey();
						arrWordsCount[count] = entryInner.getValue();
				    	count++;
					}
				} else if((entry.getKey().equals(Constants.KEY_CHARACTERS))){ 
					Map<String, String> mapInner = (Map<String, String>)entry.getValue();
					arrChars = new String[mapInner.size()];
					arrCharsCount = new String[mapInner.size()];
					int count = 0;
					Set<Entry<String, String>> entrySetInnerMap = (Set<Entry<String, String>>)mapInner.entrySet();
					Iterator<Entry<String, String>> itrInner = entrySetInnerMap.iterator();
					while(itrInner.hasNext()) {
						Entry<String, String> entryInner = itrInner.next();
						arrChars[count] = entryInner.getKey();
						arrCharsCount[count] = entryInner.getValue();
				    	count++;
					}
				} else if((entry.getKey().equals(Constants.KEY_PATTERNS))){ 
					Map<String, String> mapInner = (Map<String, String>)entry.getValue();
					arrPatterns = new String[mapInner.size()];
					arrPatternsCount = new String[mapInner.size()];
					int count = 0;
					Set<Entry<String, String>> entrySetInnerMap = (Set<Entry<String, String>>)mapInner.entrySet();
					Iterator<Entry<String, String>> itrInner = entrySetInnerMap.iterator();
					while(itrInner.hasNext()) {
						Entry<String, String> entryInner = itrInner.next();
						arrPatterns[count] = entryInner.getKey();
						arrPatternsCount[count] = entryInner.getValue();
				    	count++;
					}
				} else if((entry.getKey().equals(Constants.KEY_FREQUENT_TERMS))){ 
					Map<String, String> mapInner = (Map<String, String>)entry.getValue();
					arrFreqTerms = new String[mapInner.size()];
					arrFreqTermsCount = new String[mapInner.size()];
					int count = 0;
					Set<Entry<String, String>> entrySetInnerMap = (Set<Entry<String, String>>)mapInner.entrySet();
					Iterator<Entry<String, String>> itrInner = entrySetInnerMap.iterator();
					while(itrInner.hasNext()) {
						Entry<String, String> entryInner = itrInner.next();
						arrFreqTerms[count] = entryInner.getKey();
						arrFreqTermsCount[count] = entryInner.getValue();
				    	count++;
					}
				}
			}
			
			int[] arrVal = {arrLenCount.length, arrWordsCount.length, arrCharsCount.length, arrPatterns.length, arrFreqTerms.length};
	    	int maxValue = max(arrVal);
			
			for (int i = 0; i < maxValue; i++) {
	    		item = new TableItem(tblDialog, SWT.NONE);
	    		if(i == 0){
	    			item.setText(0, String.valueOf(dataProlifeRow.getFieldNumber()));
	    			item.setText(1, dataProlifeRow.getFieldName());
			    	item.setText(2, String.valueOf(dataProlifeRow.getCardinality()));
	    		}
	    		
	    		item.setText(3, i < arrLen.length ? arrLen[i]: "");
		    	item.setText(4, i < arrLenCount.length ? arrLenCount[i]: "");
		    	item.setText(5, i < arrWords.length ? arrWords[i]: "");
		    	item.setText(6, i < arrWordsCount.length ? arrWordsCount[i]: "");
		    	item.setText(7, i < arrChars.length ? arrChars[i]: "");
		    	item.setText(8, i < arrCharsCount.length ? arrCharsCount[i]: "");
		    	item.setText(9, i < arrPatterns.length ? arrPatterns[i]: "");
		    	item.setText(10, i < arrPatternsCount.length ? arrPatternsCount[i]: "");
		    	item.setText(11, i < arrFreqTerms.length ? arrFreqTerms[i]: "");
		    	item.setText(12, i < arrFreqTermsCount.length ? arrFreqTermsCount[i]: "");
			}
	    }
	    
	    tblDialog.setHeaderVisible(true);
	    tblDialog.setLinesVisible(true);
	    
	    for (int i=0; i<titles.length; i++) {
			tblDialog.getColumn (i).pack ();
		}
	}
	
	public int max(int[] t) {
	    int maximum = t[0];   // start with the first value
	    for (int i=1; i<t.length; i++) {
	        if (t[i] > maximum) {
	            maximum = t[i];   // new maximum
	        }
	    }
	    return maximum;
	}//end method max
}
