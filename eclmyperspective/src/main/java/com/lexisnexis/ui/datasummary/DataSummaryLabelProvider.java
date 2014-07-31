package com.lexisnexis.ui.datasummary;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.lexisnexis.ui.constants.Constants;

public class DataSummaryLabelProvider implements ITableLabelProvider{

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
		DataSummaryRow objDataSummaryRow = (DataSummaryRow)arg0;
		String text = "";
		switch(arg1) {
			case Constants.COLUMN_DS_FIELD_NAME:
				text = String.valueOf(objDataSummaryRow.getFieldName());
			break;
			case Constants.COLUMN_POPULATED:
				text = String.valueOf(objDataSummaryRow.getPopulated());
			break;
			case Constants.COLUMN_MAXLENGTH:
				text = String.valueOf(objDataSummaryRow.getMaxLength());
			break;
			case Constants.COLUMN_AVGLENGTH:
				text = String.valueOf(objDataSummaryRow.getAverageLength());
			break;
		}
		return text;
	}

}
