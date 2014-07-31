package com.lexisnexis.ui.dataprofiling;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.lexisnexis.ui.constants.Constants;

public class DataProfileLabelProvider implements ITableLabelProvider{
	
	public DataProfileLabelProvider() { }
	
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
		DataProlifeRow objDataProlifeRow = (DataProlifeRow)arg0;
		String text = "";
		switch(arg1) {
			case Constants.COLUMN_FIELD_NUMBER:
				text = String.valueOf(objDataProlifeRow.getFieldNumber());
			break;
			case Constants.COLUMN_DP_FIELD_NAME:
				text = String.valueOf(objDataProlifeRow.getFieldName());
			break;
			case Constants.COLUMN_CARDINALITY:
				text = String.valueOf(objDataProlifeRow.getCardinality());
			break;
			case Constants.COLUMN_LENGTH:
				text = objDataProlifeRow.getLength();
			break;
			case Constants.COLUMN_WORDS:
				text = objDataProlifeRow.getWords();
			break;
			case Constants.COLUMN_CHARACTERS:
				text = objDataProlifeRow.getCharacters();
			break;
			case Constants.COLUMN_PATTERNS:
				text = objDataProlifeRow.getPatterns();
			break;
			case Constants.COLUMN_FREQUENT_TERMS:
				text = objDataProlifeRow.getFrequentTerms();
			break;
		}
		return text;
	}

}
